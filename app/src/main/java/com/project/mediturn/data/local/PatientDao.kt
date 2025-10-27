package com.project.mediturn.data.local

import androidx.room.*
import com.project.mediturn.data.model.Patient
import kotlinx.coroutines.flow.Flow

@Dao
interface PatientDao {
    @Query("SELECT * FROM patient")
    fun getAllPatients(): Flow<List<Patient>>

    @Query("SELECT * FROM patient WHERE id = :id")
    suspend fun getPatientById(id: Int): Patient?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPatient(patient: Patient)

    @Update
    suspend fun updatePatient(patient: Patient)

    @Delete
    suspend fun deletePatient(patient: Patient)
}