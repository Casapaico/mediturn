package com.project.mediturn.data.repository

import com.project.mediturn.data.DataSource
import com.project.mediturn.data.model.Doctor
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DoctorRepository {

    /**
     * Obtener todos los médicos
     * Simula delay de red
     */
    suspend fun getAllDoctors(): Result<List<Doctor>> {
        return try {
            delay(800) // Simular latencia de red
            Result.success(DataSource.doctors)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Buscar médicos con filtros
     */
    suspend fun searchDoctors(
        query: String = "",
        specialty: String? = null,
        city: String? = null,
        telemedicine: Boolean? = null
    ): Result<List<Doctor>> {
        return try {
            delay(500)
            val results = DataSource.searchDoctors(
                query = query,
                specialty = specialty,
                city = city,
                telemedicine = telemedicine
            )
            Result.success(results)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Obtener médico por ID
     */
    suspend fun getDoctorById(id: Int): Result<Doctor> {
        return try {
            delay(300)
            val doctor = DataSource.getDoctorById(id)
            if (doctor != null) {
                Result.success(doctor)
            } else {
                Result.failure(Exception("Médico no encontrado"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Obtener médicos por especialidad
     */
    suspend fun getDoctorsBySpecialty(specialty: String): Result<List<Doctor>> {
        return try {
            delay(400)
            val results = DataSource.doctors.filter { it.specialty == specialty }
            Result.success(results)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Flow para búsqueda en tiempo real
     */
    fun searchDoctorsFlow(query: String): Flow<List<Doctor>> = flow {
        delay(300) // Debounce
        val results = DataSource.searchDoctors(query = query)
        emit(results)
    }

    /**
     * Verificar disponibilidad de médico
     */
    suspend fun checkDoctorAvailability(doctorId: Int): Result<Boolean> {
        return try {
            delay(200)
            val doctor = DataSource.getDoctorById(doctorId)
            val hasAvailableSlots = doctor?.availableSlots?.any { it.isAvailable } ?: false
            Result.success(hasAvailableSlots)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}