package com.project.mediturn.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.mediturn.data.DataSource
import com.project.mediturn.data.model.Patient
import com.project.mediturn.util.ValidationUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    // ========== STATE ==========

    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    private val _currentUser = MutableStateFlow<Patient?>(null)
    val currentUser: StateFlow<Patient?> = _currentUser.asStateFlow()

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    // Campos de login
    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    // Campos de registro
    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name.asStateFlow()

    private val _phone = MutableStateFlow("")
    val phone: StateFlow<String> = _phone.asStateFlow()

    private val _dni = MutableStateFlow("")
    val dni: StateFlow<String> = _dni.asStateFlow()

    // Mensajes de acción
    private val _authMessage = MutableSharedFlow<AuthMessage>()
    val authMessage: SharedFlow<AuthMessage> = _authMessage.asSharedFlow()

    // ========== INIT ==========

    init {
        // Auto-login con usuario simulado (solo para desarrollo)
        _currentUser.value = DataSource.currentPatient
        _isLoggedIn.value = true
    }

    // ========== ACTIONS - LOGIN ==========

    /**
     * Actualizar email
     */
    fun updateEmail(email: String) {
        _email.value = email
    }

    /**
     * Actualizar contraseña
     */
    fun updatePassword(password: String) {
        _password.value = password
    }

    /**
     * Login (simulado)
     */
    fun login() {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading

            // Validar email
            val emailValidation = ValidationUtils.validateEmail(_email.value)
            if (!emailValidation.isValid) {
                _uiState.value = AuthUiState.Error(
                    emailValidation.errorMessage ?: "Email inválido"
                )
                _authMessage.emit(
                    AuthMessage.Error(emailValidation.errorMessage ?: "Email inválido")
                )
                return@launch
            }

            // Validar contraseña
            if (_password.value.isEmpty()) {
                _uiState.value = AuthUiState.Error("Ingrese su contraseña")
                _authMessage.emit(AuthMessage.Error("Ingrese su contraseña"))
                return@launch
            }

            // Simular llamada a API
            delay(1500)

            // Login simulado (siempre exitoso por ahora)
            _currentUser.value = DataSource.currentPatient
            _isLoggedIn.value = true
            _uiState.value = AuthUiState.Success
            _authMessage.emit(AuthMessage.Success("Bienvenido, ${DataSource.currentPatient.name}"))
        }
    }

    /**
     * Login rápido (temporal - sin validación)
     */
    fun quickLogin() {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            delay(800)
            _currentUser.value = DataSource.currentPatient
            _isLoggedIn.value = true
            _uiState.value = AuthUiState.Success
        }
    }

    // ========== ACTIONS - REGISTRO ==========

    /**
     * Actualizar nombre
     */
    fun updateName(name: String) {
        _name.value = name
    }

    /**
     * Actualizar teléfono
     */
    fun updatePhone(phone: String) {
        _phone.value = phone
    }

    /**
     * Actualizar DNI
     */
    fun updateDNI(dni: String) {
        _dni.value = dni
    }

    /**
     * Registrar nuevo usuario (simulado)
     */
    fun register() {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading

            // Validaciones
            val nameValidation = ValidationUtils.validateNotEmpty(_name.value, "Nombre")
            if (!nameValidation.isValid) {
                _uiState.value = AuthUiState.Error(
                    nameValidation.errorMessage ?: "Nombre requerido"
                )
                _authMessage.emit(
                    AuthMessage.Error(nameValidation.errorMessage ?: "Nombre requerido")
                )
                return@launch
            }

            val emailValidation = ValidationUtils.validateEmail(_email.value)
            if (!emailValidation.isValid) {
                _uiState.value = AuthUiState.Error(
                    emailValidation.errorMessage ?: "Email inválido"
                )
                _authMessage.emit(
                    AuthMessage.Error(emailValidation.errorMessage ?: "Email inválido")
                )
                return@launch
            }

            val phoneValidation = ValidationUtils.validatePhone(_phone.value)
            if (!phoneValidation.isValid) {
                _uiState.value = AuthUiState.Error(
                    phoneValidation.errorMessage ?: "Teléfono inválido"
                )
                _authMessage.emit(
                    AuthMessage.Error(phoneValidation.errorMessage ?: "Teléfono inválido")
                )
                return@launch
            }

            val dniValidation = ValidationUtils.validateDNI(_dni.value)
            if (!dniValidation.isValid) {
                _uiState.value = AuthUiState.Error(
                    dniValidation.errorMessage ?: "DNI inválido"
                )
                _authMessage.emit(
                    AuthMessage.Error(dniValidation.errorMessage ?: "DNI inválido")
                )
                return@launch
            }

            val passwordValidation = ValidationUtils.validatePassword(_password.value)
            if (!passwordValidation.isValid) {
                _uiState.value = AuthUiState.Error(
                    passwordValidation.errorMessage ?: "Contraseña inválida"
                )
                _authMessage.emit(
                    AuthMessage.Error(passwordValidation.errorMessage ?: "Contraseña inválida")
                )
                return@launch
            }

            // Simular registro
            delay(2000)

            // Crear nuevo paciente
            val newPatient = Patient(
                id = 2, // ID simulado
                name = _name.value,
                email = _email.value,
                phone = _phone.value,
                dni = _dni.value
            )

            _currentUser.value = newPatient
            _isLoggedIn.value = true
            _uiState.value = AuthUiState.Success
            _authMessage.emit(AuthMessage.Success("Cuenta creada exitosamente"))

            // Limpiar formulario
            clearRegisterForm()
        }
    }

    // ========== ACTIONS - LOGOUT ==========

    /**
     * Cerrar sesión
     */
    fun logout() {
        viewModelScope.launch {
            _currentUser.value = null
            _isLoggedIn.value = false
            _uiState.value = AuthUiState.Idle
            _authMessage.emit(AuthMessage.Success("Sesión cerrada"))
            clearLoginForm()
        }
    }

    // ========== HELPERS ==========

    /**
     * Limpiar formulario de login
     */
    fun clearLoginForm() {
        _email.value = ""
        _password.value = ""
    }

    /**
     * Limpiar formulario de registro
     */
    fun clearRegisterForm() {
        _name.value = ""
        _email.value = ""
        _password.value = ""
        _phone.value = ""
        _dni.value = ""
    }

    /**
     * Validar si el formulario de login está completo
     */
    fun isLoginFormValid(): Boolean {
        return _email.value.isNotEmpty() && _password.value.isNotEmpty()
    }

    /**
     * Validar si el formulario de registro está completo
     */
    fun isRegisterFormValid(): Boolean {
        return _name.value.isNotEmpty() &&
               _email.value.isNotEmpty() &&
               _password.value.isNotEmpty() &&
               _phone.value.isNotEmpty() &&
               _dni.value.isNotEmpty()
    }
}

// ========== UI STATES ==========

sealed class AuthUiState {
    object Idle : AuthUiState()
    object Loading : AuthUiState()
    object Success : AuthUiState()
    data class Error(val message: String) : AuthUiState()
}

// ========== AUTH MESSAGES ==========

sealed class AuthMessage {
    data class Success(val message: String) : AuthMessage()
    data class Error(val message: String) : AuthMessage()
}