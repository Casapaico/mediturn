package com.project.mediturn.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Entidad de TimeSlot en Room
 */
@Entity(
    tableName = "time_slots",
    foreignKeys = [
        ForeignKey(
            entity = DoctorEntity::class,
            parentColumns = ["id"],
            childColumns = ["doctorId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("doctorId"), Index("dateTime")]
)
data class TimeSlotEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    
    val doctorId: Int, // Foreign Key
    val dateTime: String, // ISO String LocalDateTime
    val isAvailable: Boolean
)
