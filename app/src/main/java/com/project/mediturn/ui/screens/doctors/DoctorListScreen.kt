package com.project.mediturn.ui.screens.doctors

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.project.mediturn.ui.components.DoctorCard
import com.project.mediturn.ui.components.EmptyState
import com.project.mediturn.ui.components.SearchBar
import com.project.mediturn.ui.components.SpecialtyChip
import com.project.mediturn.viewmodel.DoctorViewModel

@Composable
fun DoctorListScreen(
    onDoctorClick: (Int) -> Unit,
    viewModel: DoctorViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadDoctors()
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
            query = state.searchQuery,
            onQueryChange = { viewModel.searchDoctors(it) },
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
                isSelected = state.selectedSpecialty == null,
                onSelected = { viewModel.filterBySpecialty(null) }
            )
            viewModel.getSpecialties().take(4).forEach { specialty ->
                SpecialtyChip(
                    specialty = specialty.name,
                    emoji = specialty.iconUrl,
                    isSelected = state.selectedSpecialty?.id == specialty.id,
                    onSelected = { viewModel.filterBySpecialty(specialty) }
                )
            }
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
                        text = "Cargando médicos...",
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
            state.filteredDoctors.isEmpty() -> {
                EmptyState(
                    emoji = "🔍",
                    title = "No se encontraron médicos",
                    description = "Intenta con otros términos de búsqueda"
                )
            }
            else -> {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // ✅ CORRECCIÓN: Usar items() con la lista directamente
                    items(state.filteredDoctors) { doctor ->
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
}
