package com.example.emergencyassistance

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun ContactListScreen(navController: NavController) {
    val customPink = Color(0xFFe27396)
    val customPink2 = Color(0xFFea9ab2)
    val playWrite = FontFamily(
        Font(R.font.mplus)
    )
    val playFareBold = FontFamily(
        Font(R.font.playfairbold)
    )
    val context = LocalContext.current
    val contactList = remember { mutableStateListOf<EmergencyContact>() }

    // Load contacts from Room when the screen is displayed
    LaunchedEffect(Unit) {
        loadContacts(context, contactList)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = Brush.verticalGradient(colors = listOf(customPink, customPink2)))
    ) {
        Text(
            modifier = Modifier
                .padding(16.dp),
            text = "Emergency Contacts",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = playFareBold,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Display the list of contacts
        LazyColumn(
            modifier = Modifier
                .padding(6.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(contactList) { contact ->
                ContactItem(contact = contact, onDelete = {
                    deleteContact(context, contact, contactList)
                })
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Button to navigate back to Add Emergency Contact screen
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
                text = "Back",
                fontWeight = FontWeight.Bold,
                fontFamily = playFareBold,
                color = Color.White
            )
        }
    }
}

@Composable
fun ContactItem(contact: EmergencyContact, onDelete: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, top = 8.dp, end = 8.dp, bottom = 8.dp) // Add padding to left and other sides
            .background(Color(0xFF006ba6))
            .height(80.dp), // Increased height for the card
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.padding(start = 8.dp) // Add padding to the left of the text
        ) {
            Text(
                text = "Contact: ${contact.name}", // Custom text for the name
                fontWeight = FontWeight.Bold,
                color = Color.White // Set text color to white
            )
            Text(
                text = "Phone: ${contact.phoneNumber}", // Custom text for the phone number
                color = Color.White // Set text color to white
            )
        }

        // Delete Button with white icon
        IconButton(onClick = { onDelete() }) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete Contact",
                tint = Color.White // Set icon color to white
            )
        }
    }
}

fun loadContacts(context: Context, contactList: MutableList<EmergencyContact>) {
    val db = AppDatabase.getDatabase(context)
    val emergencyContactDao = db.emergencyContactDao()

    CoroutineScope(Dispatchers.IO).launch {
        val contacts = emergencyContactDao.getAllContacts()
        withContext(Dispatchers.Main) {
            contactList.clear()
            contactList.addAll(contacts)
        }
    }
}

fun deleteContact(context: Context, contact: EmergencyContact, contactList: MutableList<EmergencyContact>) {
    val db = AppDatabase.getDatabase(context)
    val emergencyContactDao = db.emergencyContactDao()

    CoroutineScope(Dispatchers.IO).launch {
        emergencyContactDao.deleteContact(contact)
        withContext(Dispatchers.Main) {
            contactList.remove(contact)
            Toast.makeText(context, "Contact Deleted", Toast.LENGTH_SHORT).show()
        }
    }
}
