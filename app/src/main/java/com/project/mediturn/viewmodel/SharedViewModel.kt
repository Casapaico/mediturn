package com.project.mediturn.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class UserState(
    val userId: Int = 1,
    val userName: String = "Juan Pérez",
    val userEmail: String = "juan.perez@email.com",
    val isLoggedIn: Boolean = true
)

data class AppState(
    val isLoading: Boolean = false,
    val currentScreen: String = "",
    val errorMessage: String? = null,
    val successMessage: String? = null
)

class SharedViewModel : ViewModel() {
    private val _userState = MutableStateFlow(UserState())
    val userState: StateFlow<UserState> = _userState.asStateFlow()

    private val _appState = MutableStateFlow(AppState())
    val appState: StateFlow<AppState> = _appState.asStateFlow()

    fun setLoading(loading: Boolean) {
        _appState.update { it.copy(isLoading = loading) }
    }

    fun setError(message: String?) {
        _appState.update { it.copy(errorMessage = message) }
    }

    fun setSuccess(message: String?) {
        _appState.update { it.copy(successMessage = message) }
    }

    fun setCurrentScreen(screen: String) {
        _appState.update { it.copy(currentScreen = screen) }
    }

    fun clearMessages() {
        _appState.update {
            it.copy(
                errorMessage = null,
                successMessage = null
            )
        }
    }

    fun updateUserProfile(name: String, email: String) {
        _userState.update {
            it.copy(
                userName = name,
                userEmail = email
            )
        }
    }

    fun logout() {
        viewModelScope.launch {
            _userState.update {
                it.copy(
                    isLoggedIn = false,
                    userName = "",
                    userEmail = ""
                )
            }
            _appState.update {
                it.copy(
                    currentScreen = "login",
                    successMessage = "Sesión cerrada exitosamente"
                )
            }
        }
    }
}