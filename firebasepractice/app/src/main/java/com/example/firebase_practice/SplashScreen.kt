package com.example.firebase_practice

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun SplashScreen(navController: NavController) {
    val customBlue = Color(0xFF2091B6)
    val customCyan = Color(0xFFB1E7F8)

    // Launch the effect to delay the navigation to the home screen
    LaunchedEffect(true) {
        // Delay for 2 seconds before navigating to the home screen
        kotlinx.coroutines.delay(2000)
        navController.navigate("home") {
            // Pop the splash screen from the back stack
            popUpTo("splash") { inclusive = true }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = Brush.verticalGradient(colors = listOf(customBlue, customCyan)))
            .statusBarsPadding()  // Ensure it doesn't overlap the status bar
    ) {
        // 3D Illustration Image - positioned towards the top
        Image(
            painter = painterResource(id = R.drawable.splash_img),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 90.dp)  // Adjust padding to fit below status bar
                .size(250.dp)
        )

        // Loading Indicator - Positioned just above the bottom center
        CircularProgressIndicator(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 80.dp),  // Moved it above the bottom
            color = Color.White,
            strokeWidth = 4.dp
        )
    }
}
