package com.project.mediturn.data.repository

import android.content.Context
import com.project.mediturn.data.local.MediTurnDatabase
import com.project.mediturn.data.local.mapper.toModel
import com.project.mediturn.data.model.Doctor
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DoctorRepository(context: Context) {

    private val database = MediTurnDatabase.getDatabase(context)
    private val doctorDao = database.doctorDao()
    private val timeSlotDao = database.timeSlotDao()

    /**
     * Obtener todos los médicos
     */
    suspend fun getAllDoctors(): Result<List<Doctor>> {
        return try {
            delay(500) // Simular latencia
            val entities = doctorDao.getAllDoctors()
            val doctors = entities.map { entity ->
                val slots = timeSlotDao.getTimeSlotsByDoctor(entity.id)
                    .map { it.toModel() }
                entity.toModel(slots)
            }
            Result.success(doctors)
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
            delay(300)
            val entities = doctorDao.searchDoctorsWithFilters(
                query = query,
                specialty = specialty,
                city = city,
                telemedicine = telemedicine
            )
            val doctors = entities.map { entity ->
                val slots = timeSlotDao.getAvailableTimeSlots(entity.id)
                    .map { it.toModel() }
                entity.toModel(slots)
            }
            Result.success(doctors)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Obtener médico por ID
     */
    suspend fun getDoctorById(id: Int): Result<Doctor> {
        return try {
            delay(200)
            val entity = doctorDao.getDoctorById(id)
            if (entity != null) {
                val slots = timeSlotDao.getAvailableTimeSlots(id)
                    .map { it.toModel() }
                Result.success(entity.toModel(slots))
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
            delay(300)
            val entities = doctorDao.getDoctorsBySpecialty(specialty)
            val doctors = entities.map { entity ->
                val slots = timeSlotDao.getAvailableTimeSlots(entity.id)
                    .map { it.toModel() }
                entity.toModel(slots)
            }
            Result.success(doctors)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Flow para búsqueda en tiempo real
     */
    fun searchDoctorsFlow(query: String): Flow<List<Doctor>> = flow {
        delay(300)
        val entities = doctorDao.searchDoctors(query)
        val doctors = entities.map { entity ->
            val slots = timeSlotDao.getAvailableTimeSlots(entity.id)
                .map { it.toModel() }
            entity.toModel(slots)
        }
        emit(doctors)
    }

    /**
     * Verificar disponibilidad de médico
     */
    suspend fun checkDoctorAvailability(doctorId: Int): Result<Boolean> {
        return try {
            delay(200)
            val count = timeSlotDao.getAvailableSlotsCount(doctorId)
            Result.success(count > 0)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}