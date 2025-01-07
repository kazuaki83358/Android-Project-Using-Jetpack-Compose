package com.example.firebase_practice

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun HomeScreen(navController: NavController) {
    val customBlue = Color(0xFF2091B6)
    val customCyan = Color(0xFFB1E7F8)
    val buttonBlue = Color(0xFF0094FF)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = Brush.verticalGradient(colors = listOf(customBlue, customCyan)))
    ) {
        // Column to align everything vertically
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top // Align content at the top
        ) {
            Spacer(modifier = Modifier.height(50.dp))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Adventure All",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "Explore all",
                    fontSize = 16.sp,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(40.dp)) // Adds space between title and image

            // Image below the title
            Image(
                painter = painterResource(id = R.drawable.splash_img),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .size(250.dp)
            )

            Spacer(modifier = Modifier.height(180.dp)) // Adds space between image and buttons

            // Log In Button
            Button(
                onClick = { navController.navigate("login") },  // Navigate to Login screen
                modifier = Modifier
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = buttonBlue)
            ) {
                Text(text = "Sign in", color = Color.White, fontSize = 18.sp)
            }

            Spacer(modifier = Modifier.height(16.dp)) // Adds space between buttons

            // Register Button
            Button(
                onClick = { navController.navigate("signup") },  // Navigate to Sign Up screen
                modifier = Modifier
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = buttonBlue)
            ) {
                Text(text = "Sign up", color = Color.White, fontSize = 18.sp)
            }
        }
    }
}
