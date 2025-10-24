package com.project.mediturn.data.model

import java.time.LocalDateTime

data class Appointment(
    val id: Int,
    val patientId: Int,
    val doctor: Doctor,
    val dateTime: LocalDateTime,
    val reason: String,
    val isTelemedicine: Boolean,
    val status: AppointmentStatus,
    val createdAt: LocalDateTime
)

enum class AppointmentStatus {
    PENDING,
    CONFIRMED,
    COMPLETED,
    CANCELLED
}