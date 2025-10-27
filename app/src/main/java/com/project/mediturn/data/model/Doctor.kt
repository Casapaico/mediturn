package com.project.mediturn.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "doctor")
data class Doctor(
    @PrimaryKey val id: Int,
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
    val consultationPrice: Double
)