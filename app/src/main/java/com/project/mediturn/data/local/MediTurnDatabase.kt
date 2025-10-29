package com.project.mediturn.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.project.mediturn.data.local.converters.DateTimeConverters
import com.project.mediturn.data.local.dao.*
import com.project.mediturn.data.local.entity.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Base de datos principal de MediTurn usando Room
 */
@Database(
    entities = [
        PatientEntity::class,
        DoctorEntity::class,
        TimeSlotEntity::class,
        AppointmentEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(DateTimeConverters::class)
abstract class MediTurnDatabase : RoomDatabase() {

    // DAOs
    abstract fun patientDao(): PatientDao
    abstract fun doctorDao(): DoctorDao
    abstract fun timeSlotDao(): TimeSlotDao
    abstract fun appointmentDao(): AppointmentDao

    companion object {
        @Volatile
        private var INSTANCE: MediTurnDatabase? = null

        fun getDatabase(context: Context): MediTurnDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MediTurnDatabase::class.java,
                    "mediturn_database"
                )
                    .addCallback(DatabaseCallback())
                    .fallbackToDestructiveMigration()
                    .build()
                
                INSTANCE = instance
                instance
            }
        }

        /**
         * Callback para poblar la BD con datos iniciales
         */
        private class DatabaseCallback : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    CoroutineScope(Dispatchers.IO).launch {
                        populateDatabase(database)
                    }
                }
            }
        }

        /**
         * Poblar BD con datos de prueba
         */
        private suspend fun populateDatabase(database: MediTurnDatabase) {
            val doctorDao = database.doctorDao()
            val timeSlotDao = database.timeSlotDao()

            // Insertar doctores de prueba
            val doctors = listOf(
                DoctorEntity(
                    id = 1,
                    name = "Dr. Carlos Mendoza",
                    specialty = "Cardiología",
                    department = "Medicina Interna",
                    description = "Especialista en enfermedades cardiovasculares con más de 15 años de experiencia.",
                    yearsOfExperience = 15,
                    rating = 4.8f,
                    reviewCount = 245,
                    photoUrl = "https://i.pravatar.cc/150?img=12",
                    collegiateCertificate = "CMP 45678",
                    availableForTeleconsultation = true,
                    city = "Lima",
                    consultationPrice = 150.0
                ),
                DoctorEntity(
                    id = 2,
                    name = "Dra. María Fernández",
                    specialty = "Dermatología",
                    department = "Dermatología y Estética",
                    description = "Dermatóloga especializada en tratamientos estéticos y dermatología clínica.",
                    yearsOfExperience = 10,
                    rating = 4.9f,
                    reviewCount = 312,
                    photoUrl = "https://i.pravatar.cc/150?img=47",
                    collegiateCertificate = "CMP 56789",
                    availableForTeleconsultation = true,
                    city = "Lima",
                    consultationPrice = 120.0
                ),
                DoctorEntity(
                    id = 3,
                    name = "Dr. Juan Pérez",
                    specialty = "Pediatría",
                    department = "Pediatría General",
                    description = "Pediatra con amplia experiencia en el cuidado de niños de todas las edades.",
                    yearsOfExperience = 12,
                    rating = 4.7f,
                    reviewCount = 189,
                    photoUrl = "https://i.pravatar.cc/150?img=33",
                    collegiateCertificate = "CMP 34567",
                    availableForTeleconsultation = false,
                    city = "Lima",
                    consultationPrice = 100.0
                ),
                DoctorEntity(
                    id = 4,
                    name = "Dra. Ana Torres",
                    specialty = "Neurología",
                    department = "Neurociencias",
                    description = "Neuróloga especializada en trastornos del sueño, migrañas y enfermedades neurodegenerativas.",
                    yearsOfExperience = 18,
                    rating = 4.9f,
                    reviewCount = 278,
                    photoUrl = "https://i.pravatar.cc/150?img=45",
                    collegiateCertificate = "CMP 67890",
                    availableForTeleconsultation = true,
                    city = "Callao",
                    consultationPrice = 180.0
                ),
                DoctorEntity(
                    id = 5,
                    name = "Dr. Roberto Sánchez",
                    specialty = "Traumatología",
                    department = "Traumatología y Ortopedia",
                    description = "Traumatólogo especializado en cirugía de rodilla, cadera y columna vertebral.",
                    yearsOfExperience = 20,
                    rating = 4.6f,
                    reviewCount = 156,
                    photoUrl = "https://i.pravatar.cc/150?img=52",
                    collegiateCertificate = "CMP 12345",
                    availableForTeleconsultation = false,
                    city = "Lima",
                    consultationPrice = 200.0
                ),
                DoctorEntity(
                    id = 6,
                    name = "Dra. Patricia Rojas",
                    specialty = "Oftalmología",
                    department = "Oftalmología",
                    description = "Oftalmóloga con especialización en cirugía refractiva y catarata.",
                    yearsOfExperience = 14,
                    rating = 4.8f,
                    reviewCount = 203,
                    photoUrl = "https://i.pravatar.cc/150?img=48",
                    collegiateCertificate = "CMP 23456",
                    availableForTeleconsultation = true,
                    city = "Lima",
                    consultationPrice = 140.0
                )
            )

            doctorDao.insertDoctors(doctors)

            // Generar horarios para cada doctor
            val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
            val timeSlots = mutableListOf<TimeSlotEntity>()
            var slotId = 1

            doctors.forEach { doctor ->
                // Generar slots para los próximos 7 días
                for (day in 1..7) {
                    val date = LocalDateTime.now().plusDays(day.toLong())

                    // Slots de mañana (9:00 - 12:00)
                    for (hour in 9..11) {
                        timeSlots.add(
                            TimeSlotEntity(
                                id = slotId++,
                                doctorId = doctor.id,
                                dateTime = date.withHour(hour).withMinute(0).format(formatter),
                                isAvailable = (slotId % 3 != 0)
                            )
                        )
                        timeSlots.add(
                            TimeSlotEntity(
                                id = slotId++,
                                doctorId = doctor.id,
                                dateTime = date.withHour(hour).withMinute(30).format(formatter),
                                isAvailable = (slotId % 4 != 0)
                            )
                        )
                    }

                    // Slots de tarde (15:00 - 18:00)
                    for (hour in 15..17) {
                        timeSlots.add(
                            TimeSlotEntity(
                                id = slotId++,
                                doctorId = doctor.id,
                                dateTime = date.withHour(hour).withMinute(0).format(formatter),
                                isAvailable = (slotId % 3 != 0)
                            )
                        )
                        timeSlots.add(
                            TimeSlotEntity(
                                id = slotId++,
                                doctorId = doctor.id,
                                dateTime = date.withHour(hour).withMinute(30).format(formatter),
                                isAvailable = (slotId % 5 != 0)
                            )
                        )
                    }
                }
            }

            timeSlotDao.insertTimeSlots(timeSlots)
        }
    }
}
