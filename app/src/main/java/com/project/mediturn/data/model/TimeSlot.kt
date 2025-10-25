package com.project.mediturn.data.model

import java.time.LocalDateTime

data class TimeSlot(
    val id: Int,
    val dateTime: LocalDateTime,
    val isAvailable: Boolean
)