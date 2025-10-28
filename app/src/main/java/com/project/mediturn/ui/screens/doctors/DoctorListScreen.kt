package com.project.mediturn.ui.screens.doctors

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.swiperefresh.*
import com.project.mediturn.data.DataSource
import com.project.mediturn.ui.components.DoctorCard
import com.project.mediturn.ui.components.EmptyState
import com.project.mediturn.ui.components.SearchBar
import com.project.mediturn.viewmodel.DoctorUiState
import com.project.mediturn.viewmodel.DoctorViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoctorListScreen(
    onDoctorClick: (Int) -> Unit = {},
    onNavigateBack: () -> Unit = {},
    viewModel: DoctorViewModel = viewModel()
) {
    // Estados del ViewModel
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedSpecialties by viewModel.selectedSpecialties.collectAsState()
    val uiState by viewModel.uiState.collectAsState()

    // Estados locales de UI
    val snackbarHostState = remember { SnackbarHostState() }
    var showFilterDialog by remember { mutableStateOf(false) }
    var showAdvancedFilters by remember { mutableStateOf(false) }

    // Filtros adicionales
    var filterCity by remember { mutableStateOf<String?>(null) }
    var filterTelemedicine by remember { mutableStateOf<Boolean?>(null) }

    val specialties = DataSource.specialties
    val cities = remember { DataSource.doctors.map { it.city }.distinct().sorted() }

    // Estado de refresh
    val isRefreshing = uiState is DoctorUiState.Loading
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing)

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
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Barra de búsqueda con botones de filtro
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SearchBar(
                    query = searchQuery,
                    onQueryChange = { viewModel.updateSearchQuery(it) },
                    placeholder = "Buscar por nombre o especialidad...",
                    modifier = Modifier.weight(1f)
                )

                // Botón de filtro por especialidades
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

                // Botón de filtros avanzados
                BadgedBox(
                    badge = {
                        val activeFilters = listOfNotNull(
                            filterCity,
                            if (filterTelemedicine != null) "telemedicine" else null
                        ).size
                        if (activeFilters > 0) {
                            Badge {
                                Text(activeFilters.toString())
                            }
                        }
                    }
                ) {
                    IconButton(
                        onClick = { showAdvancedFilters = true },
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer
                        ),
                        modifier = Modifier.size(56.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Tune,
                            contentDescription = "Filtros avanzados",
                            tint = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
            }

            // Mostrar filtros activos
            if (selectedSpecialties.isNotEmpty() || filterCity != null || filterTelemedicine != null) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Filtros activos:",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                            TextButton(
                                onClick = {
                                    viewModel.clearFilters()
                                    filterCity = null
                                    filterTelemedicine = null
                                }
                            ) {
                                Text("Limpiar todo", fontSize = 12.sp)
                            }
                        }

                        if (selectedSpecialties.isNotEmpty()) {
                            Text(
                                text = "Especialidades: ${selectedSpecialties.joinToString(", ")}",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                        if (filterCity != null) {
                            Text(
                                text = "Ciudad: $filterCity",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                        if (filterTelemedicine != null) {
                            Text(
                                text = "Solo teleconsulta",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                    }
                }
            }

            HorizontalDivider()

            // Resultados con pull-to-refresh (simulado con Box)
            Box(modifier = Modifier.fillMaxSize()) {
                when (val state = uiState) {
                    is DoctorUiState.Loading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    is DoctorUiState.Success -> {
                        // Aplicar filtros adicionales
                        val filteredDoctors = state.doctors.filter { doctor ->
                            val matchesCity = filterCity == null || doctor.city == filterCity
                            val matchesTelemedicine = filterTelemedicine == null ||
                                    doctor.availableForTeleconsultation == filterTelemedicine
                            matchesCity && matchesTelemedicine
                        }

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
                                item {
                                    Text(
                                        text = "${filteredDoctors.size} médico(s) encontrado(s)",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }

                                items(filteredDoctors) { doctor ->
                                    DoctorCard(
                                        doctor = doctor,
                                        onClick = { onDoctorClick(doctor.id) }
                                    )
                                }

                                item {
                                    Spacer(modifier = Modifier.height(16.dp))
                                }
                            }
                        }
                    }

                    is DoctorUiState.Error -> {
                        EmptyState(
                            icon = "❌",
                            title = "Error",
                            message = state.message,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    else -> {}
                }
            }
        }
    }

    // Dialog de filtros por especialidad
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
                                    viewModel.toggleSpecialty(specialty.name)
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
                        viewModel.clearFilters()
                        showFilterDialog = false
                    }
                ) {
                    Text("Limpiar")
                }
            }
        )
    }

    // Dialog de filtros avanzados
    if (showAdvancedFilters) {
        AlertDialog(
            onDismissRequest = { showAdvancedFilters = false },
            icon = {
                Icon(Icons.Default.Tune, contentDescription = null)
            },
            title = {
                Text("Filtros Avanzados")
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Filtro por ciudad
                    Text(
                        "Ciudad:",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = filterCity == null,
                                onClick = { filterCity = null }
                            )
                            Text("Todas las ciudades", fontSize = 14.sp)
                        }

                        cities.forEach { city ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = filterCity == city,
                                    onClick = { filterCity = city }
                                )
                                Text(city, fontSize = 14.sp)
                            }
                        }
                    }

                    HorizontalDivider()

                    // Filtro por teleconsulta
                    Text(
                        "Tipo de consulta:",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = filterTelemedicine == null,
                                onClick = { filterTelemedicine = null }
                            )
                            Text("Todos", fontSize = 14.sp)
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = filterTelemedicine == true,
                                onClick = { filterTelemedicine = true }
                            )
                            Text("Solo teleconsulta", fontSize = 14.sp)
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = filterTelemedicine == false,
                                onClick = { filterTelemedicine = false }
                            )
                            Text("Solo presencial", fontSize = 14.sp)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showAdvancedFilters = false
                        viewModel.searchDoctors()
                    }
                ) {
                    Text("Aplicar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        filterCity = null
                        filterTelemedicine = null
                        showAdvancedFilters = false
                    }
                ) {
                    Text("Limpiar")
                }
            }
        )
    }
}