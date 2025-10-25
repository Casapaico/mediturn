package com.project.mediturn.ui.screens.doctors

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.project.mediturn.data.DataSource
import com.project.mediturn.ui.components.DoctorCard
import com.project.mediturn.ui.components.EmptyState
import com.project.mediturn.ui.components.SearchBar
import com.project.mediturn.ui.components.SpecialtyChip

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoctorListScreen(
    onDoctorClick: (Int) -> Unit = {},
    onNavigateBack: () -> Unit = {}
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedSpecialty by remember { mutableStateOf<String?>(null) }
    
    val specialties = DataSource.specialties
    
    // Filtrar médicos según búsqueda y especialidad
    val filteredDoctors = remember(searchQuery, selectedSpecialty) {
        DataSource.searchDoctors(
            query = searchQuery,
            specialty = selectedSpecialty
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Buscar Médicos",
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
            // Buscador
            SearchBar(
                query = searchQuery,
                onQueryChange = { searchQuery = it },
                placeholder = "Buscar por nombre o especialidad...",
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
            )

            // Filtros por especialidad
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                items(specialties) { specialty ->
                    SpecialtyChip(
                        specialty = specialty.name,
                        icon = specialty.iconUrl,
                        isSelected = selectedSpecialty == specialty.name,
                        onClick = {
                            selectedSpecialty = if (selectedSpecialty == specialty.name) {
                                null // Deseleccionar
                            } else {
                                specialty.name
                            }
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Resultados
            if (filteredDoctors.isEmpty()) {
                EmptyState(
                    icon = "🔍",
                    title = "No se encontraron médicos",
                    message = "Intenta con otros términos de búsqueda o especialidad",
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filteredDoctors) { doctor ->
                        DoctorCard(
                            doctor = doctor,
                            onClick = { onDoctorClick(doctor.id) }
                        )
                    }
                }
            }
        }
    }
}