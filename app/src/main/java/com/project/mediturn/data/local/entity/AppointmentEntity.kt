package com.project.mediturn.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Entidad de Appointment en Room
 */
@Entity(
    tableName = "appointments",
    foreignKeys = [
        ForeignKey(
            entity = PatientEntity::class,
            parentColumns = ["id"],
            childColumns = ["patientId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = DoctorEntity::class,
            parentColumns = ["id"],
            childColumns = ["doctorId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("patientId"),
        Index("doctorId"),
        Index("dateTime")
    ]
)
data class AppointmentEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    
    val patientId: Int, // Foreign Key
    val doctorId: Int, // Foreign Key
    val dateTime: String, // ISO String LocalDateTime
    val reason: String,
    val isTelemedicine: Boolean,
    val status: String, // PENDING, CONFIRMED, COMPLETED, CANCELLED
    val createdAt: String // ISO String LocalDateTime
)
