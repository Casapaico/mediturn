package com.project.mediturn.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.project.mediturn.ui.screens.auth.LoginScreen
import com.project.mediturn.ui.screens.auth.RegisterScreen
import com.project.mediturn.ui.screens.home.HomeScreen
import com.project.mediturn.ui.screens.doctors.DoctorListScreen
import com.project.mediturn.ui.screens.doctors.DoctorDetailScreen
import com.project.mediturn.ui.screens.appointments.BookAppointmentScreen
import com.project.mediturn.ui.screens.appointments.MyAppointmentsScreen
import com.project.mediturn.ui.screens.appointments.AppointmentDetailScreen
import com.project.mediturn.ui.screens.profile.ProfileScreen

@Composable
fun NavGraph(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        // ========== AUTENTICACIÓN ==========
        composable(Screen.Login.route) {
            LoginScreen(
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                },
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Register.route) {
            RegisterScreen(
                onNavigateToLogin = {
                    navController.popBackStack()
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        // ========== HOME ==========
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToSearch = {
                    navController.navigate(Screen.DoctorList.route)
                },
                onNavigateToAppointments = {
                    navController.navigate(Screen.MyAppointments.route)
                },
                onNavigateToProfile = {
                    navController.navigate(Screen.Profile.route)
                }
            )
        }

        // ========== MÉDICOS ==========
        composable(Screen.DoctorList.route) {
            DoctorListScreen(
                onDoctorClick = { doctorId ->
                    navController.navigate(Screen.DoctorDetail.createRoute(doctorId))
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = Screen.DoctorDetail.route,
            arguments = listOf(
                navArgument("doctorId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val doctorId = backStackEntry.arguments?.getInt("doctorId") ?: 0
            DoctorDetailScreen(
                doctorId = doctorId,
                onBookAppointment = {
                    navController.navigate(Screen.BookAppointment.createRoute(doctorId))
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        // ========== CITAS ==========
        composable(
            route = Screen.BookAppointment.route,
            arguments = listOf(
                navArgument("doctorId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val doctorId = backStackEntry.arguments?.getInt("doctorId") ?: 0
            BookAppointmentScreen(
                doctorId = doctorId,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onAppointmentBooked = {
                    navController.navigate(Screen.MyAppointments.route) {
                        popUpTo(Screen.Home.route)
                    }
                }
            )
        }

        composable(Screen.MyAppointments.route) {
            MyAppointmentsScreen(
                onAppointmentClick = { appointmentId ->
                    navController.navigate(Screen.AppointmentDetail.createRoute(appointmentId))
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = Screen.AppointmentDetail.route,
            arguments = listOf(
                navArgument("appointmentId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val appointmentId = backStackEntry.arguments?.getInt("appointmentId") ?: 0
            AppointmentDetailScreen(
                appointmentId = appointmentId,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onReschedule = {
                    navController.popBackStack()
                },
                onCancel = {
                    navController.popBackStack()
                }
            )
        }

        // ========== PERFIL ==========
        composable(Screen.Profile.route) {
            ProfileScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}