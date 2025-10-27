package com.project.mediturn.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.project.mediturn.data.DataSource
import com.project.mediturn.util.NotificationHelper
import java.util.*

class AppointmentReminderWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            val notificationHelper = NotificationHelper(applicationContext)

            val appointments = DataSource.appointments
            val now = Date()

            appointments.forEach { appointment ->
                // Verificar si la cita está dentro de las próximas 24 horas
                val diff = appointment.dateTime.time - now.time
                val hoursDiff = diff / (1000 * 60 * 60)

                if (hoursDiff in 1..24) {
                    val doctor = DataSource.doctors.find { it.id == appointment.doctorId }
                    doctor?.let {
                        notificationHelper.sendAppointmentReminder(appointment, it.name)
                    }
                }
            }

            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}