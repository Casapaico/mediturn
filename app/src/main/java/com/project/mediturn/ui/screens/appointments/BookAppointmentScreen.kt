package com.project.mediturn.ui.screens.appointments

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.project.mediturn.data.DataSource
import com.project.mediturn.data.model.TimeSlot
import com.project.mediturn.ui.components.TimeSlotButton
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookAppointmentScreen(
    doctorId: Int,
    onNavigateBack: () -> Unit = {},
    onAppointmentBooked: () -> Unit = {}
) {
    val doctor = remember { DataSource.getDoctorById(doctorId) }
    val scrollState = rememberScrollState()

    // Estados del formulario
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
    var selectedTimeSlot by remember { mutableStateOf<TimeSlot?>(null) }
    var reason by remember { mutableStateOf("") }
    var isTelemedicine by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }

    // Validación
    val isFormValid = selectedDate != null && 
                      selectedTimeSlot != null && 
                      reason.isNotBlank()

    if (doctor == null) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Error") },
                    navigationIcon = {
                        IconButton(onClick = onNavigateBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver")
                        }
                    }
                )
            }
        ) { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("Médico no encontrado")
            }
        }
        return
    }

    // Filtrar slots disponibles por fecha seleccionada
    val availableSlots = remember(selectedDate) {
        if (selectedDate != null) {
            doctor.availableSlots.filter {
                it.dateTime.toLocalDate() == selectedDate && it.isAvailable
            }
        } else {
            emptyList()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Agendar Cita",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        bottomBar = {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shadowElevation = 8.dp,
                color = MaterialTheme.colorScheme.surface
            ) {
                Button(
                    onClick = onAppointmentBooked,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .height(56.dp),
                    enabled = isFormValid
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(
                        text = "Confirmar Cita",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(scrollState)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Información del médico
            DoctorInfoCard(doctor = doctor)

            HorizontalDivider()

            // Selector de fecha
            DateSelectorSection(
                selectedDate = selectedDate,
                onDateSelected = { 
                    selectedDate = it
                    selectedTimeSlot = null // Reset time slot al cambiar fecha
                }
            )

            // Selector de hora (solo si hay fecha seleccionada)
            if (selectedDate != null) {
                TimeSelectorSection(
                    availableSlots = availableSlots,
                    selectedTimeSlot = selectedTimeSlot,
                    onTimeSlotSelected = { selectedTimeSlot = it }
                )
            }

            HorizontalDivider()

            // Motivo de la consulta
            ReasonSection(
                reason = reason,
                onReasonChange = { reason = it }
            )

            // Tipo de consulta (si el médico ofrece teleconsulta)
            if (doctor.availableForTeleconsultation) {
                ConsultationTypeSection(
                    isTelemedicine = isTelemedicine,
                    onTelemedicineChange = { isTelemedicine = it }
                )
            }

            // Resumen
            if (isFormValid) {
                SummaryCard(
                    doctor = doctor,
                    date = selectedDate!!,
                    timeSlot = selectedTimeSlot!!,
                    reason = reason,
                    isTelemedicine = isTelemedicine
                )
            }
        }
    }
}

@Composable
private fun DoctorInfoCard(doctor: com.project.mediturn.data.model.Doctor) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = doctor.photoUrl,
            contentDescription = "Foto de ${doctor.name}",
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = doctor.name,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = doctor.specialty,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "S/ ${doctor.consultationPrice.toInt()}",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}

@Composable
private fun DateSelectorSection(
    selectedDate: LocalDate?,
    onDateSelected: (LocalDate) -> Unit
) {
    val nextSevenDays = remember {
        (0..6).map { LocalDate.now().plusDays(it.toLong()) }
    }

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.CalendarMonth,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = "Selecciona la Fecha",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            nextSevenDays.forEach { date ->
                DateButton(
                    date = date,
                    isSelected = selectedDate == date,
                    onClick = { onDateSelected(date) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun DateButton(
    date: LocalDate,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dayName = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale("es", "ES"))
    val dayNumber = date.dayOfMonth

    OutlinedCard(
        onClick = onClick,
        modifier = modifier.height(80.dp),
        colors = CardDefaults.outlinedCardColors(
            containerColor = if (isSelected) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.surface
            }
        ),
        border = CardDefaults.outlinedCardBorder().copy(
            width = if (isSelected) 2.dp else 1.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = dayName.uppercase(),
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = if (isSelected) {
                    MaterialTheme.colorScheme.onPrimaryContainer
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                }
            )
            Text(
                text = dayNumber.toString(),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = if (isSelected) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.onSurface
                }
            )
        }
    }
}

@Composable
private fun TimeSelectorSection(
    availableSlots: List<TimeSlot>,
    selectedTimeSlot: TimeSlot?,
    onTimeSlotSelected: (TimeSlot) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.AccessTime,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = "Selecciona la Hora",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        if (availableSlots.isEmpty()) {
            Text(
                text = "No hay horarios disponibles para esta fecha",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.height(200.dp)
            ) {
                items(availableSlots) { slot ->
                    TimeSlotButton(
                        timeSlot = slot,
                        isSelected = selectedTimeSlot?.id == slot.id,
                        onClick = { onTimeSlotSelected(slot) }
                    )
                }
            }
        }
    }
}

@Composable
private fun ReasonSection(
    reason: String,
    onReasonChange: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Description,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = "Motivo de la Consulta",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        OutlinedTextField(
            value = reason,
            onValueChange = onReasonChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Describe brevemente el motivo de tu consulta") },
            minLines = 3,
            maxLines = 5,
            supportingText = {
                Text("${reason.length}/200 caracteres")
            }
        )
    }
}

@Composable
private fun ConsultationTypeSection(
    isTelemedicine: Boolean,
    onTelemedicineChange: (Boolean) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = "Tipo de Consulta",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            FilterChip(
                selected = !isTelemedicine,
                onClick = { onTelemedicineChange(false) },
                label = { Text("Presencial") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.LocalHospital,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                },
                modifier = Modifier.weight(1f)
            )

            FilterChip(
                selected = isTelemedicine,
                onClick = { onTelemedicineChange(true) },
                label = { Text("Teleconsulta") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Videocam,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun SummaryCard(
    doctor: com.project.mediturn.data.model.Doctor,
    date: LocalDate,
    timeSlot: TimeSlot,
    reason: String,
    isTelemedicine: Boolean
) {
    val dateFormatter = DateTimeFormatter.ofPattern("EEEE, dd 'de' MMMM", Locale("es", "ES"))
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Resumen de la Cita",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )

            HorizontalDivider()

            SummaryRow(Icons.Default.Person, "Médico", doctor.name)
            SummaryRow(Icons.Default.CalendarMonth, "Fecha", date.format(dateFormatter))
            SummaryRow(Icons.Default.AccessTime, "Hora", timeSlot.dateTime.format(timeFormatter))
            SummaryRow(
                Icons.Default.MedicalServices,
                "Tipo",
                if (isTelemedicine) "Teleconsulta" else "Presencial"
            )
            SummaryRow(Icons.Default.Payments, "Costo", "S/ ${doctor.consultationPrice.toInt()}")
        }
    }
}

@Composable
private fun SummaryRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.size(20.dp)
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f)
            )
            Text(
                text = value,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
    }
}