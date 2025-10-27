package com.project.mediturn.data.repository

import com.project.mediturn.data.local.DoctorDao
import com.project.mediturn.data.model.Doctor
import kotlinx.coroutines.flow.Flow

class DoctorRepository(private val doctorDao: DoctorDao) {
    fun getAllDoctors(): Flow<List<Doctor>> {
        return doctorDao.getAllDoctors()
    }

    suspend fun getDoctorById(id: Int): Doctor? {
        return doctorDao.getDoctorById(id)
    }

    fun getDoctorsBySpecialty(specialty: String): Flow<List<Doctor>> {
        return doctorDao.getDoctorsBySpecialty(specialty)
    }

    fun searchDoctors(query: String): Flow<List<Doctor>> {
        return doctorDao.searchDoctors(query)
    }

    suspend fun insertDoctor(doctor: Doctor) {
        doctorDao.insertDoctor(doctor)
    }

    suspend fun insertAllDoctors(doctors: List<Doctor>) {
        doctorDao.insertAll(doctors)
    }

    fun getAllSpecialties(): Flow<List<String>> {
        return doctorDao.getAllSpecialties()
    }
}