package com.project.mediturn.data.repository

import com.project.mediturn.data.DataSource
import com.project.mediturn.data.model.Appointment
import com.project.mediturn.data.model.AppointmentStatus
import com.project.mediturn.data.model.Doctor
import kotlinx.coroutines.delay
import java.time.LocalDateTime

class AppointmentRepository {

    /**
     * Crear nueva cita
     */
    suspend fun createAppointment(
        patientId: Int,
        doctor: Doctor,
        dateTime: LocalDateTime,
        reason: String,
        isTelemedicine: Boolean
    ): Result<Appointment> {
        return try {
            delay(1000) // Simular latencia de red

            // Validar que el horario esté disponible
            val slot = doctor.availableSlots.find {
                it.dateTime == dateTime && it.isAvailable
            }

            if (slot == null) {
                return Result.failure(Exception("Horario no disponible"))
            }

            // Crear nueva cita
            val newAppointment = Appointment(
                id = DataSource.appointments.maxOfOrNull { it.id }?.plus(1) ?: 1,
                patientId = patientId,
                doctor = doctor,
                dateTime = dateTime,
                reason = reason,
                isTelemedicine = isTelemedicine,
                status = AppointmentStatus.PENDING,
                createdAt = LocalDateTime.now()
            )

            // Agregar a la lista de citas
            DataSource.appointments.add(newAppointment)

            // Marcar el slot como no disponible
            val doctorInList = DataSource.doctors.find { it.id == doctor.id }
            doctorInList?.availableSlots?.find { it.id == slot.id }?.let {
                // En una implementación real, actualizaríamos el slot
                // Por ahora, simplemente devolvemos la cita
            }

            Result.success(newAppointment)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Obtener todas las citas del paciente
     */
    suspend fun getPatientAppointments(patientId: Int): Result<List<Appointment>> {
        return try {
            delay(600)
            val appointments = DataSource.appointments
                .filter { it.patientId == patientId }
                .sortedBy { it.dateTime }
            Result.success(appointments)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Obtener citas próximas
     */
    suspend fun getUpcomingAppointments(patientId: Int): Result<List<Appointment>> {
        return try {
            delay(500)
            val appointments = DataSource.getUpcomingAppointments()
                .filter { it.patientId == patientId }
            Result.success(appointments)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Obtener historial de citas
     */
    suspend fun getPastAppointments(patientId: Int): Result<List<Appointment>> {
        return try {
            delay(500)
            val appointments = DataSource.getPastAppointments()
                .filter { it.patientId == patientId }
            Result.success(appointments)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Obtener cita por ID
     */
    suspend fun getAppointmentById(appointmentId: Int): Result<Appointment> {
        return try {
            delay(300)
            val appointment = DataSource.getAppointmentById(appointmentId)
            if (appointment != null) {
                Result.success(appointment)
            } else {
                Result.failure(Exception("Cita no encontrada"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Reprogramar cita
     */
    suspend fun rescheduleAppointment(
        appointmentId: Int,
        newDateTime: LocalDateTime
    ): Result<Appointment> {
        return try {
            delay(800)

            val appointment = DataSource.appointments.find { it.id == appointmentId }
                ?: return Result.failure(Exception("Cita no encontrada"))

            // Validar que el nuevo horario esté disponible
            val doctor = appointment.doctor
            val slot = doctor.availableSlots.find {
                it.dateTime == newDateTime && it.isAvailable
            }

            if (slot == null) {
                return Result.failure(Exception("Horario no disponible"))
            }

            // Actualizar la cita
            val updatedAppointment = appointment.copy(
                dateTime = newDateTime,
                status = AppointmentStatus.PENDING
            )

            // Reemplazar en la lista
            val index = DataSource.appointments.indexOfFirst { it.id == appointmentId }
            if (index != -1) {
                DataSource.appointments[index] = updatedAppointment
            }

            Result.success(updatedAppointment)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Cancelar cita
     */
    suspend fun cancelAppointment(appointmentId: Int): Result<Appointment> {
        return try {
            delay(600)

            val appointment = DataSource.appointments.find { it.id == appointmentId }
                ?: return Result.failure(Exception("Cita no encontrada"))

            // Actualizar el estado
            val cancelledAppointment = appointment.copy(
                status = AppointmentStatus.CANCELLED
            )

            // Reemplazar en la lista
            val index = DataSource.appointments.indexOfFirst { it.id == appointmentId }
            if (index != -1) {
                DataSource.appointments[index] = cancelledAppointment
            }

            // Liberar el slot (en implementación real)

            Result.success(cancelledAppointment)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Confirmar cita
     */
    suspend fun confirmAppointment(appointmentId: Int): Result<Appointment> {
        return try {
            delay(500)

            val appointment = DataSource.appointments.find { it.id == appointmentId }
                ?: return Result.failure(Exception("Cita no encontrada"))

            val confirmedAppointment = appointment.copy(
                status = AppointmentStatus.CONFIRMED
            )

            val index = DataSource.appointments.indexOfFirst { it.id == appointmentId }
            if (index != -1) {
                DataSource.appointments[index] = confirmedAppointment
            }

            Result.success(confirmedAppointment)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Completar cita
     */
    suspend fun completeAppointment(appointmentId: Int): Result<Appointment> {
        return try {
            delay(400)

            val appointment = DataSource.appointments.find { it.id == appointmentId }
                ?: return Result.failure(Exception("Cita no encontrada"))

            val completedAppointment = appointment.copy(
                status = AppointmentStatus.COMPLETED
            )

            val index = DataSource.appointments.indexOfFirst { it.id == appointmentId }
            if (index != -1) {
                DataSource.appointments[index] = completedAppointment
            }

            Result.success(completedAppointment)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Verificar si una cita se puede reprogramar
     */
    fun canReschedule(appointment: Appointment): Boolean {
        return appointment.status in listOf(
            AppointmentStatus.PENDING,
            AppointmentStatus.CONFIRMED
        ) && appointment.dateTime.isAfter(LocalDateTime.now())
    }

    /**
     * Verificar si una cita se puede cancelar
     */
    fun canCancel(appointment: Appointment): Boolean {
        return appointment.status in listOf(
            AppointmentStatus.PENDING,
            AppointmentStatus.CONFIRMED
        ) && appointment.dateTime.isAfter(LocalDateTime.now())
    }
}