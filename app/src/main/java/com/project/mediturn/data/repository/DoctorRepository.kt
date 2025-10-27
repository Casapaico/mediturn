package com.project.mediturn.data.repository

import com.project.mediturn.data.DataSource
import com.project.mediturn.data.model.Doctor
import com.project.mediturn.data.model.Specialty
import kotlinx.coroutines.delay

class DoctorRepository {
    suspend fun getAllDoctors(): List<Doctor> {
        delay(1000) // Simular delay de red
        return DataSource.doctors
    }

    suspend fun getDoctorsBySpecialty(specialty: String): List<Doctor> {
        delay(500)
        return DataSource.doctors.filter { it.specialty == specialty }
    }

    suspend fun searchDoctors(query: String): List<Doctor> {
        delay(300)
        return DataSource.doctors.filter { doctor ->
            doctor.name.contains(query, ignoreCase = true) ||
                    doctor.specialty.contains(query, ignoreCase = true) ||
                    doctor.city.contains(query, ignoreCase = true)
        }
    }

    fun getAllSpecialties(): List<Specialty> {
        return DataSource.specialties
    }

    suspend fun getDoctorById(id: Int): Doctor? {
        delay(200)
        return DataSource.doctors.find { it.id == id }
    }
}