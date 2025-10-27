package com.project.mediturn.data.local

import androidx.room.*
import com.project.mediturn.data.model.Appointment
import com.project.mediturn.data.model.AppointmentWithDoctor
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface AppointmentDao {
    @Query("SELECT * FROM appointment ORDER BY dateTime DESC")
    fun getAllAppointments(): Flow<List<Appointment>>

    @Query("SELECT * FROM appointment WHERE patientId = :patientId ORDER BY dateTime DESC")
    fun getAppointmentsByPatient(patientId: Int): Flow<List<Appointment>>

    @Query("SELECT * FROM appointment WHERE id = :id")
    suspend fun getAppointmentById(id: Int): Appointment?

    @Transaction
    @Query("""
        SELECT * FROM appointment 
        INNER JOIN doctor ON appointment.doctorId = doctor.id 
        WHERE appointment.patientId = :patientId 
        ORDER BY appointment.dateTime DESC
    """)
    fun getAppointmentsWithDoctor(patientId: Int): Flow<List<AppointmentWithDoctor>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAppointment(appointment: Appointment)

    @Update
    suspend fun updateAppointment(appointment: Appointment)

    @Delete
    suspend fun deleteAppointment(appointment: Appointment)

    @Query("DELETE FROM appointment WHERE id = :id")
    suspend fun deleteAppointmentById(id: Int)

    @Query("SELECT * FROM appointment WHERE dateTime > :currentTime AND status != 'CANCELLED'")
    fun getUpcomingAppointments(currentTime: Date): Flow<List<Appointment>>

    @Query("SELECT * FROM appointment WHERE dateTime <= :currentTime OR status = 'CANCELLED'")
    fun getPastAppointments(currentTime: Date): Flow<List<Appointment>>
}