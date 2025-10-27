package com.project.mediturn.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.mediturn.data.DataSource
import com.project.mediturn.data.model.Patient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ProfileState(
    val patient: Patient? = null,
    val isLoading: Boolean = false,
    val isEditing: Boolean = false,
    val error: String? = null,
    val successMessage: String? = null
)

class ProfileViewModel : ViewModel() {
    private val _state = MutableStateFlow(ProfileState())
    val state: StateFlow<ProfileState> = _state.asStateFlow()

    init {
        loadPatientProfile()
    }

    fun loadPatientProfile() {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            try {
                val patient = DataSource.patients.first()
                _state.update {
                    it.copy(
                        patient = patient,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        error = "Error al cargar perfil: ${e.message}",
                        isLoading = false
                    )
                }
            }
        }
    }

    fun updatePatientProfile(name: String, email: String, phone: String) {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            try {
                val currentPatient = _state.value.patient
                currentPatient?.let { patient ->
                    val updatedPatient = patient.copy(
                        name = name,
                        email = email,
                        phone = phone
                    )

                    _state.update {
                        it.copy(
                            patient = updatedPatient,
                            isLoading = false,
                            isEditing = false,
                            successMessage = "Perfil actualizado exitosamente"
                        )
                    }
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        error = "Error al actualizar perfil: ${e.message}",
                        isLoading = false
                    )
                }
            }
        }
    }

    fun setEditing(isEditing: Boolean) {
        _state.update { it.copy(isEditing = isEditing) }
    }

    fun clearMessages() {
        _state.update {
            it.copy(
                error = null,
                successMessage = null
            )
        }
    }
}