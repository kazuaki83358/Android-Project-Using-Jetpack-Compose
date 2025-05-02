package com.example.emergencyassistance

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.emergencyassistance.ui.theme.EmergencyAssistanceTheme
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {

    private val REQUEST_AUDIO_PERMISSION_CODE = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (checkAudioPermission()) {
            setupContent()
        } else {
            requestAudioPermission()
        }
    }

    private fun checkAudioPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.FOREGROUND_SERVICE_MICROPHONE) == PackageManager.PERMISSION_GRANTED
        } else {
            ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun requestAudioPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.RECORD_AUDIO, Manifest.permission.FOREGROUND_SERVICE_MICROPHONE),
                REQUEST_AUDIO_PERMISSION_CODE
            )
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.RECORD_AUDIO),
                REQUEST_AUDIO_PERMISSION_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_AUDIO_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setupContent()
            } else {
                finish() // Close the app if permission is denied
            }
        }
    }

    private fun setupContent() {
        setContent {
            EmergencyAssistanceTheme {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = "splash_screen"
                ) {
                    composable("splash_screen") {
                        SplashScreen(onTimeout = {
                            val auth = FirebaseAuth.getInstance()
                            val user = auth.currentUser

                            if (user != null) {
                                navController.navigate("home_screen") {
                                    popUpTo("splash_screen") { inclusive = true }
                                }
                            } else {
                                navController.navigate("login_screen") {
                                    popUpTo("splash_screen") { inclusive = true }
                                }
                            }

                            startService(Intent(this@MainActivity, VoiceCommandService::class.java))
                        })
                    }

                    composable("login_screen") {
                        LoginScreen(navController = navController)
                    }

                    composable("signup_screen") {
                        SignUpScreen(navController = navController)
                    }

                    composable("home_screen") {
                        HomeScreen(navController = navController)
                    }

                    composable("add_emergency_contact") {
                        AddEmergencyContactScreen(navController = navController)
                    }

                    composable("contact_list") {
                        ContactListScreen(navController = navController)
                    }

                    // ✅ New screen added
                    composable("nearby_help_screen") {
                        NearbyHelpScreen(navController = navController)
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopService(Intent(this, VoiceCommandService::class.java))
    }
}
