package com.project.mediturn.util

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.project.mediturn.work.AppointmentReminderWorker
import java.util.concurrent.TimeUnit

class ReminderScheduler(private val context: Context) {

    fun scheduleDailyReminders() {
        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .build()

        val reminderWork = PeriodicWorkRequestBuilder<AppointmentReminderWorker>(
            24, // Repeat interval
            TimeUnit.HOURS
        ).setConstraints(constraints)
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "daily_appointment_reminders",
            ExistingPeriodicWorkPolicy.KEEP,
            reminderWork
        )
    }

    fun scheduleOneTimeReminder(appointmentId: Int, delayInMinutes: Long) {
        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .build()

        val oneTimeWork = PeriodicWorkRequestBuilder<AppointmentReminderWorker>(
            delayInMinutes, TimeUnit.MINUTES
        ).setConstraints(constraints)
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "reminder_$appointmentId",
            ExistingPeriodicWorkPolicy.REPLACE,
            oneTimeWork
        )
    }

    fun cancelReminder(appointmentId: Int) {
        WorkManager.getInstance(context).cancelUniqueWork("reminder_$appointmentId")
    }
}