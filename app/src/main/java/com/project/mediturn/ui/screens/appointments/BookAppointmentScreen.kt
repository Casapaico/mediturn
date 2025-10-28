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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.project.mediturn.data.DataSource
import com.project.mediturn.util.Constants
import com.project.mediturn.util.DateUtils
import com.project.mediturn.util.ValidationUtils
import com.project.mediturn.viewmodel.ActionMessage
import com.project.mediturn.viewmodel.AppointmentUiState
import com.project.mediturn.viewmodel.AppointmentViewModel
import kotlinx.coroutines.flow.collectLatest
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookAppointmentScreen(
    doctorId: Int = 0,
    onNavigateBack: () -> Unit = {},
    onAppointmentBooked: () -> Unit = {},
    viewModel: AppointmentViewModel = viewModel()
) {
    val scrollState = rememberScrollState()
    val snackbarHostState = remember { SnackbarHostState() }

    // Estados del ViewModel
    val selectedDoctor by viewModel.selectedDoctor.collectAsState()
    val selectedDateTime by viewModel.selectedDateTime.collectAsState()
    val reason by viewModel.reason.collectAsState()
    val isTelemedicine by viewModel.isTelemedicine.collectAsState()
    val uiState by viewModel.uiState.collectAsState()

    // Estados locales de UI
    val doctors = DataSource.doctors
    val specialties = DataSource.specialties
    var selectedSpecialty by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
    var selectedTime by remember { mutableStateOf<LocalTime?>(null) }

    // Estados de diálogos
    var showDoctorMenu by remember { mutableStateOf(false) }
    var showSpecialtyMenu by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    var showConfirmDialog by remember { mutableStateOf(false) }
    var showCancelDialog by remember { mutableStateOf(false) }

    // Validaciones en tiempo real
    val reasonValidation = remember(reason) {
        ValidationUtils.validateReason(reason)
    }
    val dateValidation = remember(selectedDate) {
        ValidationUtils.validateDate(selectedDate)
    }

    val isFormValid = selectedDoctor != null &&
            selectedDate != null &&
            selectedTime != null &&
            reasonValidation.isValid

    // Inicializar si hay doctorId
    LaunchedEffect(doctorId) {
        if (doctorId > 0) {
            val doctor = DataSource.getDoctorById(doctorId)
            doctor?.let {
                viewModel.updateSelectedDoctor(it)
                selectedSpecialty = it.specialty
            }
        }
    }

    // Escuchar mensajes de acción
    LaunchedEffect(Unit) {
        viewModel.actionMessage.collectLatest { message ->
            when (message) {
                is ActionMessage.Success -> {
                    snackbarHostState.showSnackbar(
                        message = message.message,
                        duration = SnackbarDuration.Short
                    )
                    onAppointmentBooked()
                }
                is ActionMessage.Error -> {
                    snackbarHostState.showSnackbar(
                        message = message.message,
                        duration = SnackbarDuration.Long
                    )
                }
            }
        }
    }

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
                    IconButton(onClick = {
                        if (isFormValid) {
                            showCancelDialog = true
                        } else {
                            onNavigateBack()
                        }
                    }) {
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
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
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

                // 1. Especialidad
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
                                viewModel.updateSelectedDoctor(null)
                                showSpecialtyMenu = false
                            }
                        )
                    }
                }

                // 2. Médico
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
                                viewModel.updateSelectedDoctor(doctor)
                                showDoctorMenu = false
                            }
                        )
                    }
                }

                HorizontalDivider()

                // 3. Fecha
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
                    },
                    isError = selectedDate != null && !dateValidation.isValid,
                    supportingText = {
                        if (selectedDate != null && !dateValidation.isValid) {
                            Text(
                                text = dateValidation.errorMessage ?: "",
                                color = MaterialTheme.colorScheme.error,
                                fontSize = 12.sp
                            )
                        }
                    }
                )

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
                                            dayName.replaceFirstChar { it.titlecase() },
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
                                                    if (selectedDate != null) {
                                                        val dateTime = LocalDateTime.of(selectedDate, time)
                                                        viewModel.updateSelectedDateTime(dateTime)
                                                    }
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
                    onValueChange = { viewModel.updateReason(it) },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Motivo de la consulta *") },
                    placeholder = { Text("Describa brevemente el motivo...") },
                    minLines = 3,
                    maxLines = 5,
                    leadingIcon = {
                        Icon(Icons.Default.Description, "Motivo")
                    },
                    isError = reason.isNotEmpty() && !reasonValidation.isValid,
                    supportingText = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            if (reason.isNotEmpty() && !reasonValidation.isValid) {
                                Text(
                                    text = reasonValidation.errorMessage ?: "",
                                    color = MaterialTheme.colorScheme.error,
                                    fontSize = 12.sp
                                )
                            } else {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                            Text(
                                "${reason.length}/${Constants.MAX_REASON_LENGTH}",
                                fontSize = 12.sp
                            )
                        }
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
                            onCheckedChange = { viewModel.toggleTelemedicine(it) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Botones
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            if (isFormValid) {
                                showCancelDialog = true
                            } else {
                                onNavigateBack()
                            }
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cancelar")
                    }

                    Button(
                        onClick = { showConfirmDialog = true },
                        modifier = Modifier.weight(1f),
                        enabled = isFormValid && uiState !is AppointmentUiState.Processing
                    ) {
                        if (uiState is AppointmentUiState.Processing) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                strokeWidth = 2.dp,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        } else {
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

            // Loading overlay
            if (uiState is AppointmentUiState.Processing) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            CircularProgressIndicator()
                            Text("Agendando cita...")
                        }
                    }
                }
            }
        }
    }

    // Diálogo de confirmación para agendar
    if (showConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            icon = {
                Icon(Icons.Default.CalendarMonth, contentDescription = null)
            },
            title = {
                Text("Confirmar Cita")
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("¿Desea agendar esta cita?", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Médico: ${selectedDoctor?.name}")
                    Text("Fecha: ${selectedDate?.let { DateUtils.formatFullDate(it) }}")
                    Text("Hora: ${selectedTime?.let { DateUtils.formatTime(it) }}")
                    Text("Tipo: ${if (isTelemedicine) "Teleconsulta" else "Presencial"}")
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showConfirmDialog = false
                        viewModel.createAppointment()
                    }
                ) {
                    Text("Confirmar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showConfirmDialog = false }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }

    // Diálogo de confirmación para cancelar
    if (showCancelDialog) {
        AlertDialog(
            onDismissRequest = { showCancelDialog = false },
            icon = {
                Icon(
                    Icons.Default.Warning,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error
                )
            },
            title = {
                Text("Descartar Cambios")
            },
            text = {
                Text("¿Está seguro de salir? Los datos ingresados se perderán.")
            },
            confirmButton = {
                Button(
                    onClick = {
                        showCancelDialog = false
                        viewModel.clearForm()
                        onNavigateBack()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Salir")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showCancelDialog = false }
                ) {
                    Text("Continuar editando")
                }
            }
        )
    }
}