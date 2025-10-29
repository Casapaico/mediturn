package com.project.mediturn.data.local.dao

import androidx.room.*
import com.project.mediturn.data.local.entity.PatientEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO para operaciones de Pacientes
 */
@Dao
interface PatientDao {

    @Query("SELECT * FROM patients WHERE id = :id")
    suspend fun getPatientById(id: Int): PatientEntity?

    @Query("SELECT * FROM patients WHERE email = :email LIMIT 1")
    suspend fun getPatientByEmail(email: String): PatientEntity?

    @Query("SELECT * FROM patients WHERE dni = :dni LIMIT 1")
    suspend fun getPatientByDNI(dni: String): PatientEntity?

    @Query("SELECT * FROM patients WHERE isActive = 1")
    fun getAllPatientsFlow(): Flow<List<PatientEntity>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertPatient(patient: PatientEntity): Long

    @Update
    suspend fun updatePatient(patient: PatientEntity)

    @Delete
    suspend fun deletePatient(patient: PatientEntity)

    @Query("UPDATE patients SET isActive = 0 WHERE id = :patientId")
    suspend fun deactivatePatient(patientId: Int)

    @Query("SELECT EXISTS(SELECT 1 FROM patients WHERE email = :email)")
    suspend fun emailExists(email: String): Boolean

    @Query("SELECT EXISTS(SELECT 1 FROM patients WHERE dni = :dni)")
    suspend fun dniExists(dni: String): Boolean
}
