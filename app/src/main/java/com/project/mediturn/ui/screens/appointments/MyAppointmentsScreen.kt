package com.project.mediturn.ui.screens.appointments

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.project.mediturn.data.DataSource
import com.project.mediturn.data.model.Appointment
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyAppointmentsScreen(
    onAppointmentClick: (Int) -> Unit = {},
    onNavigateBack: () -> Unit = {}
) {
    var currentMonth by remember { mutableStateOf(YearMonth.now()) }
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Calendario", "Historial")

    val allAppointments = DataSource.appointments

    // Contar citas por fecha
    val appointmentsByDate = allAppointments.groupBy {
        it.dateTime.toLocalDate()
    }

    // Citas del dÃ­a seleccionado
    val selectedDateAppointments = selectedDate?.let { date ->
        appointmentsByDate[date] ?: emptyList()
    } ?: emptyList()

    // Citas futuras y pasadas
    val upcomingAppointments = DataSource.getUpcomingAppointments()
    val pastAppointments = DataSource.getPastAppointments()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Mis Citas",
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
        ) {
            // Tabs
            TabRow(selectedTabIndex = selectedTab) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = {
                            Text(
                                text = title,
                                fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    )
                }
            }

            when (selectedTab) {
                0 -> {
                    // Vista de Calendario
                    CalendarView(
                        currentMonth = currentMonth,
                        onMonthChange = { currentMonth = it },
                        appointmentsByDate = appointmentsByDate,
                        selectedDate = selectedDate,
                        onDateSelected = { selectedDate = it },
                        selectedDateAppointments = selectedDateAppointments,
                        onAppointmentClick = onAppointmentClick
                    )
                }
                1 -> {
                    // Vista de Historial
                    HistoryView(
                        upcomingAppointments = upcomingAppointments,
                        pastAppointments = pastAppointments,
                        onAppointmentClick = onAppointmentClick
                    )
                }
            }
        }
    }
}

@Composable
private fun CalendarView(
    currentMonth: YearMonth,
    onMonthChange: (YearMonth) -> Unit,
    appointmentsByDate: Map<LocalDate, List<Appointment>>,
    selectedDate: LocalDate?,
    onDateSelected: (LocalDate) -> Unit,
    selectedDateAppointments: List<Appointment>,
    onAppointmentClick: (Int) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // NavegaciÃ³n de mes
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { onMonthChange(currentMonth.minusMonths(1)) }) {
                Icon(Icons.Default.ChevronLeft, "Mes anterior")
            }

            Text(
                text = "${currentMonth.month.getDisplayName(TextStyle.FULL, Locale("es", "ES")).capitalize()} ${currentMonth.year}",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            IconButton(onClick = { onMonthChange(currentMonth.plusMonths(1)) }) {
                Icon(Icons.Default.ChevronRight, "Mes siguiente")
            }
        }

        // DÃ­as de la semana
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            listOf("L", "M", "X", "J", "V", "S", "D").forEach { day ->
                Text(
                    text = day,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Calendario
        CalendarGrid(
            currentMonth = currentMonth,
            appointmentsByDate = appointmentsByDate,
            selectedDate = selectedDate,
            onDateSelected = onDateSelected
        )

        HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

        // Citas del dÃ­a seleccionado
        if (selectedDate != null) {
            Text(
                text = "Citas del ${selectedDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))}:",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (selectedDateAppointments.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("ðŸ“…", fontSize = 48.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("No hay citas para este dÃ­a")
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(selectedDateAppointments) { appointment ->
                        AppointmentListItem(
                            appointment = appointment,
                            onClick = { onAppointmentClick(appointment.id) }
                        )
                    }
                }
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("ðŸ‘†", fontSize = 48.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Selecciona un dÃ­a del calendario",
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
private fun CalendarGrid(
    currentMonth: YearMonth,
    appointmentsByDate: Map<LocalDate, List<Appointment>>,
    selectedDate: LocalDate?,
    onDateSelected: (LocalDate) -> Unit
) {
    val firstDayOfMonth = currentMonth.atDay(1)
    val lastDayOfMonth = currentMonth.atEndOfMonth()
    val firstDayOfWeek = firstDayOfMonth.dayOfWeek.value % 7 // 0 = Lunes

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
    ) {
        var currentDate = firstDayOfMonth.minusDays(firstDayOfWeek.toLong())

        repeat(6) { // MÃ¡ximo 6 semanas
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                repeat(7) { // 7 dÃ­as
                    val date = currentDate
                    val isCurrentMonth = date.month == currentMonth.month
                    val appointmentCount = appointmentsByDate[date]?.size ?: 0
                    val isSelected = date == selectedDate
                    val isToday = date == LocalDate.now()

                    CalendarDayCell(
                        date = date,
                        isCurrentMonth = isCurrentMonth,
                        appointmentCount = appointmentCount,
                        isSelected = isSelected,
                        isToday = isToday,
                        onClick = { if (isCurrentMonth) onDateSelected(date) },
                        modifier = Modifier.weight(1f)
                    )

                    currentDate = currentDate.plusDays(1)
                }
            }

            if (currentDate.isAfter(lastDayOfMonth)) {
                return@Column
            }
        }
    }
}

@Composable
private fun CalendarDayCell(
    date: LocalDate,
    isCurrentMonth: Boolean,
    appointmentCount: Int,
    isSelected: Boolean,
    isToday: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .padding(2.dp)
            .clip(CircleShape)
            .background(
                when {
                    isSelected -> MaterialTheme.colorScheme.primaryContainer
                    isToday -> MaterialTheme.colorScheme.secondaryContainer
                    else -> MaterialTheme.colorScheme.surface
                }
            )
            .clickable(enabled = isCurrentMonth, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = date.dayOfMonth.toString(),
                fontSize = 14.sp,
                color = when {
                    !isCurrentMonth -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                    isSelected -> MaterialTheme.colorScheme.onPrimaryContainer
                    else -> MaterialTheme.colorScheme.onSurface
                },
                fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal
            )

            // Indicador de citas
            if (appointmentCount > 0 && isCurrentMonth) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(
                            when {
                                appointmentCount >= 3 -> MaterialTheme.colorScheme.error
                                appointmentCount == 2 -> MaterialTheme.colorScheme.tertiary
                                else -> MaterialTheme.colorScheme.primary
                            }
                        )
                )
            }
        }
    }
}

@Composable
private fun AppointmentListItem(
    appointment: Appointment,
    onClick: () -> Unit
) {
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.AccessTime,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = appointment.dateTime.format(timeFormatter),
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = appointment.doctor.name,
                    fontSize = 14.sp
                )
                Text(
                    text = appointment.reason,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Ver detalle"
            )
        }
    }
}

@Composable
private fun HistoryView(
    upcomingAppointments: List<Appointment>,
    pastAppointments: List<Appointment>,
    onAppointmentClick: (Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // PrÃ³ximas citas
        item {
            Text(
                text = "PrÃ³ximas Citas (${upcomingAppointments.size})",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        if (upcomingAppointments.isEmpty()) {
            item {
                Text(
                    "No tienes citas prÃ³ximas",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            items(upcomingAppointments) { appointment ->
                AppointmentListItem(
                    appointment = appointment,
                    onClick = { onAppointmentClick(appointment.id) }
                )
            }
        }

        // Historial
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Historial (${pastAppointments.size})",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        if (pastAppointments.isEmpty()) {
            item {
                Text(
                    "No tienes historial de citas",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            items(pastAppointments) { appointment ->
                AppointmentListItem(
                    appointment = appointment,
                    onClick = { onAppointmentClick(appointment.id) }
                )
            }
        }
    }
}