package com.project.mediturn.data.local.dao

import androidx.room.*
import com.project.mediturn.data.local.entity.AppointmentEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO para operaciones de Citas
 */
@Dao
interface AppointmentDao {

    @Query("SELECT * FROM appointments")
    suspend fun getAllAppointments(): List<AppointmentEntity>

    @Query("SELECT * FROM appointments WHERE patientId = :patientId ORDER BY dateTime DESC")
    fun getAppointmentsByPatientFlow(patientId: Int): Flow<List<AppointmentEntity>>

    @Query("SELECT * FROM appointments WHERE patientId = :patientId ORDER BY dateTime DESC")
    suspend fun getAppointmentsByPatient(patientId: Int): List<AppointmentEntity>

    @Query("SELECT * FROM appointments WHERE id = :id")
    suspend fun getAppointmentById(id: Int): AppointmentEntity?

    @Query("""
        SELECT * FROM appointments 
        WHERE patientId = :patientId 
        AND dateTime >= :currentDateTime
        AND status IN ('PENDING', 'CONFIRMED')
        ORDER BY dateTime
    """)
    suspend fun getUpcomingAppointments(patientId: Int, currentDateTime: String): List<AppointmentEntity>

    @Query("""
        SELECT * FROM appointments 
        WHERE patientId = :patientId 
        AND dateTime < :currentDateTime
        ORDER BY dateTime DESC
    """)
    suspend fun getPastAppointments(patientId: Int, currentDateTime: String): List<AppointmentEntity>

    @Query("""
        SELECT * FROM appointments 
        WHERE doctorId = :doctorId 
        AND dateTime >= :startDate 
        AND dateTime <= :endDate
        ORDER BY dateTime
    """)
    suspend fun getAppointmentsByDoctorBetweenDates(
        doctorId: Int,
        startDate: String,
        endDate: String
    ): List<AppointmentEntity>

    @Query("SELECT * FROM appointments WHERE status = :status")
    suspend fun getAppointmentsByStatus(status: String): List<AppointmentEntity>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertAppointment(appointment: AppointmentEntity): Long

    @Update
    suspend fun updateAppointment(appointment: AppointmentEntity)

    @Query("UPDATE appointments SET status = :status WHERE id = :appointmentId")
    suspend fun updateAppointmentStatus(appointmentId: Int, status: String)

    @Query("UPDATE appointments SET dateTime = :newDateTime WHERE id = :appointmentId")
    suspend fun rescheduleAppointment(appointmentId: Int, newDateTime: String)

    @Delete
    suspend fun deleteAppointment(appointment: AppointmentEntity)

    @Query("DELETE FROM appointments WHERE patientId = :patientId")
    suspend fun deleteAppointmentsByPatient(patientId: Int)

    @Query("DELETE FROM appointments")
    suspend fun deleteAllAppointments()

    @Query("SELECT COUNT(*) FROM appointments WHERE patientId = :patientId")
    suspend fun getAppointmentCountByPatient(patientId: Int): Int

    @Query("""
        SELECT COUNT(*) FROM appointments 
        WHERE doctorId = :doctorId 
        AND dateTime = :dateTime
        AND status IN ('PENDING', 'CONFIRMED')
    """)
    suspend fun isTimeSlotBooked(doctorId: Int, dateTime: String): Int
}
