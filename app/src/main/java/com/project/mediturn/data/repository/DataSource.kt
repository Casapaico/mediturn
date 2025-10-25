package com.project.mediturn.data

import com.project.mediturn.data.model.*
import java.time.LocalDateTime

object DataSource {
    
    // ========== ESPECIALIDADES ==========
    val specialties = listOf(
        Specialty(1, "Cardiología", "❤️"),
        Specialty(2, "Dermatología", "🧴"),
        Specialty(3, "Pediatría", "👶"),
        Specialty(4, "Neurología", "🧠"),
        Specialty(5, "Traumatología", "🦴"),
        Specialty(6, "Oftalmología", "👁️"),
        Specialty(7, "Ginecología", "🌸"),
        Specialty(8, "Psiquiatría", "🧘")
    )

    // ========== MÉDICOS ==========
    val doctors = listOf(
        Doctor(
            id = 1,
            name = "Dr. Carlos Mendoza",
            specialty = "Cardiología",
            department = "Medicina Interna",
            description = "Especialista en enfermedades cardiovasculares con más de 15 años de experiencia. Certificado por el Colegio Médico del Perú.",
            yearsOfExperience = 15,
            rating = 4.8f,
            reviewCount = 245,
            photoUrl = "https://i.pravatar.cc/150?img=12",
            collegiateCertificate = "CMP 45678",
            availableForTeleconsultation = true,
            city = "Lima",
            consultationPrice = 150.0,
            availableSlots = generateTimeSlots()
        ),
        Doctor(
            id = 2,
            name = "Dra. María Fernández",
            specialty = "Dermatología",
            department = "Dermatología y Estética",
            description = "Dermatóloga especializada en tratamientos estéticos y dermatología clínica. Experta en láser y procedimientos mínimamente invasivos.",
            yearsOfExperience = 10,
            rating = 4.9f,
            reviewCount = 312,
            photoUrl = "https://i.pravatar.cc/150?img=47",
            collegiateCertificate = "CMP 56789",
            availableForTeleconsultation = true,
            city = "Lima",
            consultationPrice = 120.0,
            availableSlots = generateTimeSlots()
        ),
        Doctor(
            id = 3,
            name = "Dr. Juan Pérez",
            specialty = "Pediatría",
            department = "Pediatría General",
            description = "Pediatra con amplia experiencia en el cuidado de niños de todas las edades. Especializado en medicina preventiva.",
            yearsOfExperience = 12,
            rating = 4.7f,
            reviewCount = 189,
            photoUrl = "https://i.pravatar.cc/150?img=33",
            collegiateCertificate = "CMP 34567",
            availableForTeleconsultation = false,
            city = "Lima",
            consultationPrice = 100.0,
            availableSlots = generateTimeSlots()
        ),
        Doctor(
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
            consultationPrice = 180.0,
            availableSlots = generateTimeSlots()
        ),
        Doctor(
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
            consultationPrice = 200.0,
            availableSlots = generateTimeSlots()
        ),
        Doctor(
            id = 6,
            name = "Dra. Patricia Rojas",
            specialty = "Oftalmología",
            department = "Oftalmología",
            description = "Oftalmóloga con especialización en cirugía refractiva y catarata. Certificada en láser.",
            yearsOfExperience = 14,
            rating = 4.8f,
            reviewCount = 203,
            photoUrl = "https://i.pravatar.cc/150?img=48",
            collegiateCertificate = "CMP 23456",
            availableForTeleconsultation = true,
            city = "Lima",
            consultationPrice = 140.0,
            availableSlots = generateTimeSlots()
        )
    )

    // ========== PACIENTE ACTUAL (simulado) ==========
    val currentPatient = Patient(
        id = 1,
        name = "Alex Casapaico",
        email = "alex.casapaico@email.com",
        phone = "+51 999 888 777",
        dni = "72345678"
    )

    // ========== CITAS DEL PACIENTE ==========
    val appointments = mutableListOf(
        Appointment(
            id = 1,
            patientId = 1,
            doctor = doctors[0],
            dateTime = LocalDateTime.now().plusDays(2).withHour(10).withMinute(0),
            reason = "Control de presión arterial",
            isTelemedicine = false,
            status = AppointmentStatus.CONFIRMED,
            createdAt = LocalDateTime.now().minusDays(5)
        ),
        Appointment(
            id = 2,
            patientId = 1,
            doctor = doctors[1],
            dateTime = LocalDateTime.now().plusDays(5).withHour(15).withMinute(30),
            reason = "Consulta dermatológica - Acné",
            isTelemedicine = true,
            status = AppointmentStatus.PENDING,
            createdAt = LocalDateTime.now().minusDays(2)
        ),
        Appointment(
            id = 3,
            patientId = 1,
            doctor = doctors[3],
            dateTime = LocalDateTime.now().minusDays(10).withHour(9).withMinute(0),
            reason = "Consulta por migrañas recurrentes",
            isTelemedicine = false,
            status = AppointmentStatus.COMPLETED,
            createdAt = LocalDateTime.now().minusDays(15)
        )
    )

    // ========== FUNCIONES AUXILIARES ==========
    
    private fun generateTimeSlots(): List<TimeSlot> {
        val slots = mutableListOf<TimeSlot>()
        var id = 1
        
        // Generar slots para los próximos 7 días
        for (day in 1..7) {
            val date = LocalDateTime.now().plusDays(day.toLong())
            
            // Slots de mañana (9:00 - 12:00)
            for (hour in 9..11) {
                slots.add(
                    TimeSlot(
                        id = id++,
                        dateTime = date.withHour(hour).withMinute(0),
                        isAvailable = (id % 3 != 0) // Simular disponibilidad
                    )
                )
                slots.add(
                    TimeSlot(
                        id = id++,
                        dateTime = date.withHour(hour).withMinute(30),
                        isAvailable = (id % 4 != 0)
                    )
                )
            }
            
            // Slots de tarde (15:00 - 18:00)
            for (hour in 15..17) {
                slots.add(
                    TimeSlot(
                        id = id++,
                        dateTime = date.withHour(hour).withMinute(0),
                        isAvailable = (id % 3 != 0)
                    )
                )
                slots.add(
                    TimeSlot(
                        id = id++,
                        dateTime = date.withHour(hour).withMinute(30),
                        isAvailable = (id % 5 != 0)
                    )
                )
            }
        }
        
        return slots
    }

    // Funciones de búsqueda
    fun getDoctorById(id: Int): Doctor? = doctors.find { it.id == id }
    
    fun getAppointmentById(id: Int): Appointment? = appointments.find { it.id == id }
    
    fun searchDoctors(
        query: String = "",
        specialty: String? = null,
        city: String? = null,
        telemedicine: Boolean? = null
    ): List<Doctor> {
        return doctors.filter { doctor ->
            val matchesQuery = query.isEmpty() || 
                doctor.name.contains(query, ignoreCase = true) ||
                doctor.specialty.contains(query, ignoreCase = true)
            
            val matchesSpecialty = specialty == null || doctor.specialty == specialty
            val matchesCity = city == null || doctor.city == city
            val matchesTelemedicine = telemedicine == null || doctor.availableForTeleconsultation == telemedicine
            
            matchesQuery && matchesSpecialty && matchesCity && matchesTelemedicine
        }
    }
    
    fun getUpcomingAppointments(): List<Appointment> {
        return appointments
            .filter { it.dateTime.isAfter(LocalDateTime.now()) }
            .sortedBy { it.dateTime }
    }
    
    fun getPastAppointments(): List<Appointment> {
        return appointments
            .filter { it.dateTime.isBefore(LocalDateTime.now()) }
            .sortedByDescending { it.dateTime }
    }
}