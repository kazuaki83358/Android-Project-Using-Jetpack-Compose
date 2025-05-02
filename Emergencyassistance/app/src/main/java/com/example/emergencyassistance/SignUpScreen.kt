package com.example.emergencyassistance

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material3.ButtonDefaults
import androidx.navigation.NavController
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import androidx.navigation.compose.rememberNavController

@Composable
fun SignUpScreen(navController: NavController) {
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val isSignupEnabled = email.isNotEmpty() && password.isNotEmpty() && confirmPassword == password && name.isNotEmpty() && phone.isNotEmpty()

    val auth = FirebaseAuth.getInstance()
    val firestore = FirebaseFirestore.getInstance()

    // Handle Signup
    val context = LocalContext.current
    val handleSignup = lambda@{
        if (password != confirmPassword) {
            Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
            return@lambda
        }

        isLoading = true
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    val userMap = hashMapOf(
                        "name" to name,
                        "phone" to phone,
                        "email" to email
                    )

                    firestore.collection("users").document(user!!.uid).set(userMap)
                        .addOnCompleteListener { firestoreTask ->
                            isLoading = false
                            if (firestoreTask.isSuccessful) {
                                Toast.makeText(context, "Signup successful! Please login.", Toast.LENGTH_SHORT).show()
                                navController.navigate("login_screen") {
                                    popUpTo("signup_screen") { inclusive = true }
                                }
                            } else {
                                Toast.makeText(context, "Error saving user data", Toast.LENGTH_SHORT).show()
                            }
                        }
                } else {
                    Toast.makeText(context, "Error signing up", Toast.LENGTH_SHORT).show()
                    isLoading = false
                }
            }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LightGrey)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Sign Up",
            color = DeepRed,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Input Fields
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name", color = DarkText) },
            modifier = Modifier.fillMaxWidth(),
            textStyle = TextStyle(color = DarkText)
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("Phone Number", color = DarkText) },
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { /* Handle action */ }),
            modifier = Modifier.fillMaxWidth(),
            textStyle = TextStyle(color = DarkText)
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email", color = DarkText) },
            modifier = Modifier.fillMaxWidth(),
            textStyle = TextStyle(color = DarkText)
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password", color = DarkText) },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            textStyle = TextStyle(color = DarkText)
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirm Password", color = DarkText) },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            textStyle = TextStyle(color = DarkText)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { handleSignup() }, // Calling handleSignup without returning from composable
            enabled = isSignupEnabled && !isLoading,
            colors = ButtonDefaults.buttonColors(
                containerColor = DeepRed,
                disabledContainerColor = DeepRed.copy(alpha = 0.4f)
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
            } else {
                Text("Sign Up", color = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        val annotatedText = buildAnnotatedString {
            append("Already have an account? ")
            pushStringAnnotation(tag = "LOGIN", annotation = "login")
            withStyle(
                style = SpanStyle(
                    color = DeepRed,
                    fontWeight = FontWeight.Bold,
                    textDecoration = TextDecoration.None
                )
            ) {
                append("Log in")
            }
            pop()
        }

        ClickableText(
            text = annotatedText,
            onClick = { offset ->
                annotatedText.getStringAnnotations("LOGIN", offset, offset)
                    .firstOrNull()?.let {
                        navController.navigate("login_screen")
                    }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSignupScreen() {
    SignUpScreen(navController = rememberNavController())
}
