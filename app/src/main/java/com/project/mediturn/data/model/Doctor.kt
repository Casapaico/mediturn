package com.project.mediturn.data.model

data class Doctor(
    val id: Int,
    val name: String,
    val specialty: String,
    val department: String,
    val description: String,
    val yearsOfExperience: Int,
    val rating: Float,
    val reviewCount: Int,
    val photoUrl: String,
    val collegiateCertificate: String,
    val availableForTeleconsultation: Boolean,
    val city: String,
    val consultationPrice: Double,
    val availableSlots: List<TimeSlot> = emptyList()
)