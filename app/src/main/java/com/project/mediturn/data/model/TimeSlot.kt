package com.project.mediturn.data.model

import java.util.Date

data class TimeSlot(
    val id: Int,
    val dateTime: Date,
    val isAvailable: Boolean
)