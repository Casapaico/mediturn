package com.project.mediturn.util

object Constants {

    // App Info
    const val APP_NAME = "MediTurn"
    const val APP_VERSION = "1.0.0"

    // Date & Time Formats
    const val DATE_FORMAT_FULL = "dd MMM yyyy"
    const val TIME_FORMAT = "HH:mm"
    const val DATETIME_FORMAT = "dd/MM/yyyy HH:mm"
    const val DATE_FORMAT_SHORT = "dd/MM/yyyy"

    // Appointment Constraints
    const val MIN_REASON_LENGTH = 10
    const val MAX_REASON_LENGTH = 200
    const val DAYS_AVAILABLE_FOR_BOOKING = 7
    const val MIN_ADVANCE_HOURS = 2 // Mínimo 2 horas de anticipación

    // Working Hours
    const val MORNING_START_HOUR = 9
    const val MORNING_END_HOUR = 12
    const val AFTERNOON_START_HOUR = 15
    const val AFTERNOON_END_HOUR = 18
    const val SLOT_DURATION_MINUTES = 30

    // Search
    const val SEARCH_DEBOUNCE_MS = 300L
    const val MIN_SEARCH_QUERY_LENGTH = 2

    // Network (for future API integration)
    const val BASE_URL = "https://api.mediturn.com/"
    const val NETWORK_TIMEOUT_SECONDS = 30L

    // Validation Messages
    const val ERROR_EMPTY_FIELD = "Este campo es obligatorio"
    const val ERROR_INVALID_EMAIL = "Email inválido"
    const val ERROR_INVALID_PHONE = "Teléfono inválido"
    const val ERROR_INVALID_DNI = "DNI inválido (8 dígitos)"
    const val ERROR_SHORT_REASON = "Motivo muy corto (mínimo $MIN_REASON_LENGTH caracteres)"
    const val ERROR_LONG_REASON = "Motivo muy largo (máximo $MAX_REASON_LENGTH caracteres)"
    const val ERROR_NO_DATE_SELECTED = "Seleccione una fecha"
    const val ERROR_NO_TIME_SELECTED = "Seleccione un horario"
    const val ERROR_SLOT_NOT_AVAILABLE = "Horario no disponible"
    const val ERROR_PAST_DATE = "No puede agendar en fechas pasadas"
    const val ERROR_MIN_ADVANCE = "Debe agendar con al menos $MIN_ADVANCE_HOURS horas de anticipación"

    // Success Messages
    const val SUCCESS_APPOINTMENT_CREATED = "Cita agendada exitosamente"
    const val SUCCESS_APPOINTMENT_CANCELLED = "Cita cancelada correctamente"
    const val SUCCESS_APPOINTMENT_RESCHEDULED = "Cita reprogramada exitosamente"
    const val SUCCESS_APPOINTMENT_CONFIRMED = "Cita confirmada"

    // Confirmation Messages
    const val CONFIRM_CANCEL_APPOINTMENT = "¿Está seguro de cancelar esta cita?"
    const val CONFIRM_RESCHEDULE_APPOINTMENT = "¿Desea reprogramar esta cita?"

    // Specialties Icons (emojis)
    val SPECIALTY_ICONS = mapOf(
        "Cardiología" to "❤️",
        "Dermatología" to "🧴",
        "Pediatría" to "👶",
        "Neurología" to "🧠",
        "Traumatología" to "🦴",
        "Oftalmología" to "👁️",
        "Ginecología" to "🌸",
        "Psiquiatría" to "🧘"
    )

    // Default Values
    const val DEFAULT_PATIENT_ID = 1
    const val DEFAULT_DOCTOR_PHOTO = "https://i.pravatar.cc/150?img="

    // Preferences Keys (for SharedPreferences)
    const val PREF_USER_ID = "user_id"
    const val PREF_USER_NAME = "user_name"
    const val PREF_USER_EMAIL = "user_email"
    const val PREF_IS_LOGGED_IN = "is_logged_in"
}