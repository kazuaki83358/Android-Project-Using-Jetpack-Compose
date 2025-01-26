package com.example.emergencyassistance

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.emergencyassistance.ui.theme.EmergencyAssistanceTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EmergencyAssistanceTheme {
                // Remember the NavController
                val navController = rememberNavController()

                // Set up the NavHost with routes
                NavHost(navController = navController, startDestination = "splash_screen") {
                    // Splash Screen
                    composable("splash_screen") {
                        SplashScreen(onTimeout = {
                            // Navigate to Home Screen after splash
                            navController.navigate("home_screen") {
                                popUpTo("splash_screen") { inclusive = true }
                            }
                        })
                    }
                    // Home Screen
                    composable("home_screen") {
                        HomeScreen(navController = navController)
                    }
                    // Add Emergency Contact Screen
                    composable("add_emergency_contact") {
                        AddEmergencyContactScreen(navController = navController)
                    }
                    // Contact List Screen
                    composable("contact_list") {
                        ContactListScreen(navController = navController)
                    }
                }
            }
        }
    }
}
