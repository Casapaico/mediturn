package com.project.mediturn.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.mediturn.data.model.Appointment
import com.project.mediturn.data.model.AppointmentStatus
import com.project.mediturn.data.model.Doctor
import com.project.mediturn.data.model.TimeSlot
import com.project.mediturn.data.repository.AppointmentRepository
import com.project.mediturn.data.repository.TimeSlotRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.*

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

class AppointmentViewModel(
    private val appointmentRepository: AppointmentRepository,
    private val timeSlotRepository: TimeSlotRepository
) : ViewModel() {
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
                val currentTime = Date()
                appointmentRepository.getUpcomingAppointments(currentTime).collect { upcoming ->
                    appointmentRepository.getPastAppointments(currentTime).collect { past ->
                        _appointmentState.update {
                            it.copy(
                                appointments = upcoming + past,
                                upcomingAppointments = upcoming,
                                pastAppointments = past,
                                isLoading = false
                            )
                        }
                    }
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
        doctorId: Int,
        dateTime: Date,
        reason: String,
        isTelemedicine: Boolean
    ) {
        _bookAppointmentState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            try {
                val newAppointment = Appointment(
                    id = (System.currentTimeMillis() % Int.MAX_VALUE).toInt(),
                    patientId = 1,
                    doctorId = doctorId,
                    dateTime = dateTime,
                    reason = reason,
                    isTelemedicine = isTelemedicine,
                    status = AppointmentStatus.PENDING,
                    createdAt = Date()
                )

                val success = appointmentRepository.bookAppointment(newAppointment)

                if (success) {
                    _bookAppointmentState.update {
                        it.copy(
                            isLoading = false,
                            error = null
                        )
                    }
                    _appointmentState.update { state ->
                        state.copy(
                            successMessage = "Cita agendada exitosamente"
                        )
                    }
                    loadAppointments()
                } else {
                    _bookAppointmentState.update {
                        it.copy(
                            error = "Error al agendar la cita",
                            isLoading = false
                        )
                    }
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
                val success = appointmentRepository.cancelAppointment(appointmentId)
                if (success) {
                    _appointmentState.update {
                        it.copy(
                            successMessage = "Cita cancelada exitosamente"
                        )
                    }
                    loadAppointments()
                } else {
                    _appointmentState.update {
                        it.copy(error = "Error al cancelar la cita")
                    }
                }
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
                selectedTimeSlot = null
            )
        }
        loadAvailableTimeSlots(date)
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

    private fun loadAvailableTimeSlots(date: Date) {
        val selectedDoctor = _bookAppointmentState.value.selectedDoctor
        selectedDoctor?.let { doctor ->
            val availableSlots = timeSlotRepository.getAvailableTimeSlotsForDoctor(doctor.id, date)
            _bookAppointmentState.update { it.copy(availableTimeSlots = availableSlots) }
        }
    }

    fun clearError() {
        _appointmentState.update { it.copy(error = null, successMessage = null) }
        _bookAppointmentState.update { it.copy(error = null) }
    }

    fun clearBookAppointmentState() {
        _bookAppointmentState.value = BookAppointmentState()
    }
}