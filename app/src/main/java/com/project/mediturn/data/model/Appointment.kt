package com.project.mediturn.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "appointment")
data class Appointment(
    @PrimaryKey val id: Int,
    val patientId: Int,
    val doctorId: Int,
    val dateTime: Date,
    val reason: String,
    val isTelemedicine: Boolean,
    val status: AppointmentStatus,
    val createdAt: Date
)

enum class AppointmentStatus {
    PENDING,
    CONFIRMED,
    COMPLETED,
    CANCELLED
}