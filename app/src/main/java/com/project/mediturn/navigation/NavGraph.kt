package com.project.mediturn.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.NavType

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String = Screen.Login.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // Autenticación
        composable(Screen.Login.route) {
            LoginScreen(onNavigateToRegister = { /*...*/ })
        }

        composable(Screen.Register.route) {
            RegisterScreen(onNavigateToLogin = { /*...*/ })
        }

        // Home
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToSearch = { /*...*/ },
                onNavigateToAppointments = { /*...*/ }
            )
        }

        // Médicos
        composable(Screen.DoctorList.route) {
            DoctorListScreen(onDoctorClick = { doctorId ->
                navController.navigate(Screen.DoctorDetail.createRoute(doctorId))
            })
        }

        composable(
            route = Screen.DoctorDetail.route,
            arguments = listOf(navArgument("doctorId") { type = NavType.IntType })
        ) { backStackEntry ->
            val doctorId = backStackEntry.arguments?.getInt("doctorId") ?: 0
            DoctorDetailScreen(
                doctorId = doctorId,
                onBookAppointment = {
                    navController.navigate(Screen.BookAppointment.createRoute(doctorId))
                }
            )
        }

        // Citas
        composable(
            route = Screen.BookAppointment.route,
            arguments = listOf(navArgument("doctorId") { type = NavType.IntType })
        ) { backStackEntry ->
            val doctorId = backStackEntry.arguments?.getInt("doctorId") ?: 0
            BookAppointmentScreen(doctorId = doctorId)
        }

        composable(Screen.MyAppointments.route) {
            MyAppointmentsScreen(onAppointmentClick = { appointmentId ->
                navController.navigate(Screen.AppointmentDetail.createRoute(appointmentId))
            })
        }

        // Perfil
        composable(Screen.Profile.route) {
            ProfileScreen()
        }
    }
}