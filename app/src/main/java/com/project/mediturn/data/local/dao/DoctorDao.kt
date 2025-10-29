package com.project.mediturn.data.local.dao

import androidx.room.*
import com.project.mediturn.data.local.entity.DoctorEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO para operaciones de Doctores
 */
@Dao
interface DoctorDao {

    @Query("SELECT * FROM doctors")
    suspend fun getAllDoctors(): List<DoctorEntity>

    @Query("SELECT * FROM doctors")
    fun getAllDoctorsFlow(): Flow<List<DoctorEntity>>

    @Query("SELECT * FROM doctors WHERE id = :id")
    suspend fun getDoctorById(id: Int): DoctorEntity?

    @Query("SELECT * FROM doctors WHERE specialty = :specialty")
    suspend fun getDoctorsBySpecialty(specialty: String): List<DoctorEntity>

    @Query("SELECT * FROM doctors WHERE city = :city")
    suspend fun getDoctorsByCity(city: String): List<DoctorEntity>

    @Query("SELECT * FROM doctors WHERE availableForTeleconsultation = 1")
    suspend fun getDoctorsWithTelemedicine(): List<DoctorEntity>

    @Query("""
        SELECT * FROM doctors 
        WHERE (name LIKE '%' || :query || '%' 
            OR specialty LIKE '%' || :query || '%')
    """)
    suspend fun searchDoctors(query: String): List<DoctorEntity>

    @Query("""
        SELECT * FROM doctors 
        WHERE (name LIKE '%' || :query || '%' OR specialty LIKE '%' || :query || '%')
        AND (:specialty IS NULL OR specialty = :specialty)
        AND (:city IS NULL OR city = :city)
        AND (:telemedicine IS NULL OR availableForTeleconsultation = :telemedicine)
    """)
    suspend fun searchDoctorsWithFilters(
        query: String,
        specialty: String?,
        city: String?,
        telemedicine: Boolean?
    ): List<DoctorEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDoctor(doctor: DoctorEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDoctors(doctors: List<DoctorEntity>)

    @Update
    suspend fun updateDoctor(doctor: DoctorEntity)

    @Delete
    suspend fun deleteDoctor(doctor: DoctorEntity)

    @Query("DELETE FROM doctors")
    suspend fun deleteAllDoctors()

    @Query("SELECT COUNT(*) FROM doctors")
    suspend fun getDoctorCount(): Int

    @Query("SELECT DISTINCT specialty FROM doctors ORDER BY specialty")
    suspend fun getAllSpecialties(): List<String>

    @Query("SELECT DISTINCT city FROM doctors ORDER BY city")
    suspend fun getAllCities(): List<String>
}
