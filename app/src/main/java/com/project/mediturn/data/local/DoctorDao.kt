package com.project.mediturn.data.local

import androidx.room.*
import com.project.mediturn.data.model.Doctor
import kotlinx.coroutines.flow.Flow

@Dao
interface DoctorDao {
    @Query("SELECT * FROM doctor")
    fun getAllDoctors(): Flow<List<Doctor>>

    @Query("SELECT * FROM doctor WHERE id = :id")
    suspend fun getDoctorById(id: Int): Doctor?

    @Query("SELECT * FROM doctor WHERE specialty = :specialty")
    fun getDoctorsBySpecialty(specialty: String): Flow<List<Doctor>>

    @Query("SELECT * FROM doctor WHERE name LIKE '%' || :query || '%' OR specialty LIKE '%' || :query || '%' OR city LIKE '%' || :query || '%'")
    fun searchDoctors(query: String): Flow<List<Doctor>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDoctor(doctor: Doctor)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(doctors: List<Doctor>)

    @Update
    suspend fun updateDoctor(doctor: Doctor)

    @Delete
    suspend fun deleteDoctor(doctor: Doctor)

    @Query("SELECT DISTINCT specialty FROM doctor")
    fun getAllSpecialties(): Flow<List<String>>
}