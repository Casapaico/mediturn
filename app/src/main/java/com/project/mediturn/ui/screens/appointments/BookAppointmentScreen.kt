package com.project.mediturn.ui.screens.appointments

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.project.mediturn.data.DataSource
import com.project.mediturn.ui.components.TimeSlotButton
import java.util.Calendar
import java.util.Date

@Composable
fun BookAppointmentScreen(doctorId: Int) {
    val doctor = DataSource.doctors.find { it.id == doctorId } ?: return

    var selectedDate by remember { mutableStateOf<Date?>(null) }
    var selectedTimeSlot by remember { mutableStateOf<Date?>(null) }
    var reason by remember { mutableStateOf("") }
    var isTelemedicine by remember { mutableStateOf(false) }

    val calendar = Calendar.getInstance()
    val today = calendar.time

    // Generar próximos 7 días
    val availableDates = List(7) { index ->
        calendar.time = today
        calendar.add(Calendar.DAY_OF_YEAR, index)
        calendar.time
    }

    // Horarios de ejemplo
    val timeSlots = List(6) { index ->
        calendar.time = today
        calendar.set(Calendar.HOUR_OF_DAY, 9 + index)
        calendar.set(Calendar.MINUTE, 0)
        calendar.time
    }

    val isFormValid = selectedDate != null &&
            selectedTimeSlot != null &&
            reason.isNotBlank()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Agendar Cita",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Información del doctor
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = doctor.photoUrl,
                    contentDescription = "Foto de ${doctor.name}",
                    modifier = Modifier
                        .size(60.dp)
                        .padding(end = 16.dp)
                )
                Column {
                    Text(
                        text = doctor.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = doctor.specialty,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "S/. %.0f".format(
                            if (isTelemedicine) doctor.consultationPrice * 0.8
                            else doctor.consultationPrice
                        ),
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Selector de fecha
        Text(
            text = "Seleccionar Fecha",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            availableDates.forEach { date ->
                val calendarDate = Calendar.getInstance().apply { time = date }
                val isSelected = selectedDate == date

                Button(
                    onClick = { selectedDate = date },
                    modifier = Modifier.weight(1f),
                    colors = if (isSelected) {
                        ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    } else {
                        ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    }
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = calendarDate.get(Calendar.DAY_OF_MONTH).toString(),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = when (calendarDate.get(Calendar.DAY_OF_WEEK)) {
                                Calendar.MONDAY -> "Lun"
                                Calendar.TUESDAY -> "Mar"
                                Calendar.WEDNESDAY -> "Mié"
                                Calendar.THURSDAY -> "Jue"
                                Calendar.FRIDAY -> "Vie"
                                Calendar.SATURDAY -> "Sáb"
                                else -> "Dom"
                            },
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            }
        }

        // Selector de hora
        if (selectedDate != null) {
            Text(
                text = "Seleccionar Hora",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                timeSlots.chunked(3).forEach { chunk ->
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        chunk.forEach { timeSlot ->
                            TimeSlotButton(
                                dateTime = timeSlot,
                                isAvailable = true,
                                isSelected = selectedTimeSlot == timeSlot,
                                onTimeSlotClick = { selectedTimeSlot = it }
                            )
                        }
                    }
                }
            }
        }

        // Tipo de consulta
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Consulta Virtual",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "20% de descuento",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
                Switch(
                    checked = isTelemedicine,
                    onCheckedChange = { isTelemedicine = it }
                )
            }
        }

        // Motivo de la consulta
        Text(
            text = "Motivo de la consulta",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            BasicTextField(
                value = reason,
                onValueChange = { reason = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(100.dp),
                decorationBox = { innerTextField ->
                    if (reason.isEmpty()) {
                        Text(
                            text = "Describe el motivo de tu consulta...",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    innerTextField()
                }
            )
        }

        // Resumen
        if (isFormValid) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Resumen de la cita",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = "Doctor: ${doctor.name}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "Especialidad: ${doctor.specialty}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "Tipo: ${if (isTelemedicine) "Virtual" else "Presencial"}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "Precio: S/. ${"%.0f".format(
                            if (isTelemedicine) doctor.consultationPrice * 0.8
                            else doctor.consultationPrice
                        )}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Botón de confirmación
        Button(
            onClick = { /* Confirmar cita */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            enabled = isFormValid,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text(
                text = "Confirmar Cita",
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}