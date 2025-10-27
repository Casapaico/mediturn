package com.project.mediturn.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.mediturn.data.DataSource
import com.project.mediturn.data.model.Appointment
import com.project.mediturn.data.model.AppointmentStatus
import com.project.mediturn.data.model.Doctor
import com.project.mediturn.data.model.TimeSlot
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date

data class AppointmentState(
    val appointments: List<Appointment> = emptyList(),
    val upcomingAppointments: List<Appointment> = emptyList(),
    val pastAppointments: List<Appointment> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val successMessage: String? = null
)

data class BookAppointmentState(
    val selectedDoctor: Doctor? = null,
    val selectedDate: Date? = null,
    val selectedTimeSlot: TimeSlot? = null,
    val reason: String = "",
    val isTelemedicine: Boolean = false,
    val availableTimeSlots: List<TimeSlot> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class AppointmentViewModel : ViewModel() {
    private val _appointmentState = MutableStateFlow(AppointmentState())
    val appointmentState: StateFlow<AppointmentState> = _appointmentState.asStateFlow()

    private val _bookAppointmentState = MutableStateFlow(BookAppointmentState())
    val bookAppointmentState: StateFlow<BookAppointmentState> = _bookAppointmentState.asStateFlow()

    init {
        loadAppointments()
    }

    fun loadAppointments() {
        _appointmentState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            try {
                val appointments = DataSource.appointments
                val now = Date()

                val upcoming = appointments.filter {
                    it.dateTime.after(now) && it.status != AppointmentStatus.CANCELLED
                }
                val past = appointments.filter {
                    it.dateTime.before(now) || it.status == AppointmentStatus.CANCELLED
                }

                _appointmentState.update {
                    it.copy(
                        appointments = appointments,
                        upcomingAppointments = upcoming,
                        pastAppointments = past,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _appointmentState.update {
                    it.copy(
                        error = "Error al cargar citas: ${e.message}",
                        isLoading = false
                    )
                }
            }
        }
    }

    fun bookAppointment(
        doctor: Doctor,
        dateTime: Date,
        reason: String,
        isTelemedicine: Boolean
    ) {
        _bookAppointmentState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            try {
                // Simular creación de cita
                val newAppointment = Appointment(
                    id = (DataSource.appointments.maxOfOrNull { it.id } ?: 0) + 1,
                    patientId = 1, // ID del paciente actual
                    doctor = doctor,
                    dateTime = dateTime,
                    reason = reason,
                    isTelemedicine = isTelemedicine,
                    status = AppointmentStatus.PENDING,
                    createdAt = Date()
                )

                _bookAppointmentState.update {
                    it.copy(
                        isLoading = false,
                        error = null
                    )
                }

                _appointmentState.update { state ->
                    state.copy(
                        successMessage = "Cita agendada exitosamente",
                        appointments = state.appointments + newAppointment
                    )
                }
            } catch (e: Exception) {
                _bookAppointmentState.update {
                    it.copy(
                        error = "Error al agendar cita: ${e.message}",
                        isLoading = false
                    )
                }
            }
        }
    }

    fun cancelAppointment(appointmentId: Int) {
        viewModelScope.launch {
            try {
                // Simular cancelación de cita
                val updatedAppointments = _appointmentState.value.appointments.map { appointment ->
                    if (appointment.id == appointmentId) {
                        appointment.copy(status = AppointmentStatus.CANCELLED)
                    } else {
                        appointment
                    }
                }

                _appointmentState.update {
                    it.copy(
                        appointments = updatedAppointments,
                        successMessage = "Cita cancelada exitosamente"
                    )
                }

                loadAppointments() // Recargar para actualizar las listas
            } catch (e: Exception) {
                _appointmentState.update {
                    it.copy(error = "Error al cancelar cita: ${e.message}")
                }
            }
        }
    }

    fun setSelectedDoctor(doctor: Doctor) {
        _bookAppointmentState.update { it.copy(selectedDoctor = doctor) }
    }

    fun setSelectedDate(date: Date) {
        _bookAppointmentState.update {
            it.copy(
                selectedDate = date,
                selectedTimeSlot = null // Reset time slot when date changes
            )
        }
    }

    fun setSelectedTimeSlot(timeSlot: TimeSlot) {
        _bookAppointmentState.update { it.copy(selectedTimeSlot = timeSlot) }
    }

    fun setReason(reason: String) {
        _bookAppointmentState.update { it.copy(reason = reason) }
    }

    fun setIsTelemedicine(isTelemedicine: Boolean) {
        _bookAppointmentState.update { it.copy(isTelemedicine = isTelemedicine) }
    }

    fun clearError() {
        _appointmentState.update { it.copy(error = null, successMessage = null) }
        _bookAppointmentState.update { it.copy(error = null) }
    }

    fun clearBookAppointmentState() {
        _bookAppointmentState.value = BookAppointmentState()
    }
}