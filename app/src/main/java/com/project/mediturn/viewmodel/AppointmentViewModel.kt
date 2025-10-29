package com.project.mediturn.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.project.mediturn.data.model.Appointment
import com.project.mediturn.data.model.Doctor
import com.project.mediturn.data.repository.AppointmentRepository
import com.project.mediturn.data.repository.AuthRepository
import com.project.mediturn.util.Constants
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class AppointmentViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = AppointmentRepository(application.applicationContext)
    private val authRepository = AuthRepository(application.applicationContext)

    // ========== STATE ==========

    private val _uiState = MutableStateFlow<AppointmentUiState>(AppointmentUiState.Loading)
    val uiState: StateFlow<AppointmentUiState> = _uiState.asStateFlow()

    private val _appointments = MutableStateFlow<List<Appointment>>(emptyList())
    val appointments: StateFlow<List<Appointment>> = _appointments.asStateFlow()

    private val _selectedAppointment = MutableStateFlow<Appointment?>(null)
    val selectedAppointment: StateFlow<Appointment?> = _selectedAppointment.asStateFlow()

    // Estados del formulario
    private val _selectedDoctor = MutableStateFlow<Doctor?>(null)
    val selectedDoctor: StateFlow<Doctor?> = _selectedDoctor.asStateFlow()

    private val _selectedDateTime = MutableStateFlow<LocalDateTime?>(null)
    val selectedDateTime: StateFlow<LocalDateTime?> = _selectedDateTime.asStateFlow()

    private val _reason = MutableStateFlow("")
    val reason: StateFlow<String> = _reason.asStateFlow()

    private val _isTelemedicine = MutableStateFlow(false)
    val isTelemedicine: StateFlow<Boolean> = _isTelemedicine.asStateFlow()

    // Mensajes de acción
    private val _actionMessage = MutableSharedFlow<ActionMessage>()
    val actionMessage: SharedFlow<ActionMessage> = _actionMessage.asSharedFlow()

    // ========== ACTIONS - CARGAR DATOS ==========

    /**
     * Cargar citas del paciente actual
     */
    fun loadAppointments() {
        viewModelScope.launch {
            _uiState.value = AppointmentUiState.Loading

            val patientId = authRepository.getSessionPatientId() ?: Constants.DEFAULT_PATIENT_ID

            repository.getPatientAppointments(patientId)
                .onSuccess { appointments ->
                    _appointments.value = appointments
                    _uiState.value = AppointmentUiState.AppointmentsList(
                        upcoming = appointments.filter {
                            it.dateTime.isAfter(LocalDateTime.now())
                        },
                        past = appointments.filter {
                            it.dateTime.isBefore(LocalDateTime.now())
                        }
                    )
                }
                .onFailure { error ->
                    _uiState.value = AppointmentUiState.Error(
                        error.message ?: "Error al cargar citas"
                    )
                }
        }
    }

    /**
     * Cargar detalle de una cita
     */
    fun loadAppointmentById(appointmentId: Int) {
        viewModelScope.launch {
            _uiState.value = AppointmentUiState.Loading

            repository.getAppointmentById(appointmentId)
                .onSuccess { appointment ->
                    _selectedAppointment.value = appointment
                    _uiState.value = AppointmentUiState.AppointmentDetail(appointment)
                }
                .onFailure { error ->
                    _uiState.value = AppointmentUiState.Error(
                        error.message ?: "Cita no encontrada"
                    )
                    _actionMessage.emit(
                        ActionMessage.Error("Cita no encontrada")
                    )
                }
        }
    }

    // ========== ACTIONS - FORMULARIO ==========

    /**
     * Actualizar médico seleccionado
     */
    fun updateSelectedDoctor(doctor: Doctor?) {
        _selectedDoctor.value = doctor
    }

    /**
     * Actualizar fecha y hora seleccionada
     */
    fun updateSelectedDateTime(dateTime: LocalDateTime?) {
        _selectedDateTime.value = dateTime
    }

    /**
     * Actualizar motivo
     */
    fun updateReason(reason: String) {
        _reason.value = reason
    }

    /**
     * Toggle telemedicina
     */
    fun toggleTelemedicine(enabled: Boolean) {
        _isTelemedicine.value = enabled
    }

    /**
     * Limpiar formulario
     */
    fun clearForm() {
        _selectedDoctor.value = null
        _selectedDateTime.value = null
        _reason.value = ""
        _isTelemedicine.value = false
    }

    /**
     * Validar si el formulario está completo
     */
    fun isFormValid(): Boolean {
        return _selectedDoctor.value != null &&
                _selectedDateTime.value != null &&
                _reason.value.length >= Constants.MIN_REASON_LENGTH
    }

    // ========== ACTIONS - CRUD ==========

    /**
     * Crear nueva cita
     */
    fun createAppointment() {
        viewModelScope.launch {
            val doctor = _selectedDoctor.value
            val dateTime = _selectedDateTime.value
            val reason = _reason.value
            val isTelemedicine = _isTelemedicine.value

            if (doctor == null || dateTime == null) {
                _actionMessage.emit(
                    ActionMessage.Error("Complete todos los campos requeridos")
                )
                return@launch
            }

            if (reason.length < Constants.MIN_REASON_LENGTH) {
                _actionMessage.emit(
                    ActionMessage.Error(Constants.ERROR_SHORT_REASON)
                )
                return@launch
            }

            _uiState.value = AppointmentUiState.Processing

            val patientId = authRepository.getSessionPatientId() ?: Constants.DEFAULT_PATIENT_ID

            repository.createAppointment(
                patientId = patientId,
                doctor = doctor,
                dateTime = dateTime,
                reason = reason,
                isTelemedicine = isTelemedicine
            )
                .onSuccess { appointment ->
                    _selectedAppointment.value = appointment
                    _uiState.value = AppointmentUiState.Success(appointment)
                    _actionMessage.emit(
                        ActionMessage.Success(Constants.SUCCESS_APPOINTMENT_CREATED)
                    )
                    clearForm()

                    // Recargar lista de citas
                    loadAppointments()
                }
                .onFailure { error ->
                    _uiState.value = AppointmentUiState.Error(
                        error.message ?: "Error al crear cita"
                    )
                    _actionMessage.emit(
                        ActionMessage.Error(error.message ?: "Error al crear cita")
                    )
                }
        }
    }

    /**
     * Reprogramar cita
     */
    fun rescheduleAppointment(
        appointmentId: Int,
        newDateTime: LocalDateTime
    ) {
        viewModelScope.launch {
            val appointment = _selectedAppointment.value

            if (appointment == null) {
                _actionMessage.emit(ActionMessage.Error("Cita no encontrada"))
                return@launch
            }

            if (!repository.canReschedule(appointment)) {
                _actionMessage.emit(
                    ActionMessage.Error("Esta cita no puede ser reprogramada")
                )
                return@launch
            }

            _uiState.value = AppointmentUiState.Processing

            repository.rescheduleAppointment(appointmentId, newDateTime)
                .onSuccess { updatedAppointment ->
                    _selectedAppointment.value = updatedAppointment
                    _uiState.value = AppointmentUiState.Success(updatedAppointment)
                    _actionMessage.emit(
                        ActionMessage.Success(Constants.SUCCESS_APPOINTMENT_RESCHEDULED)
                    )

                    // Recargar lista
                    loadAppointments()
                }
                .onFailure { error ->
                    _uiState.value = AppointmentUiState.Error(
                        error.message ?: "Error al reprogramar"
                    )
                    _actionMessage.emit(
                        ActionMessage.Error(error.message ?: "Error al reprogramar")
                    )
                }
        }
    }

    /**
     * Cancelar cita
     */
    fun cancelAppointment(appointmentId: Int) {
        viewModelScope.launch {
            val appointment = _selectedAppointment.value

            if (appointment == null) {
                _actionMessage.emit(ActionMessage.Error("Cita no encontrada"))
                return@launch
            }

            if (!repository.canCancel(appointment)) {
                _actionMessage.emit(
                    ActionMessage.Error("Esta cita no puede ser cancelada")
                )
                return@launch
            }

            _uiState.value = AppointmentUiState.Processing

            repository.cancelAppointment(appointmentId)
                .onSuccess { cancelledAppointment ->
                    _selectedAppointment.value = cancelledAppointment
                    _uiState.value = AppointmentUiState.Success(cancelledAppointment)
                    _actionMessage.emit(
                        ActionMessage.Success(Constants.SUCCESS_APPOINTMENT_CANCELLED)
                    )

                    // Recargar lista
                    loadAppointments()
                }
                .onFailure { error ->
                    _uiState.value = AppointmentUiState.Error(
                        error.message ?: "Error al cancelar"
                    )
                    _actionMessage.emit(
                        ActionMessage.Error(error.message ?: "Error al cancelar")
                    )
                }
        }
    }

    /**
     * Confirmar cita
     */
    fun confirmAppointment(appointmentId: Int) {
        viewModelScope.launch {
            _uiState.value = AppointmentUiState.Processing

            repository.confirmAppointment(appointmentId)
                .onSuccess { confirmedAppointment ->
                    _selectedAppointment.value = confirmedAppointment
                    _actionMessage.emit(
                        ActionMessage.Success(Constants.SUCCESS_APPOINTMENT_CONFIRMED)
                    )
                    loadAppointments()
                }
                .onFailure { error ->
                    _actionMessage.emit(
                        ActionMessage.Error(error.message ?: "Error al confirmar")
                    )
                }
        }
    }

    // ========== HELPERS ==========

    /**
     * Verificar si una cita puede ser cancelada
     */
    fun canCancel(appointment: Appointment): Boolean {
        return repository.canCancel(appointment)
    }

    /**
     * Verificar si una cita puede ser reprogramada
     */
    fun canReschedule(appointment: Appointment): Boolean {
        return repository.canReschedule(appointment)
    }
}

// ========== UI STATES ==========

sealed class AppointmentUiState {
    object Loading : AppointmentUiState()
    object Processing : AppointmentUiState()

    data class AppointmentsList(
        val upcoming: List<Appointment>,
        val past: List<Appointment>
    ) : AppointmentUiState()

    data class AppointmentDetail(
        val appointment: Appointment
    ) : AppointmentUiState()

    data class Success(
        val appointment: Appointment
    ) : AppointmentUiState()

    data class Error(
        val message: String
    ) : AppointmentUiState()
}

// ========== ACTION MESSAGES ==========

sealed class ActionMessage {
    data class Success(val message: String) : ActionMessage()
    data class Error(val message: String) : ActionMessage()
}