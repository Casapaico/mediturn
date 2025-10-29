package com.project.mediturn.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.project.mediturn.data.model.Patient
import com.project.mediturn.data.repository.AuthRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * Estados de la UI para autenticación
 */
sealed class AuthUiState {
    data object Idle : AuthUiState()
    data object Loading : AuthUiState()
    data class Success(val patient: Patient) : AuthUiState()
    data class Error(val message: String) : AuthUiState()
}

/**
 * Mensajes de autenticación
 */
sealed class AuthMessage {
    data class Success(val message: String) : AuthMessage()
    data class Error(val message: String) : AuthMessage()
}

/**
 * ViewModel para autenticación
 */
class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val authRepository = AuthRepository(application.applicationContext)

    // UI State
    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    // Mensajes/Eventos
    private val _authMessage = MutableSharedFlow<AuthMessage>()
    val authMessage: SharedFlow<AuthMessage> = _authMessage.asSharedFlow()

    // Estado de sesión
    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private val _currentPatient = MutableStateFlow<Patient?>(null)
    val currentPatient: StateFlow<Patient?> = _currentPatient.asStateFlow()

    // --- ESTADOS DE LOGIN ---
    private val _loginEmail = MutableStateFlow("")
    val loginEmail: StateFlow<String> = _loginEmail.asStateFlow()

    private val _loginPassword = MutableStateFlow("")
    val loginPassword: StateFlow<String> = _loginPassword.asStateFlow()

    private val _loginPasswordVisible = MutableStateFlow(false)
    val loginPasswordVisible: StateFlow<Boolean> = _loginPasswordVisible.asStateFlow()

    // --- ESTADOS DE REGISTRO ---
    private val _registerName = MutableStateFlow("")
    val registerName: StateFlow<String> = _registerName.asStateFlow()

    private val _registerEmail = MutableStateFlow("")
    val registerEmail: StateFlow<String> = _registerEmail.asStateFlow()

    private val _registerPhone = MutableStateFlow("")
    val registerPhone: StateFlow<String> = _registerPhone.asStateFlow()

    private val _registerDni = MutableStateFlow("")
    val registerDni: StateFlow<String> = _registerDni.asStateFlow()

    private val _registerPassword = MutableStateFlow("")
    val registerPassword: StateFlow<String> = _registerPassword.asStateFlow()

    private val _registerConfirmPassword = MutableStateFlow("")
    val registerConfirmPassword: StateFlow<String> = _registerConfirmPassword.asStateFlow()

    private val _registerPasswordVisible = MutableStateFlow(false)
    val registerPasswordVisible: StateFlow<Boolean> = _registerPasswordVisible.asStateFlow()

    private val _registerConfirmPasswordVisible = MutableStateFlow(false)
    val registerConfirmPasswordVisible: StateFlow<Boolean> = _registerConfirmPasswordVisible.asStateFlow()

    // --- ERRORES DE VALIDACIÓN ---
    private val _registerNameError = MutableStateFlow<String?>(null)
    val registerNameError: StateFlow<String?> = _registerNameError.asStateFlow()

    private val _registerEmailError = MutableStateFlow<String?>(null)
    val registerEmailError: StateFlow<String?> = _registerEmailError.asStateFlow()

    private val _registerPhoneError = MutableStateFlow<String?>(null)
    val registerPhoneError: StateFlow<String?> = _registerPhoneError.asStateFlow()

    private val _registerDniError = MutableStateFlow<String?>(null)
    val registerDniError: StateFlow<String?> = _registerDniError.asStateFlow()

    private val _registerPasswordError = MutableStateFlow<String?>(null)
    val registerPasswordError: StateFlow<String?> = _registerPasswordError.asStateFlow()

    private val _registerConfirmPasswordError = MutableStateFlow<String?>(null)
    val registerConfirmPasswordError: StateFlow<String?> = _registerConfirmPasswordError.asStateFlow()

    init {
        checkSession()
    }

    // =============================================
    // ACCIONES DE LOGIN
    // =============================================

    fun updateLoginEmail(email: String) {
        _loginEmail.value = email
    }

    fun updateLoginPassword(password: String) {
        _loginPassword.value = password
    }

    fun toggleLoginPasswordVisibility() {
        _loginPasswordVisible.value = !_loginPasswordVisible.value
    }

    fun isLoginFormValid(): Boolean {
        return _loginEmail.value.isNotBlank() && _loginPassword.value.isNotBlank()
    }

    fun login() {
        if (!isLoginFormValid()) return

        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading

            val result = authRepository.login(
                email = _loginEmail.value.trim(),
                password = _loginPassword.value
            )

            result.onSuccess { patient ->
                _uiState.value = AuthUiState.Success(patient)
                _currentPatient.value = patient
                _isLoggedIn.value = true
                _authMessage.emit(AuthMessage.Success("¡Bienvenido ${patient.name}!"))
                clearLoginForm()
            }.onFailure { exception ->
                _uiState.value = AuthUiState.Error(exception.message ?: "Error al iniciar sesión")
                _authMessage.emit(AuthMessage.Error(exception.message ?: "Error al iniciar sesión"))
            }
        }
    }

    private fun clearLoginForm() {
        _loginEmail.value = ""
        _loginPassword.value = ""
        _loginPasswordVisible.value = false
    }

    // =============================================
    // ACCIONES DE REGISTRO
    // =============================================

    fun updateRegisterName(name: String) {
        _registerName.value = name
        validateName(name)
    }

    fun updateRegisterEmail(email: String) {
        _registerEmail.value = email
        validateEmail(email)
    }

    fun updateRegisterPhone(phone: String) {
        _registerPhone.value = phone
        validatePhone(phone)
    }

    fun updateRegisterDni(dni: String) {
        _registerDni.value = dni
        validateDni(dni)
    }

    fun updateRegisterPassword(password: String) {
        _registerPassword.value = password
        validatePassword(password)
        // Re-validar confirmación si ya tiene contenido
        if (_registerConfirmPassword.value.isNotEmpty()) {
            validateConfirmPassword(_registerConfirmPassword.value)
        }
    }

    fun updateRegisterConfirmPassword(confirmPassword: String) {
        _registerConfirmPassword.value = confirmPassword
        validateConfirmPassword(confirmPassword)
    }

    fun toggleRegisterPasswordVisibility() {
        _registerPasswordVisible.value = !_registerPasswordVisible.value
    }

    fun toggleRegisterConfirmPasswordVisibility() {
        _registerConfirmPasswordVisible.value = !_registerConfirmPasswordVisible.value
    }

    // Validaciones individuales
    private fun validateName(name: String) {
        _registerNameError.value = when {
            name.isBlank() -> null
            name.length < 3 -> "El nombre debe tener al menos 3 caracteres"
            else -> null
        }
    }

    private fun validateEmail(email: String) {
        _registerEmailError.value = when {
            email.isBlank() -> null
            !authRepository.isValidEmail(email) -> "Email inválido"
            else -> null
        }
    }

    private fun validatePhone(phone: String) {
        _registerPhoneError.value = when {
            phone.isBlank() -> null
            !authRepository.isValidPhone(phone) -> "Teléfono inválido (9XXXXXXXX)"
            else -> null
        }
    }

    private fun validateDni(dni: String) {
        _registerDniError.value = when {
            dni.isBlank() -> null
            !authRepository.isValidDNI(dni) -> "DNI debe tener 8 dígitos"
            else -> null
        }
    }

    private fun validatePassword(password: String) {
        _registerPasswordError.value = when {
            password.isBlank() -> null
            !authRepository.isStrongPassword(password) ->
                "Mínimo 8 caracteres, 1 mayúscula, 1 minúscula y 1 número"
            else -> null
        }
    }

    private fun validateConfirmPassword(confirmPassword: String) {
        _registerConfirmPasswordError.value = when {
            confirmPassword.isBlank() -> null
            confirmPassword != _registerPassword.value -> "Las contraseñas no coinciden"
            else -> null
        }
    }

    fun isRegisterFormValid(): Boolean {
        val name = _registerName.value
        val email = _registerEmail.value
        val phone = _registerPhone.value
        val dni = _registerDni.value
        val password = _registerPassword.value
        val confirmPassword = _registerConfirmPassword.value

        return name.isNotBlank() && name.length >= 3 &&
                email.isNotBlank() && authRepository.isValidEmail(email) &&
                phone.isNotBlank() && authRepository.isValidPhone(phone) &&
                dni.isNotBlank() && authRepository.isValidDNI(dni) &&
                password.isNotBlank() && authRepository.isStrongPassword(password) &&
                confirmPassword == password &&
                _registerNameError.value == null &&
                _registerEmailError.value == null &&
                _registerPhoneError.value == null &&
                _registerDniError.value == null &&
                _registerPasswordError.value == null &&
                _registerConfirmPasswordError.value == null
    }

    fun register() {
        if (!isRegisterFormValid()) {
            viewModelScope.launch {
                _authMessage.emit(AuthMessage.Error("Por favor completa todos los campos correctamente"))
            }
            return
        }

        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading

            val result = authRepository.register(
                name = _registerName.value.trim(),
                email = _registerEmail.value.trim(),
                password = _registerPassword.value,
                phone = _registerPhone.value.trim(),
                dni = _registerDni.value.trim()
            )

            result.onSuccess { patient ->
                _uiState.value = AuthUiState.Success(patient)
                _currentPatient.value = patient
                _isLoggedIn.value = true
                _authMessage.emit(AuthMessage.Success("¡Cuenta creada exitosamente!"))
                clearRegisterForm()
            }.onFailure { exception ->
                _uiState.value = AuthUiState.Error(exception.message ?: "Error al crear cuenta")
                _authMessage.emit(AuthMessage.Error(exception.message ?: "Error al crear cuenta"))
            }
        }
    }

    private fun clearRegisterForm() {
        _registerName.value = ""
        _registerEmail.value = ""
        _registerPhone.value = ""
        _registerDni.value = ""
        _registerPassword.value = ""
        _registerConfirmPassword.value = ""
        _registerPasswordVisible.value = false
        _registerConfirmPasswordVisible.value = false
        _registerNameError.value = null
        _registerEmailError.value = null
        _registerPhoneError.value = null
        _registerDniError.value = null
        _registerPasswordError.value = null
        _registerConfirmPasswordError.value = null
    }

    // =============================================
    // GESTIÓN DE SESIÓN
    // =============================================

    private fun checkSession() {
        viewModelScope.launch {
            _isLoggedIn.value = authRepository.isLoggedIn()

            if (_isLoggedIn.value) {
                val result = authRepository.getCurrentPatient()
                result.onSuccess { patient ->
                    _currentPatient.value = patient
                    if (patient != null) {
                        _uiState.value = AuthUiState.Success(patient)
                    }
                }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading

            val result = authRepository.logout()

            result.onSuccess {
                _uiState.value = AuthUiState.Idle
                _currentPatient.value = null
                _isLoggedIn.value = false
                clearLoginForm()
                clearRegisterForm()
                _authMessage.emit(AuthMessage.Success("Sesión cerrada correctamente"))
            }.onFailure { exception ->
                _uiState.value = AuthUiState.Error(exception.message ?: "Error al cerrar sesión")
                _authMessage.emit(AuthMessage.Error(exception.message ?: "Error al cerrar sesión"))
            }
        }
    }

    // =============================================
    // ACTUALIZACIÓN DE PERFIL
    // =============================================

    fun updateProfile(name: String, phone: String) {
        val patientId = _currentPatient.value?.id ?: return

        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading

            val result = authRepository.updateProfile(patientId, name, phone)

            result.onSuccess { updatedPatient ->
                _currentPatient.value = updatedPatient
                _uiState.value = AuthUiState.Success(updatedPatient)
                _authMessage.emit(AuthMessage.Success("Perfil actualizado"))
            }.onFailure { exception ->
                _uiState.value = AuthUiState.Error(exception.message ?: "Error al actualizar perfil")
                _authMessage.emit(AuthMessage.Error(exception.message ?: "Error al actualizar perfil"))
            }
        }
    }

    fun changePassword(oldPassword: String, newPassword: String) {
        val patientId = _currentPatient.value?.id ?: return

        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading

            val result = authRepository.changePassword(patientId, oldPassword, newPassword)

            result.onSuccess {
                _uiState.value = AuthUiState.Idle
                _authMessage.emit(AuthMessage.Success("Contraseña actualizada"))
            }.onFailure { exception ->
                _uiState.value = AuthUiState.Error(exception.message ?: "Error al cambiar contraseña")
                _authMessage.emit(AuthMessage.Error(exception.message ?: "Error al cambiar contraseña"))
            }
        }
    }
}