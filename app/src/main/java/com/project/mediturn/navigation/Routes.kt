package com.project.mediturn.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Home : Screen("home")
    object DoctorList : Screen("doctor_list")
    object DoctorDetail : Screen("doctor_detail/{doctorId}") {
        fun createRoute(doctorId: Int) = "doctor_detail/$doctorId"
    }
    object BookAppointment : Screen("book_appointment/{doctorId}") {
        fun createRoute(doctorId: Int) = "book_appointment/$doctorId"
    }
    object MyAppointments : Screen("my_appointments")
    object AppointmentDetail : Screen("appointment_detail/{appointmentId}") {
        fun createRoute(appointmentId: Int) = "appointment_detail/$appointmentId"
    }
    object Profile : Screen("profile")
}