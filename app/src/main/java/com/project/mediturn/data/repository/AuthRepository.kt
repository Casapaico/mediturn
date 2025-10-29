package com.project.mediturn.data.repository

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.project.mediturn.data.local.MediTurnDatabase
import com.project.mediturn.data.local.entity.PatientEntity
import com.project.mediturn.data.local.mapper.toModel
import com.project.mediturn.data.model.Patient
import kotlinx.coroutines.delay
import java.security.MessageDigest
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Repositorio para autenticación de usuarios
 */
class AuthRepository(private val context: Context) {

    private val database = MediTurnDatabase.getDatabase(context)
    private val patientDao = database.patientDao()

    // SharedPreferences encriptadas para tokens/sesión
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val encryptedPrefs = EncryptedSharedPreferences.create(
        context,
        "mediturn_secure_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    companion object {
        private const val KEY_PATIENT_ID = "patient_id"
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
    }

    /**
     * Registrar nuevo paciente
     */
    suspend fun register(
        name: String,
        email: String,
        password: String,
        phone: String,
        dni: String
    ): Result<Patient> {
        return try {
            delay(1500) // Simular latencia de red

            // Validar que el email no exista
            if (patientDao.emailExists(email)) {
                return Result.failure(Exception("El email ya está registrado"))
            }

            // Validar que el DNI no exista
            if (patientDao.dniExists(dni)) {
                return Result.failure(Exception("El DNI ya está registrado"))
            }

            // Encriptar contraseña
            val passwordHash = hashPassword(password)

            // Crear entidad
            val patientEntity = PatientEntity(
                name = name,
                email = email,
                phone = phone,
                dni = dni,
                passwordHash = passwordHash,
                createdAt = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                isActive = true
            )

            // Insertar en BD
            val patientId = patientDao.insertPatient(patientEntity)

            // Obtener paciente creado
            val createdPatient = patientDao.getPatientById(patientId.toInt())
                ?: return Result.failure(Exception("Error al crear cuenta"))

            // Guardar sesión
            saveSession(createdPatient.id)

            Result.success(createdPatient.toModel())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Login con email y contraseña
     */
    suspend fun login(email: String, password: String): Result<Patient> {
        return try {
            delay(1200) // Simular latencia de red

            // Buscar paciente por email
            val patientEntity = patientDao.getPatientByEmail(email)
                ?: return Result.failure(Exception("Email no registrado"))

            // Verificar que la cuenta esté activa
            if (!patientEntity.isActive) {
                return Result.failure(Exception("Cuenta desactivada"))
            }

            // Verificar contraseña
            val passwordHash = hashPassword(password)
            if (patientEntity.passwordHash != passwordHash) {
                return Result.failure(Exception("Contraseña incorrecta"))
            }

            // Guardar sesión
            saveSession(patientEntity.id)

            Result.success(patientEntity.toModel())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Logout
     */
    suspend fun logout(): Result<Boolean> {
        return try {
            delay(500)
            clearSession()
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Obtener paciente actual (sesión activa)
     */
    suspend fun getCurrentPatient(): Result<Patient?> {
        return try {
            val patientId = getSessionPatientId()
            if (patientId == null || !isLoggedIn()) {
                return Result.success(null)
            }

            val patientEntity = patientDao.getPatientById(patientId)
            if (patientEntity == null || !patientEntity.isActive) {
                clearSession()
                return Result.success(null)
            }

            Result.success(patientEntity.toModel())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Actualizar perfil del paciente
     */
    suspend fun updateProfile(
        patientId: Int,
        name: String,
        phone: String
    ): Result<Patient> {
        return try {
            delay(800)

            val patientEntity = patientDao.getPatientById(patientId)
                ?: return Result.failure(Exception("Paciente no encontrado"))

            val updatedEntity = patientEntity.copy(
                name = name,
                phone = phone
            )

            patientDao.updatePatient(updatedEntity)

            val updated = patientDao.getPatientById(patientId)!!
            Result.success(updated.toModel())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Cambiar contraseña
     */
    suspend fun changePassword(
        patientId: Int,
        oldPassword: String,
        newPassword: String
    ): Result<Boolean> {
        return try {
            delay(1000)

            val patientEntity = patientDao.getPatientById(patientId)
                ?: return Result.failure(Exception("Paciente no encontrado"))

            // Verificar contraseña actual
            val oldPasswordHash = hashPassword(oldPassword)
            if (patientEntity.passwordHash != oldPasswordHash) {
                return Result.failure(Exception("Contraseña actual incorrecta"))
            }

            // Actualizar con nueva contraseña
            val newPasswordHash = hashPassword(newPassword)
            val updatedEntity = patientEntity.copy(passwordHash = newPasswordHash)
            patientDao.updatePatient(updatedEntity)

            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Verificar si hay sesión activa
     */
    fun isLoggedIn(): Boolean {
        return encryptedPrefs.getBoolean(KEY_IS_LOGGED_IN, false)
    }

    /**
     * Obtener ID del paciente en sesión
     */
    fun getSessionPatientId(): Int? {
        val id = encryptedPrefs.getInt(KEY_PATIENT_ID, -1)
        return if (id > 0) id else null
    }

    /**
     * Guardar sesión
     */
    private fun saveSession(patientId: Int) {
        encryptedPrefs.edit().apply {
            putInt(KEY_PATIENT_ID, patientId)
            putBoolean(KEY_IS_LOGGED_IN, true)
            apply()
        }
    }

    /**
     * Limpiar sesión
     */
    private fun clearSession() {
        encryptedPrefs.edit().clear().apply()
    }

    /**
     * Encriptar contraseña con SHA-256
     */
    private fun hashPassword(password: String): String {
        val bytes = password.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        return digest.fold("") { str, it -> str + "%02x".format(it) }
    }

    /**
     * Validar formato de email
     */
    fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    /**
     * Validar formato de teléfono (Perú)
     */
    fun isValidPhone(phone: String): Boolean {
        val cleanPhone = phone.replace(Regex("[\\s-]"), "")
        return cleanPhone.matches(Regex("^(\\+51)?[9][0-9]{8}\$"))
    }

    /**
     * Validar DNI (Perú - 8 dígitos)
     */
    fun isValidDNI(dni: String): Boolean {
        return dni.matches(Regex("^[0-9]{8}\$"))
    }

    /**
     * Validar fortaleza de contraseña
     */
    fun isStrongPassword(password: String): Boolean {
        return password.length >= 8 &&
                password.any { it.isUpperCase() } &&
                password.any { it.isLowerCase() } &&
                password.any { it.isDigit() }
    }
}