package com.project.mediturn.ui.screens.appointments

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.project.mediturn.ui.components.AppointmentCard
import com.project.mediturn.ui.components.EmptyState
import com.project.mediturn.viewmodel.AppointmentViewModel

@Composable
fun MyAppointmentsScreen(
    onAppointmentClick: (Int) -> Unit,
    viewModel: AppointmentViewModel = viewModel()
) {
    val state by viewModel.appointmentState.collectAsState()
    var selectedTab by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        viewModel.loadAppointments()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Mis Citas",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Tabs
        TabRow(selectedTabIndex = selectedTab) {
            Tab(
                text = { Text("Próximas (${state.upcomingAppointments.size})") },
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 }
            )
            Tab(
                text = { Text("Pasadas (${state.pastAppointments.size})") },
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 }
            )
        }

        when {
            state.isLoading -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                    Text(
                        text = "Cargando citas...",
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }
            }
            state.error != null -> {
                EmptyState(
                    emoji = "😵",
                    title = "Error al cargar",
                    description = state.error ?: "Ha ocurrido un error"
                )
            }
            else -> {
                val appointments = if (selectedTab == 0) {
                    state.upcomingAppointments
                } else {
                    state.pastAppointments
                }

                if (appointments.isEmpty()) {
                    EmptyState(
                        emoji = if (selectedTab == 0) "📅" else "✅",
                        title = if (selectedTab == 0) "No tienes citas próximas" else "No hay citas pasadas",
                        description = if (selectedTab == 0)
                            "Agenda tu primera cita médica"
                        else
                            "Tus citas completadas aparecerán aquí"
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 16.dp)
                    ) {
                        items(appointments) { appointment ->
                            AppointmentCard(
                                doctorName = appointment.doctor.name,
                                specialty = appointment.doctor.specialty,
                                dateTime = appointment.dateTime,
                                status = appointment.status,
                                photoUrl = appointment.doctor.photoUrl,
                                isTelemedicine = appointment.isTelemedicine,
                                onCardClick = { onAppointmentClick(appointment.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}