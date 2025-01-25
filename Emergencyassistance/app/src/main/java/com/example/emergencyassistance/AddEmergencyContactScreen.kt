package com.example.emergencyassistance

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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

@Composable
fun AddEmergencyContactScreen(navController: NavController) {
    var contactName by remember { mutableStateOf("") }
    var contactPhone by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Add Emergency Contact",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Name input field
        TextField(
            value = contactName,
            onValueChange = { contactName = it },
            label = { Text("Contact Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Phone number input field
        TextField(
            value = contactPhone,
            onValueChange = { contactPhone = it },
            label = { Text("Phone Number") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Save Button
        Button(
            onClick = {
                if (contactName.isNotEmpty() && contactPhone.isNotEmpty()) {
                    saveEmergencyContact(contactName, contactPhone, context)
                    navController.navigate("contact_list") // Navigate to the contact list screen
                } else {
                    Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save Contact")
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Button to navigate to the list of contacts
        Button(
            onClick = { navController.navigate("contact_list") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("View Contact List")
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
