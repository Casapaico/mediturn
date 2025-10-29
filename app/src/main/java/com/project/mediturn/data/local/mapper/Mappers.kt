package com.project.mediturn.data.local.mapper

import com.project.mediturn.data.local.entity.*
import com.project.mediturn.data.model.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Mappers para convertir entre Entity (Room) y Model (Domain)
 */

// ========== PATIENT MAPPERS ==========

fun PatientEntity.toModel(): Patient {
    return Patient(
        id = id,
        name = name,
        email = email,
        phone = phone,
        dni = dni
    )
}

fun Patient.toEntity(passwordHash: String = ""): PatientEntity {
    return PatientEntity(
        id = id,
        name = name,
        email = email,
        phone = phone,
        dni = dni,
        passwordHash = passwordHash,
        createdAt = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
        isActive = true
    )
}

// ========== DOCTOR MAPPERS ==========

fun DoctorEntity.toModel(availableSlots: List<TimeSlot> = emptyList()): Doctor {
    return Doctor(
        id = id,
        name = name,
        specialty = specialty,
        department = department,
        description = description,
        yearsOfExperience = yearsOfExperience,
        rating = rating,
        reviewCount = reviewCount,
        photoUrl = photoUrl,
        collegiateCertificate = collegiateCertificate,
        availableForTeleconsultation = availableForTeleconsultation,
        city = city,
        consultationPrice = consultationPrice,
        availableSlots = availableSlots
    )
}

fun Doctor.toEntity(): DoctorEntity {
    return DoctorEntity(
        id = id,
        name = name,
        specialty = specialty,
        department = department,
        description = description,
        yearsOfExperience = yearsOfExperience,
        rating = rating,
        reviewCount = reviewCount,
        photoUrl = photoUrl,
        collegiateCertificate = collegiateCertificate,
        availableForTeleconsultation = availableForTeleconsultation,
        city = city,
        consultationPrice = consultationPrice
    )
}

// ========== TIMESLOT MAPPERS ==========

fun TimeSlotEntity.toModel(): TimeSlot {
    return TimeSlot(
        id = id,
        dateTime = LocalDateTime.parse(dateTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME),
        isAvailable = isAvailable
    )
}

fun TimeSlot.toEntity(doctorId: Int): TimeSlotEntity {
    return TimeSlotEntity(
        id = id,
        doctorId = doctorId,
        dateTime = dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
        isAvailable = isAvailable
    )
}

// ========== APPOINTMENT MAPPERS ==========

fun AppointmentEntity.toModel(doctor: Doctor): Appointment {
    return Appointment(
        id = id,
        patientId = patientId,
        doctor = doctor,
        dateTime = LocalDateTime.parse(dateTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME),
        reason = reason,
        isTelemedicine = isTelemedicine,
        status = AppointmentStatus.valueOf(status),
        createdAt = LocalDateTime.parse(createdAt, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    )
}

fun Appointment.toEntity(): AppointmentEntity {
    return AppointmentEntity(
        id = id,
        patientId = patientId,
        doctorId = doctor.id,
        dateTime = dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
        reason = reason,
        isTelemedicine = isTelemedicine,
        status = status.name,
        createdAt = createdAt.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    )
}
