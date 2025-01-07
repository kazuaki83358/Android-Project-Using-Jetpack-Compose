package com.example.firebase_practice

import androidx.compose.runtime.remember
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextFieldDefaults

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignupScreen() {
    val buttonBlue = Color(0xFF0094FF)
    val customBlue = Color(0xFF2091B6)
    val customCyan = Color(0xFFB1E7F8)

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = Brush.verticalGradient(colors = listOf(customBlue, customCyan))),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(50.dp))

        // Profile Picture
        Box(
            modifier = Modifier
                .size(100.dp)
                .background(Color.Gray, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text("Add", color = Color.White, fontSize = 16.sp)
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Profile Photo",
            fontSize = 13.sp,
            fontWeight = FontWeight.Black,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(35.dp))

        // Name Input - Translucent White Background Inside TextField (Not Border)
        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(Color(0x80FFFFFF), shape = RoundedCornerShape(12.dp)), // Translucent white inside
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.Transparent,  // Ensure the container is transparent
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent
            )
        )

        // Email Input - Translucent White Background Inside TextField (Not Border)
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("E-mail") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(Color(0x80FFFFFF), shape = RoundedCornerShape(12.dp)), // Translucent white inside
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.Transparent,  // Ensure the container is transparent
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent
            )
        )

        // Password Input - Translucent White Background Inside TextField (Not Border)
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(Color(0x80FFFFFF), shape = RoundedCornerShape(12.dp)), // Translucent white inside
            visualTransformation = PasswordVisualTransformation(),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.Transparent,  // Ensure the container is transparent
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent
            )
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Register Button
        Button(
            onClick = { /* Handle Register click */ },
            modifier = Modifier
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = buttonBlue)
        ) {
            Text(text = "Sign up", color = Color.White, fontSize = 18.sp)
        }
    }
}
