package com.example.animewallpaperapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavHostController) {
    // Effect to delay and navigate to home
    LaunchedEffect(key1 = true) {
        delay(2000) // Delay for 2 seconds
        navController.navigate("home") {
            // Remove the splash screen from the back stack
            popUpTo("splash") { inclusive = true }
        }
    }
    val anglina = FontFamily(
        Font(R.font.feel_free) // Replace with your font resource name
    )
    // Splash Screen UI with centered image and text below
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF212529)), // Dark background
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.splash_logo), // Replace with your image resource
                contentDescription = "Splash Screen Image"
            )
            Text(
                text = "Otaku Orbit",
                color = Color.White, // Text color
                fontSize = 30.sp, // Font size
                fontFamily = anglina, //Custom font
                fontWeight = FontWeight.Bold, // Font weight
                modifier = Modifier.padding(top = 16.dp) // Space between image and text
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Preview2() {
    val navController = rememberNavController()
    SplashScreen(navController = navController) // Update to directly preview the SplashScreen
}
