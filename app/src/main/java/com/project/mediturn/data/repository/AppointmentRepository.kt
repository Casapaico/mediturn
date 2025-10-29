package com.project.mediturn.data.repository

import android.content.Context
import com.project.mediturn.data.local.MediTurnDatabase
import com.project.mediturn.data.local.mapper.*
import com.project.mediturn.data.model.*
import kotlinx.coroutines.delay
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AppointmentRepository(context: Context) {

    private val database = MediTurnDatabase.getDatabase(context)
    private val appointmentDao = database.appointmentDao()
    private val doctorDao = database.doctorDao()
    private val timeSlotDao = database.timeSlotDao()

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
            delay(800)

            // Verificar disponibilidad
            val dateTimeStr = dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            val isBooked = appointmentDao.isTimeSlotBooked(doctor.id, dateTimeStr) > 0

            if (isBooked) {
                return Result.failure(Exception("Horario no disponible"))
            }

            // Crear cita
            val newAppointment = Appointment(
                id = 0,
                patientId = patientId,
                doctor = doctor,
                dateTime = dateTime,
                reason = reason,
                isTelemedicine = isTelemedicine,
                status = AppointmentStatus.PENDING,
                createdAt = LocalDateTime.now()
            )

            val appointmentId = appointmentDao.insertAppointment(newAppointment.toEntity())

            // Marcar slot como no disponible
            val slots = timeSlotDao.getTimeSlotsByDoctor(doctor.id)
            slots.find { it.dateTime == dateTimeStr }?.let { slot ->
                timeSlotDao.markTimeSlotAsUnavailable(slot.id)
            }

            Result.success(newAppointment.copy(id = appointmentId.toInt()))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Obtener todas las citas del paciente
     */
    suspend fun getPatientAppointments(patientId: Int): Result<List<Appointment>> {
        return try {
            delay(500)
            val entities = appointmentDao.getAppointmentsByPatient(patientId)
            val appointments = entities.map { entity ->
                val doctor = doctorDao.getDoctorById(entity.doctorId)
                val slots = timeSlotDao.getAvailableTimeSlots(entity.doctorId)
                    .map { it.toModel() }
                entity.toModel(doctor!!.toModel(slots))
            }
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
            delay(400)
            val currentDateTime = LocalDateTime.now()
                .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)

            val entities = appointmentDao.getUpcomingAppointments(patientId, currentDateTime)
            val appointments = entities.map { entity ->
                val doctor = doctorDao.getDoctorById(entity.doctorId)
                val slots = timeSlotDao.getAvailableTimeSlots(entity.doctorId)
                    .map { it.toModel() }
                entity.toModel(doctor!!.toModel(slots))
            }
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
            delay(400)
            val currentDateTime = LocalDateTime.now()
                .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)

            val entities = appointmentDao.getPastAppointments(patientId, currentDateTime)
            val appointments = entities.map { entity ->
                val doctor = doctorDao.getDoctorById(entity.doctorId)
                val slots = timeSlotDao.getAvailableTimeSlots(entity.doctorId)
                    .map { it.toModel() }
                entity.toModel(doctor!!.toModel(slots))
            }
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
            val entity = appointmentDao.getAppointmentById(appointmentId)
            if (entity != null) {
                val doctor = doctorDao.getDoctorById(entity.doctorId)
                val slots = timeSlotDao.getAvailableTimeSlots(entity.doctorId)
                    .map { it.toModel() }
                Result.success(entity.toModel(doctor!!.toModel(slots)))
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
            delay(600)

            val appointmentEntity = appointmentDao.getAppointmentById(appointmentId)
                ?: return Result.failure(Exception("Cita no encontrada"))

            val dateTimeStr = newDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            val isBooked = appointmentDao.isTimeSlotBooked(appointmentEntity.doctorId, dateTimeStr) > 0

            if (isBooked) {
                return Result.failure(Exception("Horario no disponible"))
            }

            // Liberar slot anterior
            val oldSlots = timeSlotDao.getTimeSlotsByDoctor(appointmentEntity.doctorId)
            oldSlots.find { it.dateTime == appointmentEntity.dateTime }?.let { slot ->
                timeSlotDao.markTimeSlotAsAvailable(slot.id)
            }

            // Marcar nuevo slot como ocupado
            oldSlots.find { it.dateTime == dateTimeStr }?.let { slot ->
                timeSlotDao.markTimeSlotAsUnavailable(slot.id)
            }

            // Actualizar cita
            appointmentDao.rescheduleAppointment(appointmentId, dateTimeStr)

            val updatedEntity = appointmentDao.getAppointmentById(appointmentId)!!
            val doctor = doctorDao.getDoctorById(updatedEntity.doctorId)
            val slots = timeSlotDao.getAvailableTimeSlots(updatedEntity.doctorId)
                .map { it.toModel() }

            Result.success(updatedEntity.toModel(doctor!!.toModel(slots)))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Cancelar cita
     */
    suspend fun cancelAppointment(appointmentId: Int): Result<Appointment> {
        return try {
            delay(500)

            val appointmentEntity = appointmentDao.getAppointmentById(appointmentId)
                ?: return Result.failure(Exception("Cita no encontrada"))

            // Liberar slot
            val slots = timeSlotDao.getTimeSlotsByDoctor(appointmentEntity.doctorId)
            slots.find { it.dateTime == appointmentEntity.dateTime }?.let { slot ->
                timeSlotDao.markTimeSlotAsAvailable(slot.id)
            }

            // Actualizar estado
            appointmentDao.updateAppointmentStatus(appointmentId, AppointmentStatus.CANCELLED.name)

            val updatedEntity = appointmentDao.getAppointmentById(appointmentId)!!
            val doctor = doctorDao.getDoctorById(updatedEntity.doctorId)
            val docSlots = timeSlotDao.getAvailableTimeSlots(updatedEntity.doctorId)
                .map { it.toModel() }

            Result.success(updatedEntity.toModel(doctor!!.toModel(docSlots)))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Confirmar cita
     */
    suspend fun confirmAppointment(appointmentId: Int): Result<Appointment> {
        return try {
            delay(400)
            appointmentDao.updateAppointmentStatus(appointmentId, AppointmentStatus.CONFIRMED.name)

            val entity = appointmentDao.getAppointmentById(appointmentId)!!
            val doctor = doctorDao.getDoctorById(entity.doctorId)
            val slots = timeSlotDao.getAvailableTimeSlots(entity.doctorId)
                .map { it.toModel() }

            Result.success(entity.toModel(doctor!!.toModel(slots)))
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
            appointmentDao.updateAppointmentStatus(appointmentId, AppointmentStatus.COMPLETED.name)

            val entity = appointmentDao.getAppointmentById(appointmentId)!!
            val doctor = doctorDao.getDoctorById(entity.doctorId)
            val slots = timeSlotDao.getAvailableTimeSlots(entity.doctorId)
                .map { it.toModel() }

            Result.success(entity.toModel(doctor!!.toModel(slots)))
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