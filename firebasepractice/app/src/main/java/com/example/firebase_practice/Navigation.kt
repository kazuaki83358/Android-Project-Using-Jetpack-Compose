package com.example.firebase_practice

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.delay

@Composable
fun Navigation(auth: FirebaseAuth, storage: FirebaseStorage) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "splash") {
        // Splash Screen Route
        composable("splash") {
            SplashScreen(navController = navController)
            // Navigate to home after the splash screen
            LaunchedEffect(Unit) {
                delay(2000) // Splash screen duration
                navController.navigate("home") {
                    popUpTo("splash") { inclusive = true }
                }
            }
        }

        // Home Screen Route
        composable("home") {
            HomeScreen(navController = navController) // The home screen
        }

        // Login Screen Route
        composable("login") {
            LoginScreen(navController = navController)
        }

        // Signup Screen Route
        composable("signup") {
            SignupScreen(navController = navController, auth = auth)
        }
    }
}
