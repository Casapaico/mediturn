package com.project.mediturn.data


import com.project.mediturn.data.model.Appointment
import com.project.mediturn.data.model.AppointmentStatus
import com.project.mediturn.data.model.Doctor
import com.project.mediturn.data.model.Specialty
import com.project.mediturn.data.model.TimeSlot
import java.util.Calendar
import java.util.Date

object DataSource {
    val specialties = listOf(
        Specialty(1, "Cardiología", "❤️"),
        Specialty(2, "Dermatología", "🔬"),
        Specialty(3, "Pediatría", "👶"),
        Specialty(4, "Neurología", "🧠"),
        Specialty(5, "Traumatología", "🦴"),
        Specialty(6, "Oftalmología", "👁️"),
        Specialty(7, "Ginecología", "🌸"),
        Specialty(8, "Psiquiatría", "🧘")
    )

    val doctors = listOf(
        Doctor(
            id = 1,
            name = "Dr. Carlos Mendoza",
            specialty = "Cardiología",
            department = "Cardiología Intervencionista",
            description = "Especialista en enfermedades del corazón con más de 15 años de experiencia.",
            yearsOfExperience = 15,
            rating = 4.8f,
            reviewCount = 124,
            photoUrl = "https://images.unsplash.com/photo-1612349317150-e413f6a5b16d?w=400",
            collegiateCertificate = "CMP 12345",
            availableForTeleconsultation = true,
            city = "Lima",
            consultationPrice = 120.0,
            availableSlots = generateTimeSlots()
        ),
        Doctor(
            id = 2,
            name = "Dra. María Fernández",
            specialty = "Dermatología",
            department = "Dermatología Clínica",
            description = "Dermatóloga con especialización en enfermedades de la piel.",
            yearsOfExperience = 10,
            rating = 4.6f,
            reviewCount = 89,
            photoUrl = "https://images.unsplash.com/photo-1559839734-2b71ea197ec2?w=400",
            collegiateCertificate = "CMP 23456",
            availableForTeleconsultation = true,
            city = "Lima",
            consultationPrice = 100.0,
            availableSlots = generateTimeSlots()
        ),
        Doctor(
            id = 3,
            name = "Dr. Juan Pérez",
            specialty = "Pediatría",
            department = "Pediatría General",
            description = "Pediatra especializado en atención infantil.",
            yearsOfExperience = 12,
            rating = 4.9f,
            reviewCount = 156,
            photoUrl = "https://images.unsplash.com/photo-1622253692010-333f2da6031d?w=400",
            collegiateCertificate = "CMP 34567",
            availableForTeleconsultation = false,
            city = "Arequipa",
            consultationPrice = 80.0,
            availableSlots = generateTimeSlots()
        ),
        Doctor(
            id = 4,
            name = "Dra. Ana Torres",
            specialty = "Neurología",
            department = "Neurología Clínica",
            description = "Neuróloga con experiencia en trastornos del sistema nervioso.",
            yearsOfExperience = 18,
            rating = 4.7f,
            reviewCount = 203,
            photoUrl = "https://images.unsplash.com/photo-1594824947933-d0501ba2fe65?w=400",
            collegiateCertificate = "CMP 45678",
            availableForTeleconsultation = true,
            city = "Lima",
            consultationPrice = 150.0,
            availableSlots = generateTimeSlots()
        ),
        Doctor(
            id = 5,
            name = "Dr. Roberto Sánchez",
            specialty = "Traumatología",
            department = "Traumatología y Ortopedia",
            description = "Traumatólogo especializado en cirugía articular.",
            yearsOfExperience = 14,
            rating = 4.5f,
            reviewCount = 98,
            photoUrl = "https://images.unsplash.com/photo-1582750433449-648ed127bb54?w=400",
            collegiateCertificate = "CMP 56789",
            availableForTeleconsultation = false,
            city = "Trujillo",
            consultationPrice = 130.0,
            availableSlots = generateTimeSlots()
        ),
        Doctor(
            id = 6,
            name = "Dra. Patricia Rojas",
            specialty = "Oftalmología",
            department = "Oftalmología General",
            description = "Oftalmóloga con experiencia en cirugía refractiva.",
            yearsOfExperience = 11,
            rating = 4.8f,
            reviewCount = 167,
            photoUrl = "https://images.unsplash.com/photo-1559757175-0eb30cd8c063?w=400",
            collegiateCertificate = "CMP 67890",
            availableForTeleconsultation = true,
            city = "Lima",
            consultationPrice = 110.0,
            availableSlots = generateTimeSlots()
        )
    )

    val appointments = listOf(
        Appointment(
            id = 1,
            patientId = 1,
            doctor = doctors[0],
            dateTime = getDate(1, 10, 0),
            reason = "Control cardíaco regular",
            isTelemedicine = false,
            status = AppointmentStatus.CONFIRMED,
            createdAt = getDate(-2, 14, 30)
        ),
        Appointment(
            id = 2,
            patientId = 1,
            doctor = doctors[1],
            dateTime = getDate(3, 15, 30),
            reason = "Consulta por dermatitis",
            isTelemedicine = true,
            status = AppointmentStatus.PENDING,
            createdAt = getDate(-1, 9, 0)
        ),
        Appointment(
            id = 3,
            patientId = 1,
            doctor = doctors[2],
            dateTime = getDate(-7, 11, 0),
            reason = "Control pediátrico anual",
            isTelemedicine = false,
            status = AppointmentStatus.COMPLETED,
            createdAt = getDate(-14, 16, 0)
        )
    )

    private fun getDate(daysFromNow: Int, hour: Int, minute: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, daysFromNow)
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.time
    }

    private fun generateTimeSlots(): List<TimeSlot> {
        val slots = mutableListOf<TimeSlot>()
        val calendar = Calendar.getInstance()
        val currentDate = calendar.time

        for (day in 0..6) {
            calendar.time = currentDate
            calendar.add(Calendar.DAY_OF_YEAR, day)

            for (hour in 9..16) {
                if (hour == 12) continue

                calendar.set(Calendar.HOUR_OF_DAY, hour)
                calendar.set(Calendar.MINUTE, 0)

                slots.add(
                    TimeSlot(
                        id = slots.size + 1,
                        dateTime = calendar.time,
                        isAvailable = (slots.size % 3) != 0
                    )
                )

                calendar.set(Calendar.MINUTE, 30)
                slots.add(
                    TimeSlot(
                        id = slots.size + 1,
                        dateTime = calendar.time,
                        isAvailable = (slots.size % 4) != 0
                    )
                )
            }
        }
        return slots
    }

    fun getDoctorById(id: Int): Doctor? {
        return doctors.find { it.id == id }
    }

    fun getSpecialtyByName(name: String): Specialty? {
        return specialties.find { it.name == name }
    }
}