package com.project.mediturn.ui.screens.appointments

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.project.mediturn.data.DataSource
import com.project.mediturn.ui.components.AppointmentCard
import com.project.mediturn.ui.components.EmptyState

@Composable
fun MyAppointmentsScreen(
    onAppointmentClick: (Int) -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) }

    val appointments = DataSource.appointments
    val upcomingAppointments = appointments.filter { it.status.name in listOf("PENDING", "CONFIRMED") }
    val pastAppointments = appointments.filter { it.status.name in listOf("COMPLETED", "CANCELLED") }

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
                text = { Text("Próximas (${upcomingAppointments.size})") },
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 }
            )
            Tab(
                text = { Text("Pasadas (${pastAppointments.size})") },
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 }
            )
        }

        // Contenido de las tabs
        when (selectedTab) {
            0 -> {
                if (upcomingAppointments.isEmpty()) {
                    EmptyState(
                        emoji = "📅",
                        title = "No tienes citas próximas",
                        description = "Agenda tu primera cita médica"
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 16.dp)
                    ) {
                        items(upcomingAppointments) { appointment ->
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
            1 -> {
                if (pastAppointments.isEmpty()) {
                    EmptyState(
                        emoji = "✅",
                        title = "No hay citas pasadas",
                        description = "Tus citas completadas aparecerán aquí"
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 16.dp)
                    ) {
                        items(pastAppointments) { appointment ->
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