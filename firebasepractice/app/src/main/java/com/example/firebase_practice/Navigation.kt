package com.example.firebase_practice

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun Navigation(){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") {
            SplashScreen(navController = navController)  // Passing navController to SplashScreen
        }
        composable("home") {
            HomeScreen(navController)  // Pass the NavController to HomeScreen
        }
        composable("login") {
            LoginScreen()  // Define your LoginPage Composable
        }
        composable("signup") {
            SignupScreen()  // Define your SignUpPage Composable
        }
    }
}