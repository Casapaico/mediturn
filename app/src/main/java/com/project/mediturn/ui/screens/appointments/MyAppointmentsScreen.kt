package com.project.mediturn.ui.screens.appointments

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.project.mediturn.data.DataSource
import com.project.mediturn.ui.components.AppointmentCard
import com.project.mediturn.ui.components.EmptyState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyAppointmentsScreen(
    onAppointmentClick: (Int) -> Unit = {},
    onNavigateBack: () -> Unit = {}
) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Próximas", "Pasadas")
    
    val upcomingAppointments = remember { DataSource.getUpcomingAppointments() }
    val pastAppointments = remember { DataSource.getPastAppointments() }

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
            // Tabs para próximas y pasadas
            TabRow(
                selectedTabIndex = selectedTab,
                modifier = Modifier.fillMaxWidth()
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = {
                            Text(
                                text = title,
                                fontWeight = if (selectedTab == index) {
                                    FontWeight.Bold
                                } else {
                                    FontWeight.Normal
                                }
                            )
                        }
                    )
                }
            }

            // Contenido según tab seleccionada
            when (selectedTab) {
                0 -> {
                    // Citas próximas
                    if (upcomingAppointments.isEmpty()) {
                        EmptyState(
                            icon = "📅",
                            title = "No tienes citas próximas",
                            message = "Agenda una cita con tu médico de confianza",
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(upcomingAppointments) { appointment ->
                                AppointmentCard(
                                    appointment = appointment,
                                    onClick = { onAppointmentClick(appointment.id) }
                                )
                            }
                        }
                    }
                }
                1 -> {
                    // Citas pasadas
                    if (pastAppointments.isEmpty()) {
                        EmptyState(
                            icon = "📋",
                            title = "No tienes historial de citas",
                            message = "Aquí aparecerán tus citas completadas",
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(pastAppointments) { appointment ->
                                AppointmentCard(
                                    appointment = appointment,
                                    onClick = { onAppointmentClick(appointment.id) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}