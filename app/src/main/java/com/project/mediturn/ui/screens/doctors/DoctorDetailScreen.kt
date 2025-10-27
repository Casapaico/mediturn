package com.project.mediturn.ui.screens.doctors

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.project.mediturn.data.DataSource

@Composable
fun DoctorDetailScreen(
    doctorId: Int,
    onBookAppointment: () -> Unit
) {
    val doctor = DataSource.doctors.find { it.id == doctorId } ?: return

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // Header con foto
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) {
            AsyncImage(
                model = doctor.photoUrl,
                contentDescription = "Foto de ${doctor.name}",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
        }

        // Contenido
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Nombre y especialidad
            Text(
                text = doctor.name,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = doctor.specialty,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Rating y experiencia
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Rating",
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "%.1f".format(doctor.rating),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(start = 4.dp, end = 16.dp)
                )
                Text(
                    text = "•",
                    color = MaterialTheme.colorScheme.outline
                )
                Text(
                    text = "${doctor.yearsOfExperience} años de experiencia",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }

            // Información del doctor
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Información Profesional",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = "Colegiatura: ${doctor.collegiateCertificate}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "Departamento: ${doctor.department}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "Ciudad: ${doctor.city}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            // Descripción
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Sobre el doctor",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = doctor.description,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            // Servicios y precios
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Card(
                    modifier = Modifier.weight(1f),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "👨‍⚕️",
                            style = MaterialTheme.typography.headlineMedium
                        )
                        Text(
                            text = "Presencial",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "S/. %.0f".format(doctor.consultationPrice),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                if (doctor.availableForTeleconsultation) {
                    Card(
                        modifier = Modifier.weight(1f),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "💻",
                                style = MaterialTheme.typography.headlineMedium
                            )
                            Text(
                                text = "Virtual",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "S/. %.0f".format(doctor.consultationPrice * 0.8),
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(80.dp))
        }
    }

    // Bottom bar fija
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Button(
            onClick = onBookAppointment,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text(
                text = "Agendar Cita",
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}