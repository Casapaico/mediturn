package com.project.mediturn.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "patient")
data class Patient(
    @PrimaryKey val id: Int,
    val name: String,
    val email: String,
    val phone: String,
    val dni: String
)