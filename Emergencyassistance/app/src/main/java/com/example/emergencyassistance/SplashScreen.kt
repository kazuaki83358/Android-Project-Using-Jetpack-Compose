package com.example.emergencyassistance

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onTimeout: () -> Unit) {
    val customPink = Color(0xFFe27396)
    val customPink2 = Color(0xFFea9ab2)
    val playWrite = FontFamily(
        Font(R.font.playwrite)
    )
    val playFareBold = FontFamily(
        Font(R.font.playfairbold)
    )
    // Main container with gradient background
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = Brush.verticalGradient(colors = listOf(customPink, customPink2))),
        contentAlignment = Alignment.Center
    ) {
        // Column for the main content in the center
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo Image
            Image(
                painter = painterResource(id = R.drawable.logo_s),
                contentDescription = "Splash Screen Image"
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Welcome Text
            Text(
                text = "Welcome",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = playWrite
            )
        }

        // Your name as the app creator at the bottom
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 16.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                // Loading Spinner
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Text(
                    text = "Made by Nishant Kumar",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = playFareBold
                )
            }
        }
    }

    // Simulate a delay for splash screen transition
    LaunchedEffect(Unit) {
        delay(2000) // 2-second delay
        onTimeout() // Trigger navigation after the delay
    }
}
