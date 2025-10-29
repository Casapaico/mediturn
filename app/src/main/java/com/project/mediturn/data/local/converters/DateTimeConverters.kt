package com.project.mediturn.data.local.converters

import androidx.room.TypeConverter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Converters para tipos de datos complejos en Room
 */
class DateTimeConverters {

    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    /**
     * Convertir LocalDateTime a String para guardar en BD
     */
    @TypeConverter
    fun fromLocalDateTime(dateTime: LocalDateTime?): String? {
        return dateTime?.format(formatter)
    }

    /**
     * Convertir String de BD a LocalDateTime
     */
    @TypeConverter
    fun toLocalDateTime(dateTimeString: String?): LocalDateTime? {
        return dateTimeString?.let {
            LocalDateTime.parse(it, formatter)
        }
    }

    /**
     * Convertir List<String> a String (para especialidades, etc.)
     */
    @TypeConverter
    fun fromStringList(value: List<String>?): String? {
        return value?.joinToString(",")
    }

    /**
     * Convertir String a List<String>
     */
    @TypeConverter
    fun toStringList(value: String?): List<String>? {
        return value?.split(",")?.filter { it.isNotEmpty() }
    }
}
