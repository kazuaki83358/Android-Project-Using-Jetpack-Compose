package com.example.firebase_practice

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen() {
    val buttonBlue = Color(0xFF0094FF)
    val customBlue = Color(0xFF2091B6)
    val customCyan = Color(0xFFB1E7F8)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = Brush.verticalGradient(colors = listOf(customBlue, customCyan))),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(50.dp))

        // Header
        Text("Getting Started", fontSize = 24.sp, color = Color.Black, fontWeight = FontWeight.Bold)
        Text("Let's login to explore", fontSize = 16.sp, color = Color.White)

        Spacer(modifier = Modifier.height(20.dp))

        // Email Input - Transparent white background and larger icon
        TextField(
            value = "", onValueChange = {},
            label = { Text("E-mail") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(Color(0x80FFFFFF), shape = RoundedCornerShape(12.dp)), // Transparent white background
            leadingIcon = {
                Icon(
                    Icons.Default.Email,
                    contentDescription = null,
                    modifier = Modifier.size(28.dp) // Increased icon size
                )
            },
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.Transparent, // Transparent background
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Password Input - Transparent white background and larger icon
        TextField(
            value = "", onValueChange = {},
            label = { Text("Password") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(Color(0x80FFFFFF), shape = RoundedCornerShape(12.dp)), // Transparent white background
            leadingIcon = {
                Icon(
                    Icons.Default.Lock,
                    contentDescription = null,
                    modifier = Modifier.size(28.dp) // Increased icon size
                )
            },
            visualTransformation = PasswordVisualTransformation(),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.Transparent, // Transparent background
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent
            )
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Sign In Button
        Button(
            onClick = { /* Handle Register click */ },
            modifier = Modifier
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = buttonBlue)
        ) {
            Text(text = "Sign in", color = Color.White, fontSize = 18.sp)
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Forgot Password
        TextButton(onClick = {  }) {
            Text("Forgot your password? Reset Password", color = Color.Blue)
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Social Media Login
        Text("Or continue with", color = Color.White, fontSize = 14.sp)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            IconButton(onClick = { /* Handle Facebook Login */ }) {
                Icon(painter = painterResource(R.drawable.facebook_icon), contentDescription = null, modifier = Modifier.size(30.dp)) // Increased size
            }
            IconButton(onClick = { /* Handle Google Login */ }) {
                Icon(painter = painterResource(R.drawable.google_icon), contentDescription = null, modifier = Modifier.size(30.dp)) // Increased size
            }
            IconButton(onClick = { /* Handle Apple Login */ }) {
                Icon(painter = painterResource(R.drawable.apple_icon), contentDescription = null, modifier = Modifier.size(30.dp)) // Increased size
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Navigate to Register
        TextButton(onClick = {  }) {
            Text(
                text = "Don't have an account? Register",
                fontWeight = FontWeight.Medium,
                color = Color.White
            )
        }
    }
}
