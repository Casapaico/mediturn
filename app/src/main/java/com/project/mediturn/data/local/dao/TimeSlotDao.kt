package com.project.mediturn.data.local.dao

import androidx.room.*
import com.project.mediturn.data.local.entity.TimeSlotEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO para operaciones de TimeSlots
 */
@Dao
interface TimeSlotDao {

    @Query("SELECT * FROM time_slots WHERE doctorId = :doctorId")
    suspend fun getTimeSlotsByDoctor(doctorId: Int): List<TimeSlotEntity>

    @Query("SELECT * FROM time_slots WHERE doctorId = :doctorId AND isAvailable = 1")
    suspend fun getAvailableTimeSlots(doctorId: Int): List<TimeSlotEntity>

    @Query("SELECT * FROM time_slots WHERE doctorId = :doctorId")
    fun getTimeSlotsByDoctorFlow(doctorId: Int): Flow<List<TimeSlotEntity>>

    @Query("SELECT * FROM time_slots WHERE id = :id")
    suspend fun getTimeSlotById(id: Int): TimeSlotEntity?

    @Query("""
        SELECT * FROM time_slots 
        WHERE doctorId = :doctorId 
        AND dateTime >= :startDate 
        AND dateTime <= :endDate
        AND isAvailable = 1
        ORDER BY dateTime
    """)
    suspend fun getAvailableTimeSlotsBetweenDates(
        doctorId: Int,
        startDate: String,
        endDate: String
    ): List<TimeSlotEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTimeSlot(timeSlot: TimeSlotEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTimeSlots(timeSlots: List<TimeSlotEntity>)

    @Update
    suspend fun updateTimeSlot(timeSlot: TimeSlotEntity)

    @Query("UPDATE time_slots SET isAvailable = 0 WHERE id = :timeSlotId")
    suspend fun markTimeSlotAsUnavailable(timeSlotId: Int)

    @Query("UPDATE time_slots SET isAvailable = 1 WHERE id = :timeSlotId")
    suspend fun markTimeSlotAsAvailable(timeSlotId: Int)

    @Delete
    suspend fun deleteTimeSlot(timeSlot: TimeSlotEntity)

    @Query("DELETE FROM time_slots WHERE doctorId = :doctorId")
    suspend fun deleteTimeSlotsByDoctor(doctorId: Int)

    @Query("DELETE FROM time_slots")
    suspend fun deleteAllTimeSlots()

    @Query("SELECT COUNT(*) FROM time_slots WHERE doctorId = :doctorId AND isAvailable = 1")
    suspend fun getAvailableSlotsCount(doctorId: Int): Int
}
