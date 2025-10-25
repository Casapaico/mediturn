package com.project.mediturn.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.project.mediturn.data.model.TimeSlot
import java.time.format.DateTimeFormatter

@Composable
fun TimeSlotButton(
    timeSlot: TimeSlot,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
    val enabled = timeSlot.isAvailable

    OutlinedButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = if (isSelected) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.surface
            },
            contentColor = if (isSelected) {
                MaterialTheme.colorScheme.onPrimaryContainer
            } else if (enabled) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
            }
        ),
        border = BorderStroke(
            width = if (isSelected) 2.dp else 1.dp,
            color = if (isSelected) {
                MaterialTheme.colorScheme.primary
            } else if (enabled) {
                MaterialTheme.colorScheme.outline
            } else {
                MaterialTheme.colorScheme.outline.copy(alpha = 0.38f)
            }
        )
    ) {
        Text(
            text = timeSlot.dateTime.format(timeFormatter),
            fontSize = 14.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 4.dp)
        )
    }
}