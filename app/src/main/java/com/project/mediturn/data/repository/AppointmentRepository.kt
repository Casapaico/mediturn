package com.project.mediturn.data.repository

import com.project.mediturn.data.local.AppointmentDao
import com.project.mediturn.data.local.DoctorDao
import com.project.mediturn.data.model.Appointment
import com.project.mediturn.data.model.AppointmentStatus
import com.project.mediturn.data.model.AppointmentWithDoctor
import kotlinx.coroutines.flow.Flow
import java.util.Date

class AppointmentRepository(
    private val appointmentDao: AppointmentDao,
    private val doctorDao: DoctorDao
) {
    fun getAllAppointments(): Flow<List<Appointment>> {
        return appointmentDao.getAllAppointments()
    }

    fun getAppointmentsByPatient(patientId: Int): Flow<List<Appointment>> {
        return appointmentDao.getAppointmentsByPatient(patientId)
    }

    fun getAppointmentsWithDoctor(patientId: Int): Flow<List<AppointmentWithDoctor>> {
        return appointmentDao.getAppointmentsWithDoctor(patientId)
    }

    suspend fun getAppointmentById(id: Int): Appointment? {
        return appointmentDao.getAppointmentById(id)
    }

    suspend fun getAppointmentWithDoctor(id: Int): AppointmentWithDoctor? {
        val appointment = appointmentDao.getAppointmentById(id)
        return if (appointment != null) {
            val doctor = doctorDao.getDoctorById(appointment.doctorId)
            doctor?.let { AppointmentWithDoctor(appointment, it) }
        } else {
            null
        }
    }

    suspend fun bookAppointment(appointment: Appointment): Boolean {
        return try {
            appointmentDao.insertAppointment(appointment)
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun cancelAppointment(appointmentId: Int): Boolean {
        return try {
            val appointment = appointmentDao.getAppointmentById(appointmentId)
            appointment?.let {
                val updatedAppointment = it.copy(status = AppointmentStatus.CANCELLED)
                appointmentDao.updateAppointment(updatedAppointment)
            }
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun confirmAppointment(appointmentId: Int): Boolean {
        return try {
            val appointment = appointmentDao.getAppointmentById(appointmentId)
            appointment?.let {
                val updatedAppointment = it.copy(status = AppointmentStatus.CONFIRMED)
                appointmentDao.updateAppointment(updatedAppointment)
            }
            true
        } catch (e: Exception) {
            false
        }
    }

    fun getUpcomingAppointments(currentTime: Date): Flow<List<Appointment>> {
        return appointmentDao.getUpcomingAppointments(currentTime)
    }

    fun getPastAppointments(currentTime: Date): Flow<List<Appointment>> {
        return appointmentDao.getPastAppointments(currentTime)
    }
}