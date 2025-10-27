package com.project.mediturn.data.repository

import com.project.mediturn.data.DataSource
import com.project.mediturn.data.model.TimeSlot
import java.util.*
import javax.inject.Inject

class TimeSlotRepository @Inject constructor() {

    fun getAvailableTimeSlots(): List<TimeSlot> {
        return DataSource.generateAvailableTimeSlots()
    }

    fun getAvailableTimeSlotsForDate(date: Date): List<TimeSlot> {
        val calendar = Calendar.getInstance()
        calendar.time = date

        return getAvailableTimeSlots().filter { timeSlot ->
            val slotCalendar = Calendar.getInstance()
            slotCalendar.time = timeSlot.dateTime
            slotCalendar.get(Calendar.DAY_OF_YEAR) == calendar.get(Calendar.DAY_OF_YEAR) &&
                    slotCalendar.get(Calendar.YEAR) == calendar.get(Calendar.YEAR) &&
                    timeSlot.isAvailable
        }
    }

    fun getAvailableTimeSlotsForDoctor(doctorId: Int, date: Date): List<TimeSlot> {
        // En una implementación real, esto consultaría la base de datos
        // Por ahora, usamos datos simulados
        return getAvailableTimeSlotsForDate(date).take(6) // Limitar a 6 horarios para demo
    }
}