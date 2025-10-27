package com.project.mediturn.ui.screens.doctors

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.project.mediturn.data.DataSource
import com.project.mediturn.ui.components.DoctorCard
import com.project.mediturn.ui.components.EmptyState
import com.project.mediturn.ui.components.SearchBar
import com.project.mediturn.ui.components.SpecialtyChip

@Composable
fun DoctorListScreen(
    onDoctorClick: (Int) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedSpecialty by remember { mutableStateOf<String?>(null) }

    val filteredDoctors = DataSource.doctors.filter { doctor ->
        val matchesSearch = searchQuery.isEmpty() ||
                doctor.name.contains(searchQuery, ignoreCase = true) ||
                doctor.specialty.contains(searchQuery, ignoreCase = true)

        val matchesSpecialty = selectedSpecialty == null ||
                doctor.specialty == selectedSpecialty

        matchesSearch && matchesSpecialty
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Buscar Médicos",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Barra de búsqueda
        SearchBar(
            query = searchQuery,
            onQueryChange = { searchQuery = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        // Chips de especialidades
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SpecialtyChip(
                specialty = "Todos",
                emoji = "👨‍⚕️",
                isSelected = selectedSpecialty == null,
                onSelected = { selectedSpecialty = null }
            )
            DataSource.specialties.take(4).forEach { specialty ->
                SpecialtyChip(
                    specialty = specialty.name,
                    emoji = specialty.iconUrl,
                    isSelected = selectedSpecialty == specialty.name,
                    onSelected = { selectedSpecialty = specialty.name }
                )
            }
        }

        // Lista de médicos
        if (filteredDoctors.isEmpty()) {
            EmptyState(
                emoji = "🔍",
                title = "No se encontraron médicos",
                description = "Intenta con otros términos de búsqueda"
            )
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filteredDoctors) { doctor ->
                    DoctorCard(
                        name = doctor.name,
                        specialty = doctor.specialty,
                        rating = doctor.rating,
                        experience = doctor.yearsOfExperience,
                        price = doctor.consultationPrice,
                        photoUrl = doctor.photoUrl,
                        isTelemedicineAvailable = doctor.availableForTeleconsultation,
                        onCardClick = { onDoctorClick(doctor.id) }
                    )
                }
            }
        }
    }
}