package com.project.mediturn.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.mediturn.data.DataSource
import com.project.mediturn.data.model.Doctor
import com.project.mediturn.data.model.Specialty
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class DoctorListState(
    val doctors: List<Doctor> = emptyList(),
    val filteredDoctors: List<Doctor> = emptyList(),
    val searchQuery: String = "",
    val selectedSpecialty: Specialty? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

class DoctorViewModel : ViewModel() {
    private val _state = MutableStateFlow(DoctorListState())
    val state: StateFlow<DoctorListState> = _state.asStateFlow()

    init {
        loadDoctors()
    }

    fun loadDoctors() {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            try {
                val doctors = DataSource.doctors
                _state.update {
                    it.copy(
                        doctors = doctors,
                        filteredDoctors = doctors,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        error = "Error al cargar médicos: ${e.message}",
                        isLoading = false
                    )
                }
            }
        }
    }

    fun searchDoctors(query: String) {
        _state.update { it.copy(searchQuery = query) }
        applyFilters()
    }

    fun filterBySpecialty(specialty: Specialty?) {
        _state.update { it.copy(selectedSpecialty = specialty) }
        applyFilters()
    }

    private fun applyFilters() {
        val state = _state.value
        var filtered = state.doctors

        if (state.searchQuery.isNotBlank()) {
            filtered = filtered.filter { doctor ->
                doctor.name.contains(state.searchQuery, ignoreCase = true) ||
                        doctor.specialty.contains(state.searchQuery, ignoreCase = true) ||
                        doctor.city.contains(state.searchQuery, ignoreCase = true)
            }
        }

        state.selectedSpecialty?.let { specialty ->
            filtered = filtered.filter { it.specialty == specialty.name }
        }

        _state.update { it.copy(filteredDoctors = filtered) }
    }

    fun clearError() {
        _state.update { it.copy(error = null) }
    }

    fun getSpecialties(): List<Specialty> {
        return DataSource.specialties
    }

    fun getDoctorById(id: Int): Doctor? {
        return DataSource.doctors.find { it.id == id }
    }
}