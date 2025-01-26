package com.example.emergencyassistance

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEmergencyContactScreen(navController: NavController) {
    val customPink = Color(0xFFe27396)
    val customPink2 = Color(0xFFea9ab2)
    val playFareBold = FontFamily(
        Font(R.font.playfairbold)
    )
    var contactName by remember { mutableStateOf("") }
    var contactPhone by remember { mutableStateOf("") }
    val context = LocalContext.current

    // Function to check if phone number is valid (only digits)
    fun isValidPhoneNumber(phoneNumber: String): Boolean {
        val regex = "^[0-9]*$".toRegex() // Only digits
        return regex.matches(phoneNumber)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = Brush.verticalGradient(colors = listOf(customPink, customPink2))),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Add Emergency Contact",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = playFareBold,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Name input field
        TextField(
            value = contactName,
            onValueChange = { contactName = it },
            label = { Text("Contact Name") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .border(1.dp, Color.White, RoundedCornerShape(8.dp)), // White border with rounded corners
            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor = Color(0x80FFFFFF), // Light translucent white background
                focusedBorderColor = Color.White,
                unfocusedBorderColor = Color.White,
                focusedLabelColor = Color.White,
                unfocusedLabelColor = Color.White,
                cursorColor = Color.Black // Set cursor color to black
            ),
            textStyle = TextStyle(color = Color.Black) // This will set the text color to black while typing
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Phone number input field
        TextField(
            value = contactPhone,
            onValueChange = { contactPhone = it },
            label = { Text("Phone Number") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .border(1.dp, Color.White, RoundedCornerShape(8.dp)), // White border with rounded corners
            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor = Color(0x80FFFFFF), // Light translucent white background
                focusedBorderColor = Color.White,
                unfocusedBorderColor = Color.White,
                focusedLabelColor = Color.White,
                unfocusedLabelColor = Color.White,
                cursorColor = Color.Black // Set cursor color to black
            ),
            textStyle = TextStyle(color = Color.Black) // Set text color to black while typing
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Save Button
        Button(
            onClick = {
                if (contactName.isNotEmpty() && contactPhone.isNotEmpty()) {
                    // Validate the phone number
                    if (isValidPhoneNumber(contactPhone)) {
                        saveEmergencyContact(contactName, contactPhone, context)
                        navController.navigate("contact_list") // Navigate to the contact list screen
                    } else {
                        Toast.makeText(context, "Please enter a valid phone number (numbers only)", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF006ba6) // Custom blue color
            ),
            shape = RoundedCornerShape(16.dp) // Light rounded corners
        ) {
            Text(
                text = "Save Contact",
                fontWeight = FontWeight.Bold,
                fontFamily = playFareBold,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(5.dp))

        // Button to navigate to the list of contacts
        Button(
            onClick = { navController.navigate("contact_list") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF06d6a0) // Custom blue color
            ),
            shape = RoundedCornerShape(16.dp) // Light rounded corners
        ) {
            Text(
                text = "View Contacts",
                fontWeight = FontWeight.Bold,
                fontFamily = playFareBold,
                color = Color.White
            )
        }

    }
}

// Save the contact to Room
fun saveEmergencyContact(contactName: String, contactPhone: String, context: Context) {
    val db = AppDatabase.getDatabase(context)
    val emergencyContactDao = db.emergencyContactDao()

    // Create a new EmergencyContact object
    val newContact = EmergencyContact(name = contactName, phoneNumber = contactPhone)

    // Save contact to Room database in a coroutine
    // Assuming you are using viewModelScope or similar coroutine context to call this method
    // For simplicity, you can call this method in a coroutine scope
    CoroutineScope(Dispatchers.IO).launch {
        emergencyContactDao.insertContact(newContact)
        withContext(Dispatchers.Main) {
            Toast.makeText(context, "Emergency Contact Saved!", Toast.LENGTH_SHORT).show()
        }
    }
}

