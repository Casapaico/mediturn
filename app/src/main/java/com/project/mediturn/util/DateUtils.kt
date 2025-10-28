package com.project.mediturn.util

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.time.temporal.ChronoUnit
import java.util.Locale

object DateUtils {

    private val locale = Locale("es", "ES")

    // ========== FORMATTERS ==========

    val fullDateFormatter: DateTimeFormatter = 
        DateTimeFormatter.ofPattern(Constants.DATE_FORMAT_FULL, locale)
    
    val shortDateFormatter: DateTimeFormatter = 
        DateTimeFormatter.ofPattern(Constants.DATE_FORMAT_SHORT, locale)
    
    val timeFormatter: DateTimeFormatter = 
        DateTimeFormatter.ofPattern(Constants.TIME_FORMAT, locale)
    
    val dateTimeFormatter: DateTimeFormatter = 
        DateTimeFormatter.ofPattern(Constants.DATETIME_FORMAT, locale)

    // ========== FORMATO ==========

    /**
     * Formatear fecha: "25 Oct 2024"
     */
    fun formatFullDate(date: LocalDate): String {
        return date.format(fullDateFormatter).capitalize()
    }

    fun formatFullDate(dateTime: LocalDateTime): String {
        return dateTime.format(fullDateFormatter).capitalize()
    }

    /**
     * Formatear fecha corta: "25/10/2024"
     */
    fun formatShortDate(date: LocalDate): String {
        return date.format(shortDateFormatter)
    }

    fun formatShortDate(dateTime: LocalDateTime): String {
        return dateTime.format(shortDateFormatter)
    }

    /**
     * Formatear hora: "10:30"
     */
    fun formatTime(time: LocalTime): String {
        return time.format(timeFormatter)
    }

    fun formatTime(dateTime: LocalDateTime): String {
        return dateTime.format(timeFormatter)
    }

    /**
     * Formatear fecha y hora: "25/10/2024 10:30"
     */
    fun formatDateTime(dateTime: LocalDateTime): String {
        return dateTime.format(dateTimeFormatter)
    }

    /**
     * Formatear día de la semana: "Lunes"
     */
    fun formatDayOfWeek(date: LocalDate): String {
        return date.dayOfWeek
            .getDisplayName(TextStyle.FULL, locale)
            .capitalize()
    }

    /**
     * Formatear día de la semana corto: "Lun"
     */
    fun formatShortDayOfWeek(date: LocalDate): String {
        return date.dayOfWeek
            .getDisplayName(TextStyle.SHORT, locale)
            .capitalize()
    }

    /**
     * Formatear con relativo: "Hoy", "Mañana", o "Lunes 25"
     */
    fun formatRelativeDate(date: LocalDate): String {
        val today = LocalDate.now()
        val tomorrow = today.plusDays(1)

        return when (date) {
            today -> "Hoy"
            tomorrow -> "Mañana"
            else -> "${formatDayOfWeek(date)} ${date.dayOfMonth}"
        }
    }

    // ========== VALIDACIONES ==========

    /**
     * Verificar si una fecha está en el pasado
     */
    fun isPastDate(date: LocalDate): Boolean {
        return date.isBefore(LocalDate.now())
    }

    fun isPastDateTime(dateTime: LocalDateTime): Boolean {
        return dateTime.isBefore(LocalDateTime.now())
    }

    /**
     * Verificar si una fecha está dentro del rango permitido para reservas
     */
    fun isWithinBookingRange(date: LocalDate): Boolean {
        val today = LocalDate.now()
        val maxDate = today.plusDays(Constants.DAYS_AVAILABLE_FOR_BOOKING.toLong())
        return !date.isBefore(today) && !date.isAfter(maxDate)
    }

    /**
     * Verificar si cumple con el tiempo mínimo de anticipación
     */
    fun hasMinimumAdvance(dateTime: LocalDateTime): Boolean {
        val now = LocalDateTime.now()
        val hoursDifference = ChronoUnit.HOURS.between(now, dateTime)
        return hoursDifference >= Constants.MIN_ADVANCE_HOURS
    }

    /**
     * Verificar si un horario está en horario laboral
     */
    fun isWorkingHour(time: LocalTime): Boolean {
        val hour = time.hour
        return (hour >= Constants.MORNING_START_HOUR && hour < Constants.MORNING_END_HOUR) ||
               (hour >= Constants.AFTERNOON_START_HOUR && hour < Constants.AFTERNOON_END_HOUR)
    }

    // ========== GENERACIÓN ==========

    /**
     * Generar lista de fechas para los próximos N días
     */
    fun generateNextDays(days: Int = Constants.DAYS_AVAILABLE_FOR_BOOKING): List<LocalDate> {
        val today = LocalDate.now()
        return (0 until days).map { today.plusDays(it.toLong()) }
    }

    /**
     * Generar slots de horario para un día específico
     */
    fun generateTimeSlots(date: LocalDate): List<LocalDateTime> {
        val slots = mutableListOf<LocalDateTime>()
        
        // Slots de mañana
        var currentTime = LocalTime.of(Constants.MORNING_START_HOUR, 0)
        val morningEnd = LocalTime.of(Constants.MORNING_END_HOUR, 0)
        
        while (currentTime.isBefore(morningEnd)) {
            slots.add(LocalDateTime.of(date, currentTime))
            currentTime = currentTime.plusMinutes(Constants.SLOT_DURATION_MINUTES.toLong())
        }
        
        // Slots de tarde
        currentTime = LocalTime.of(Constants.AFTERNOON_START_HOUR, 0)
        val afternoonEnd = LocalTime.of(Constants.AFTERNOON_END_HOUR, 0)
        
        while (currentTime.isBefore(afternoonEnd)) {
            slots.add(LocalDateTime.of(date, currentTime))
            currentTime = currentTime.plusMinutes(Constants.SLOT_DURATION_MINUTES.toLong())
        }
        
        return slots
    }

    // ========== UTILIDADES ==========

    /**
     * Calcular tiempo restante hasta una cita
     */
    fun getTimeUntilAppointment(dateTime: LocalDateTime): String {
        val now = LocalDateTime.now()
        val days = ChronoUnit.DAYS.between(now, dateTime)
        val hours = ChronoUnit.HOURS.between(now, dateTime) % 24
        
        return when {
            days > 1 -> "En $days días"
            days == 1L -> "Mañana"
            hours > 1 -> "En $hours horas"
            hours == 1L -> "En 1 hora"
            else -> "Muy pronto"
        }
    }

    /**
     * Verificar si dos fechas son el mismo día
     */
    fun isSameDay(date1: LocalDate, date2: LocalDate): Boolean {
        return date1.isEqual(date2)
    }

    /**
     * Obtener el inicio del día
     */
    fun getStartOfDay(date: LocalDate): LocalDateTime {
        return date.atStartOfDay()
    }

    /**
     * Obtener el fin del día
     */
    fun getEndOfDay(date: LocalDate): LocalDateTime {
        return date.atTime(23, 59, 59)
    }

    // ========== EXTENSIONES ==========

    /**
     * Extensión para capitalizar strings
     */
    private fun String.capitalize(): String {
        return this.replaceFirstChar { 
            if (it.isLowerCase()) it.titlecase(locale) else it.toString() 
        }
    }
}