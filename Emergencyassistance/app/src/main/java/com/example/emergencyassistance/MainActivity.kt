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
                // Remember the navController
                val navController = rememberNavController()

                // Setup the NavHost with routes for screens
                NavHost(navController = navController, startDestination = "home_screen") {
                    composable("home_screen") {
                        HomeScreen(navController = navController) // Pass navController
                    }
                    composable("add_emergency_contact") {
                        AddEmergencyContactScreen(navController = navController) // Add the emergency contact screen
                    }
                    composable("contact_list") {
                        ContactListScreen(navController = navController) // Add the contact list screen
                    }
                }
            }
        }
    }
}
