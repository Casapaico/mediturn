package com.project.mediturn.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Entidad de Paciente en Room
 * Representa la tabla 'patients' en SQLite
 */
@Entity(
    tableName = "patients",
    indices = [
        Index(value = ["email"], unique = true),
        Index(value = ["dni"], unique = true)
    ]
)
data class PatientEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    
    val name: String,
    val email: String,
    val phone: String,
    val dni: String,
    val passwordHash: String, // Contraseña encriptada
    val createdAt: String, // ISO String LocalDateTime
    val isActive: Boolean = true
)
