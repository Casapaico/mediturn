package com.project.mediturn.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.mediturn.data.DataSource
import com.project.mediturn.data.model.Doctor
import com.project.mediturn.data.model.Specialty
import com.project.mediturn.data.repository.DoctorRepository
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
class DoctorViewModel : ViewModel() {

    private val repository = DoctorRepository()

    // ========== STATE ==========

    private val _uiState = MutableStateFlow<DoctorUiState>(DoctorUiState.Loading)
    val uiState: StateFlow<DoctorUiState> = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedSpecialties = MutableStateFlow<Set<String>>(emptySet())
    val selectedSpecialties: StateFlow<Set<String>> = _selectedSpecialties.asStateFlow()

    private val _selectedDoctor = MutableStateFlow<Doctor?>(null)
    val selectedDoctor: StateFlow<Doctor?> = _selectedDoctor.asStateFlow()

    // ========== INIT ==========

    init {
        loadAllDoctors()
        setupSearch()
    }

    // ========== ACTIONS ==========

    /**
     * Cargar todos los médicos
     */
    fun loadAllDoctors() {
        viewModelScope.launch {
            _uiState.value = DoctorUiState.Loading

            repository.getAllDoctors()
                .onSuccess { doctors ->
                    _uiState.value = DoctorUiState.Success(
                        doctors = doctors,
                        specialties = DataSource.specialties
                    )
                }
                .onFailure { error ->
                    _uiState.value = DoctorUiState.Error(
                        error.message ?: "Error al cargar médicos"
                    )
                }
        }
    }

    /**
     * Buscar médicos
     */
    fun searchDoctors(
        query: String = _searchQuery.value,
        specialties: Set<String> = _selectedSpecialties.value
    ) {
        viewModelScope.launch {
            _uiState.value = DoctorUiState.Loading

            // Simular delay de búsqueda
            delay(300)

            repository.searchDoctors(
                query = query,
                specialty = specialties.firstOrNull()
            )
                .onSuccess { doctors ->
                    // Filtrar por múltiples especialidades si es necesario
                    val filteredDoctors = if (specialties.isEmpty()) {
                        doctors
                    } else {
                        doctors.filter { it.specialty in specialties }
                    }

                    _uiState.value = DoctorUiState.Success(
                        doctors = filteredDoctors,
                        specialties = DataSource.specialties
                    )
                }
                .onFailure { error ->
                    _uiState.value = DoctorUiState.Error(
                        error.message ?: "Error en la búsqueda"
                    )
                }
        }
    }

    /**
     * Actualizar query de búsqueda
     */
    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    /**
     * Toggle especialidad en filtros
     */
    fun toggleSpecialty(specialty: String) {
        val current = _selectedSpecialties.value
        _selectedSpecialties.value = if (specialty in current) {
            current - specialty
        } else {
            current + specialty
        }

        // Buscar automáticamente con los nuevos filtros
        searchDoctors()
    }

    /**
     * Limpiar filtros
     */
    fun clearFilters() {
        _selectedSpecialties.value = emptySet()
        _searchQuery.value = ""
        loadAllDoctors()
    }

    /**
     * Cargar doctor por ID
     */
    fun loadDoctorById(doctorId: Int) {
        viewModelScope.launch {
            _uiState.value = DoctorUiState.Loading

            repository.getDoctorById(doctorId)
                .onSuccess { doctor ->
                    _selectedDoctor.value = doctor
                    _uiState.value = DoctorUiState.DoctorDetail(doctor)
                }
                .onFailure { error ->
                    _uiState.value = DoctorUiState.Error(
                        error.message ?: "Médico no encontrado"
                    )
                }
        }
    }

    /**
     * Setup de búsqueda en tiempo real con debounce
     */
    private fun setupSearch() {
        viewModelScope.launch {
            _searchQuery
                .debounce(300) // Esperar 300ms después del último cambio
                .distinctUntilChanged()
                .collectLatest { query ->
                    if (query.length >= 2 || query.isEmpty()) {
                        searchDoctors(query)
                    }
                }
        }
    }
}

// ========== UI STATES ==========

sealed class DoctorUiState {
    object Loading : DoctorUiState()

    data class Success(
        val doctors: List<Doctor>,
        val specialties: List<Specialty>
    ) : DoctorUiState()

    data class DoctorDetail(
        val doctor: Doctor
    ) : DoctorUiState()

    data class Error(
        val message: String
    ) : DoctorUiState()
}