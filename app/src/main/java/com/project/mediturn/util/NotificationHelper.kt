package com.project.mediturn.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.project.mediturn.R
import com.project.mediturn.data.model.Appointment
import java.text.SimpleDateFormat
import java.util.*

class NotificationHelper(private val context: Context) {
    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    companion object {
        const val CHANNEL_ID = "mediturn_reminders"
        const val CHANNEL_NAME = "Recordatorios de Citas"
    }

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Recordatorios de citas médicas"
                enableLights(true)
                enableVibration(true)
            }
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun sendAppointmentReminder(appointment: Appointment, doctorName: String) {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy 'a las' HH:mm", Locale.getDefault())

        // ✅ CORREGIDO: Usar NotificationCompat.Builder correctamente
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_medical) // Asegúrate de crear este recurso
            .setContentTitle("Recordatorio de cita médica")
            .setContentText("Tienes una cita con $doctorName el ${dateFormat.format(appointment.dateTime)}")
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("Cita con $doctorName\n" +
                            "Fecha: ${dateFormat.format(appointment.dateTime)}\n" +
                            "Motivo: ${appointment.reason ?: "Consulta médica"}")
            )
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(appointment.id, notification)
    }

    fun sendAppointmentConfirmation(doctorName: String, dateTime: Date) {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy 'a las' HH:mm", Locale.getDefault())

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_medical)
            .setContentTitle("Cita confirmada ✓")
            .setContentText("Tu cita con $doctorName ha sido confirmada")
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("Cita confirmada con $doctorName\n" +
                            "Fecha: ${dateFormat.format(dateTime)}")
            )
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
    }

    fun cancelReminder(appointmentId: Int) {
        notificationManager.cancel(appointmentId)
    }
}