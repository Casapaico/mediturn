package com.project.mediturn.ui.screens.appointments

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.project.mediturn.data.DataSource
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookAppointmentScreen(
    doctorId: Int = 0, // 0 = sin médico preseleccionado
    onNavigateBack: () -> Unit = {},
    onAppointmentBooked: () -> Unit = {}
) {
    val scrollState = rememberScrollState()
    val doctors = DataSource.doctors
    val specialties = DataSource.specialties

    // Estados del formulario
    var selectedDoctor by remember {
        mutableStateOf(
            if (doctorId > 0) DataSource.getDoctorById(doctorId) else null
        )
    }
    var selectedSpecialty by remember {
        mutableStateOf(
            if (doctorId > 0) DataSource.getDoctorById(doctorId)?.specialty ?: "" else ""
        )
    }
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
    var selectedTime by remember { mutableStateOf<LocalTime?>(null) }
    var reason by remember { mutableStateOf("") }
    var isTelemedicine by remember { mutableStateOf(false) }

    // Dropdowns
    var showDoctorMenu by remember { mutableStateOf(false) }
    var showSpecialtyMenu by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    // Validación
    val isFormValid = selectedDoctor != null &&
            selectedDate != null &&
            selectedTime != null &&
            reason.isNotBlank()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Agendar Nueva Cita",
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
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(scrollState)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Título de sección
            Text(
                text = "Complete los datos de la cita",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            // 1. Especialidad (Dropdown)
            OutlinedTextField(
                value = selectedSpecialty,
                onValueChange = { },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Especialidad *") },
                placeholder = { Text("Seleccione una especialidad") },
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = { showSpecialtyMenu = !showSpecialtyMenu }) {
                        Icon(Icons.Default.ArrowDropDown, "Seleccionar")
                    }
                },
                leadingIcon = {
                    Icon(Icons.Default.MedicalServices, "Especialidad")
                }
            )

            DropdownMenu(
                expanded = showSpecialtyMenu,
                onDismissRequest = { showSpecialtyMenu = false }
            ) {
                specialties.forEach { specialty ->
                    DropdownMenuItem(
                        text = { Text("${specialty.iconUrl} ${specialty.name}") },
                        onClick = {
                            selectedSpecialty = specialty.name
                            selectedDoctor = null // Reset doctor al cambiar especialidad
                            showSpecialtyMenu = false
                        }
                    )
                }
            }

            // 2. Médico (Dropdown filtrado por especialidad)
            val filteredDoctors = if (selectedSpecialty.isNotEmpty()) {
                doctors.filter { it.specialty == selectedSpecialty }
            } else {
                doctors
            }

            OutlinedTextField(
                value = selectedDoctor?.name ?: "",
                onValueChange = { },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Médico *") },
                placeholder = { Text("Seleccione un médico") },
                readOnly = true,
                enabled = selectedSpecialty.isNotEmpty(),
                trailingIcon = {
                    IconButton(
                        onClick = { showDoctorMenu = !showDoctorMenu },
                        enabled = selectedSpecialty.isNotEmpty()
                    ) {
                        Icon(Icons.Default.ArrowDropDown, "Seleccionar")
                    }
                },
                leadingIcon = {
                    Icon(Icons.Default.Person, "Médico")
                },
                supportingText = {
                    if (selectedSpecialty.isEmpty()) {
                        Text("Primero seleccione una especialidad", fontSize = 12.sp)
                    }
                }
            )

            DropdownMenu(
                expanded = showDoctorMenu,
                onDismissRequest = { showDoctorMenu = false }
            ) {
                filteredDoctors.forEach { doctor ->
                    DropdownMenuItem(
                        text = {
                            Column {
                                Text(doctor.name, fontWeight = FontWeight.Bold)
                                Text(
                                    "S/ ${doctor.consultationPrice.toInt()}",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        },
                        onClick = {
                            selectedDoctor = doctor
                            showDoctorMenu = false
                        }
                    )
                }
            }

            HorizontalDivider()

            // 3. Fecha (DatePicker)
            OutlinedTextField(
                value = selectedDate?.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) ?: "",
                onValueChange = { },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Fecha *") },
                placeholder = { Text("Seleccione la fecha") },
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = { showDatePicker = true }) {
                        Icon(Icons.Default.CalendarMonth, "Seleccionar fecha")
                    }
                },
                leadingIcon = {
                    Icon(Icons.Default.DateRange, "Fecha")
                }
            )

            // Selector de fecha simplificado (próximos 7 días)
            if (showDatePicker) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            "Seleccione una fecha:",
                            fontWeight = FontWeight.Bold
                        )

                        (0..6).forEach { daysToAdd ->
                            val date = LocalDate.now().plusDays(daysToAdd.toLong())
                            val dayName = date.dayOfWeek.getDisplayName(TextStyle.FULL, Locale("es", "ES"))

                            OutlinedCard(
                                onClick = {
                                    selectedDate = date
                                    showDatePicker = false
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        dayName.capitalize(),
                                        fontWeight = FontWeight.Medium
                                    )
                                    Text(
                                        date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }

                        TextButton(
                            onClick = { showDatePicker = false },
                            modifier = Modifier.align(Alignment.End)
                        ) {
                            Text("Cancelar")
                        }
                    }
                }
            }

            // 4. Hora
            OutlinedTextField(
                value = selectedTime?.format(DateTimeFormatter.ofPattern("HH:mm")) ?: "",
                onValueChange = { },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Hora *") },
                placeholder = { Text("Seleccione la hora") },
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = { showTimePicker = true }) {
                        Icon(Icons.Default.AccessTime, "Seleccionar hora")
                    }
                },
                leadingIcon = {
                    Icon(Icons.Default.Schedule, "Hora")
                }
            )

            // Selector de hora simplificado
            if (showTimePicker) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            "Seleccione una hora:",
                            fontWeight = FontWeight.Bold
                        )

                        // Horarios de 9:00 a 18:00
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            (9..17).forEach { hour ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    listOf(0, 30).forEach { minute ->
                                        val time = LocalTime.of(hour, minute)
                                        OutlinedCard(
                                            onClick = {
                                                selectedTime = time
                                                showTimePicker = false
                                            },
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Text(
                                                text = time.format(DateTimeFormatter.ofPattern("HH:mm")),
                                                modifier = Modifier.padding(12.dp),
                                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        TextButton(
                            onClick = { showTimePicker = false },
                            modifier = Modifier.align(Alignment.End)
                        ) {
                            Text("Cancelar")
                        }
                    }
                }
            }

            HorizontalDivider()

            // 5. Motivo
            OutlinedTextField(
                value = reason,
                onValueChange = { reason = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Motivo de la consulta *") },
                placeholder = { Text("Describa brevemente el motivo...") },
                minLines = 3,
                maxLines = 5,
                leadingIcon = {
                    Icon(Icons.Default.Description, "Motivo")
                },
                supportingText = {
                    Text("${reason.length}/200 caracteres")
                }
            )

            // 6. Tipo de consulta
            if (selectedDoctor?.availableForTeleconsultation == true) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(Icons.Default.Videocam, "Tipo")
                    Text("Teleconsulta disponible", modifier = Modifier.weight(1f))
                    Switch(
                        checked = isTelemedicine,
                        onCheckedChange = { isTelemedicine = it }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botones
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Cancelar
                OutlinedButton(
                    onClick = onNavigateBack,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Cancelar")
                }

                // Agendar
                Button(
                    onClick = onAppointmentBooked,
                    modifier = Modifier.weight(1f),
                    enabled = isFormValid
                ) {
                    Icon(
                        Icons.Default.Check,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Agendar")
                }
            }
        }
    }
}