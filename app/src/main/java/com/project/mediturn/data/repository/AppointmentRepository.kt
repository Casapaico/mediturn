package com.project.mediturn.data.repository

import com.project.mediturn.data.DataSource
import com.project.mediturn.data.model.Appointment
import com.project.mediturn.data.model.AppointmentStatus
import kotlinx.coroutines.delay
import java.util.Date

class AppointmentRepository {
    private var mockAppointments = DataSource.appointments.toMutableList()

    suspend fun getAllAppointments(): List<Appointment> {
        delay(800)
        return mockAppointments
    }

    suspend fun getUpcomingAppointments(): List<Appointment> {
        delay(600)
        val now = Date()
        return mockAppointments.filter {
            it.dateTime.after(now) && it.status != AppointmentStatus.CANCELLED
        }
    }

    suspend fun getPastAppointments(): List<Appointment> {
        delay(600)
        val now = Date()
        return mockAppointments.filter {
            it.dateTime.before(now) || it.status == AppointmentStatus.CANCELLED
        }
    }

    suspend fun bookAppointment(appointment: Appointment): Boolean {
        delay(1000)
        return try {
            mockAppointments.add(appointment)
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun cancelAppointment(appointmentId: Int): Boolean {
        delay(500)
        return try {
            val appointment = mockAppointments.find { it.id == appointmentId }
            appointment?.let {
                val index = mockAppointments.indexOf(it)
                mockAppointments[index] = it.copy(status = AppointmentStatus.CANCELLED)
            }
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun getAppointmentById(id: Int): Appointment? {
        delay(300)
        return mockAppointments.find { it.id == id }
    }
}