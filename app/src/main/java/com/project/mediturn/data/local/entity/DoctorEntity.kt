package com.project.mediturn.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entidad de Doctor en Room
 */
@Entity(tableName = "doctors")
data class DoctorEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    
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
