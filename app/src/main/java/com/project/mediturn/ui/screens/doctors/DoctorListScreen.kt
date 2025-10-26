package com.project.mediturn.ui.screens.doctors

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.project.mediturn.data.DataSource
import com.project.mediturn.ui.components.DoctorCard
import com.project.mediturn.ui.components.EmptyState
import com.project.mediturn.ui.components.SearchBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoctorListScreen(
    onDoctorClick: (Int) -> Unit = {},
    onNavigateBack: () -> Unit = {}
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedSpecialties by remember { mutableStateOf(setOf<String>()) }
    var showFilterDialog by remember { mutableStateOf(false) }

    val specialties = DataSource.specialties

    // Filtrar médicos según búsqueda y especialidades seleccionadas
    val filteredDoctors = remember(searchQuery, selectedSpecialties) {
        DataSource.searchDoctors(
            query = searchQuery,
            specialty = if (selectedSpecialties.isEmpty()) null else selectedSpecialties.firstOrNull()
        ).filter { doctor ->
            selectedSpecialties.isEmpty() || selectedSpecialties.contains(doctor.specialty)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Búsqueda de Médicos",
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
            // Barra de búsqueda con botón de filtro
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Buscador
                SearchBar(
                    query = searchQuery,
                    onQueryChange = { searchQuery = it },
                    placeholder = "Buscar por nombre o especialidad...",
                    modifier = Modifier.weight(1f)
                )

                // Botón de filtro (dropdown)
                BadgedBox(
                    badge = {
                        if (selectedSpecialties.isNotEmpty()) {
                            Badge {
                                Text(selectedSpecialties.size.toString())
                            }
                        }
                    }
                ) {
                    IconButton(
                        onClick = { showFilterDialog = true },
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        ),
                        modifier = Modifier.size(56.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.FilterList,
                            contentDescription = "Filtrar especialidades",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            // Mostrar filtros activos
            if (selectedSpecialties.isNotEmpty()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Filtrando por: ${selectedSpecialties.joinToString(", ")}",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            modifier = Modifier.weight(1f)
                        )
                        TextButton(
                            onClick = { selectedSpecialties = setOf() }
                        ) {
                            Text("Limpiar", fontSize = 12.sp)
                        }
                    }
                }
            }

            HorizontalDivider()

            // Resultados
            if (filteredDoctors.isEmpty()) {
                EmptyState(
                    icon = "🔍",
                    title = "No se encontraron médicos",
                    message = "Intenta con otros términos de búsqueda o filtros",
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

    // Dialog de filtros (especialidades con checkboxes)
    if (showFilterDialog) {
        AlertDialog(
            onDismissRequest = { showFilterDialog = false },
            icon = {
                Icon(Icons.Default.FilterList, contentDescription = null)
            },
            title = {
                Text("Filtrar por Especialidad")
            },
            text = {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(specialties) { specialty ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = selectedSpecialties.contains(specialty.name),
                                onCheckedChange = { checked ->
                                    selectedSpecialties = if (checked) {
                                        selectedSpecialties + specialty.name
                                    } else {
                                        selectedSpecialties - specialty.name
                                    }
                                }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "${specialty.iconUrl} ${specialty.name}",
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = { showFilterDialog = false }
                ) {
                    Text("Aplicar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        selectedSpecialties = setOf()
                        showFilterDialog = false
                    }
                ) {
                    Text("Limpiar todo")
                }
            }
        )
    }
}