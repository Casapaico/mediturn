package com.project.mediturn.data.local

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import android.content.Context
import com.project.mediturn.data.model.Appointment
import com.project.mediturn.data.model.Doctor
import com.project.mediturn.data.model.Patient

@Database(
    entities = [Appointment::class, Doctor::class, Patient::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class MediTurnDatabase : RoomDatabase() {
    abstract fun appointmentDao(): AppointmentDao
    abstract fun doctorDao(): DoctorDao
    abstract fun patientDao(): PatientDao

    companion object {
        @Volatile
        private var INSTANCE: MediTurnDatabase? = null

        fun getDatabase(context: Context): MediTurnDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MediTurnDatabase::class.java,
                    "mediturn_database"
                ).fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}