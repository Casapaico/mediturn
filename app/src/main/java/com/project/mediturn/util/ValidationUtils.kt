package com.project.mediturn.util

import java.time.LocalDate
import java.time.LocalDateTime

object ValidationUtils {

    // ========== EMAIL ==========

    private val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$")

    /**
     * Validar formato de email
     */
    fun isValidEmail(email: String): Boolean {
        return email.isNotBlank() && emailRegex.matches(email)
    }

    fun validateEmail(email: String): ValidationResult {
        return when {
            email.isBlank() -> ValidationResult(false, Constants.ERROR_EMPTY_FIELD)
            !isValidEmail(email) -> ValidationResult(false, Constants.ERROR_INVALID_EMAIL)
            else -> ValidationResult(true)
        }
    }

    // ========== TELÉFONO ==========

    private val phoneRegex = Regex("^[+]?[0-9]{9,15}\$")

    /**
     * Validar formato de teléfono
     * Acepta: 999888777, +51999888777, etc.
     */
    fun isValidPhone(phone: String): Boolean {
        val cleaned = phone.replace("\\s".toRegex(), "")
        return phoneRegex.matches(cleaned)
    }

    fun validatePhone(phone: String): ValidationResult {
        return when {
            phone.isBlank() -> ValidationResult(false, Constants.ERROR_EMPTY_FIELD)
            !isValidPhone(phone) -> ValidationResult(false, Constants.ERROR_INVALID_PHONE)
            else -> ValidationResult(true)
        }
    }

    // ========== DNI ==========

    private val dniRegex = Regex("^[0-9]{8}\$")

    /**
     * Validar DNI peruano (8 dígitos)
     */
    fun isValidDNI(dni: String): Boolean {
        return dniRegex.matches(dni)
    }

    fun validateDNI(dni: String): ValidationResult {
        return when {
            dni.isBlank() -> ValidationResult(false, Constants.ERROR_EMPTY_FIELD)
            !isValidDNI(dni) -> ValidationResult(false, Constants.ERROR_INVALID_DNI)
            else -> ValidationResult(true)
        }
    }

    // ========== MOTIVO DE CONSULTA ==========

    /**
     * Validar motivo de consulta
     */
    fun validateReason(reason: String): ValidationResult {
        return when {
            reason.isBlank() -> ValidationResult(false, Constants.ERROR_EMPTY_FIELD)
            reason.length < Constants.MIN_REASON_LENGTH ->
                ValidationResult(false, Constants.ERROR_SHORT_REASON)
            reason.length > Constants.MAX_REASON_LENGTH ->
                ValidationResult(false, Constants.ERROR_LONG_REASON)
            else -> ValidationResult(true)
        }
    }

    // ========== FECHA Y HORA ==========

    /**
     * Validar fecha seleccionada
     */
    fun validateDate(date: LocalDate?): ValidationResult {
        return when {
            date == null -> ValidationResult(false, Constants.ERROR_NO_DATE_SELECTED)
            DateUtils.isPastDate(date) -> ValidationResult(false, Constants.ERROR_PAST_DATE)
            !DateUtils.isWithinBookingRange(date) ->
                ValidationResult(false, "Fecha fuera del rango permitido")
            else -> ValidationResult(true)
        }
    }

    /**
     * Validar hora seleccionada
     */
    fun validateDateTime(dateTime: LocalDateTime?): ValidationResult {
        return when {
            dateTime == null -> ValidationResult(false, Constants.ERROR_NO_TIME_SELECTED)
            DateUtils.isPastDateTime(dateTime) ->
                ValidationResult(false, Constants.ERROR_PAST_DATE)
            !DateUtils.hasMinimumAdvance(dateTime) ->
                ValidationResult(false, Constants.ERROR_MIN_ADVANCE)
            else -> ValidationResult(true)
        }
    }

    // ========== FORMULARIO COMPLETO DE CITA ==========

    /**
     * Validar todos los campos del formulario de agendar cita
     */
    fun validateAppointmentForm(
        doctorId: Int?,
        date: LocalDate?,
        time: LocalDateTime?,
        reason: String
    ): FormValidationResult {
        val errors = mutableListOf<String>()

        if (doctorId == null || doctorId == 0) {
            errors.add("Seleccione un médico")
        }

        val dateValidation = validateDate(date)
        if (!dateValidation.isValid) {
            errors.add(dateValidation.errorMessage ?: "Error en fecha")
        }

        val timeValidation = validateDateTime(time)
        if (!timeValidation.isValid) {
            errors.add(timeValidation.errorMessage ?: "Error en hora")
        }

        val reasonValidation = validateReason(reason)
        if (!reasonValidation.isValid) {
            errors.add(reasonValidation.errorMessage ?: "Error en motivo")
        }

        return FormValidationResult(
            isValid = errors.isEmpty(),
            errors = errors
        )
    }

    // ========== CAMPOS GENERALES ==========

    /**
     * Validar que un campo no esté vacío
     */
    fun validateNotEmpty(value: String, fieldName: String = "Campo"): ValidationResult {
        return if (value.isBlank()) {
            ValidationResult(false, "$fieldName es obligatorio")
        } else {
            ValidationResult(true)
        }
    }

    /**
     * Validar longitud mínima
     */
    fun validateMinLength(
        value: String,
        minLength: Int,
        fieldName: String = "Campo"
    ): ValidationResult {
        return if (value.length < minLength) {
            ValidationResult(false, "$fieldName debe tener al menos $minLength caracteres")
        } else {
            ValidationResult(true)
        }
    }

    /**
     * Validar longitud máxima
     */
    fun validateMaxLength(
        value: String,
        maxLength: Int,
        fieldName: String = "Campo"
    ): ValidationResult {
        return if (value.length > maxLength) {
            ValidationResult(false, "$fieldName debe tener máximo $maxLength caracteres")
        } else {
            ValidationResult(true)
        }
    }

    // ========== CONTRASEÑA (para futuro login) ==========

    /**
     * Validar fortaleza de contraseña
     * Mínimo 8 caracteres, 1 mayúscula, 1 minúscula, 1 número
     */
    fun validatePassword(password: String): ValidationResult {
        return when {
            password.isBlank() -> ValidationResult(false, Constants.ERROR_EMPTY_FIELD)
            password.length < 8 ->
                ValidationResult(false, "Contraseña debe tener al menos 8 caracteres")
            !password.any { it.isUpperCase() } ->
                ValidationResult(false, "Debe contener al menos una mayúscula")
            !password.any { it.isLowerCase() } ->
                ValidationResult(false, "Debe contener al menos una minúscula")
            !password.any { it.isDigit() } ->
                ValidationResult(false, "Debe contener al menos un número")
            else -> ValidationResult(true)
        }
    }

    /**
     * Validar que dos contraseñas coincidan
     */
    fun validatePasswordMatch(password: String, confirmPassword: String): ValidationResult {
        return if (password != confirmPassword) {
            ValidationResult(false, "Las contraseñas no coinciden")
        } else {
            ValidationResult(true)
        }
    }
}

// ========== DATA CLASSES ==========

/**
 * Resultado de una validación simple
 */
data class ValidationResult(
    val isValid: Boolean,
    val errorMessage: String? = null
)

/**
 * Resultado de validación de formulario completo
 */
data class FormValidationResult(
    val isValid: Boolean,
    val errors: List<String> = emptyList()
) {
    val errorMessage: String
        get() = errors.joinToString("\n")
}