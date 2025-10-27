package com.project.mediturn.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Search
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
import com.project.mediturn.data.DataSource
import com.project.mediturn.ui.components.SpecialtyChip

@Composable
fun HomeScreen(
    onNavigateToSearch: () -> Unit,
    onNavigateToAppointments: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Saludo personalizado
        Text(
            text = "¡Hola, Juan!",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "¿En qué podemos ayudarte hoy?",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Botones de acción rápida
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = onNavigateToSearch,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Buscar médicos",
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = "Buscar Médicos",
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            Button(
                onClick = onNavigateToAppointments,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                )
            ) {
                Icon(
                    imageVector = Icons.Default.CalendarMonth,
                    contentDescription = "Mis citas",
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = "Mis Citas",
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }

        // Especialidades populares
        Text(
            text = "Especialidades Populares",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.padding(bottom = 24.dp)
        ) {
            items(DataSource.specialties.take(6)) { specialty ->
                SpecialtyChip(
                    specialty = specialty.name,
                    emoji = specialty.iconUrl,
                    isSelected = false,
                    onSelected = { /* Implementar filtro */ }
                )
            }
        }

        // Próximas citas (Preview)
        Text(
            text = "Tu próxima cita",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Card(
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Dr. Carlos Mendoza",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Cardiología",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = "Mañana • 10:00 AM",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}