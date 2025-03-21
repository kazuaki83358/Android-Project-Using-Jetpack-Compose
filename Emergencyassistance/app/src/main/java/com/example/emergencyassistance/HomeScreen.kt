package com.example.emergencyassistance

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.telephony.SmsManager
import android.widget.Toast
import android.speech.RecognizerIntent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.content.Intent
import android.os.Bundle
import android.speech.SpeechRecognizer
import androidx.navigation.NavController

@Composable
fun HomeScreen(navController: NavController) {
    val context = LocalContext.current
    var locationPermissionGranted by remember { mutableStateOf(false) }
    var smsPermissionGranted by remember { mutableStateOf(false) }
    val emergencyContactDao = AppDatabase.getDatabase(context).emergencyContactDao()

    // Request permissions and handle them as before
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            locationPermissionGranted = isGranted
        }
    )

    val smsPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            smsPermissionGranted = isGranted
        }
    )

    LaunchedEffect(Unit) {
        // Check and request permissions if needed
        val locationPermissionStatus = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        if (locationPermissionStatus == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true
        } else {
            locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }

        val smsPermissionStatus = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.SEND_SMS
        )
        if (smsPermissionStatus == PackageManager.PERMISSION_GRANTED) {
            smsPermissionGranted = true
        } else {
            smsPermissionLauncher.launch(Manifest.permission.SEND_SMS)
        }

        // Start voice command immediately
        startVoiceCommand(context)
    }

    Scaffold(
        containerColor = Color.White,
        bottomBar = {
            BottomAppBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                containerColor = Color(0xFFea9ab2)
            ) {
                IconButton(
                    onClick = { navController.navigate("add_emergency_contact") },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.personadd),
                        contentDescription = "Add Contact",
                        tint = Color.White,
                        modifier = Modifier.size(25.dp)
                    )
                }

                IconButton(
                    onClick = { navController.navigate("contact_list") },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.List,
                        contentDescription = "View Contacts",
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Emergency Assistance",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Red,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 40.dp, bottom = 40.dp)
            )
            Spacer(modifier = Modifier.height(60.dp))

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(200.dp)
                    .background(Color(0xFFf20089), shape = CircleShape)
                    .padding(20.dp)
            ) {
                Button(
                    onClick = {
                        if (locationPermissionGranted && smsPermissionGranted) {
                            triggerSOS(context, emergencyContactDao) // Pass emergencyContactDao
                        } else {
                            Toast.makeText(context, "Permissions are required to send an SOS", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(0.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                ) {
                    Text(
                        text = "S.O.S",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            TextButton(onClick = { }) {
                Text(
                    text = "Tap the SOS button to alert your emergency contacts.",
                    fontSize = 16.sp,
                    color = Color(0xFF218380)
                )
            }
        }
    }
}

fun startVoiceCommand(context: Context) {
    // ... (rest of startVoiceCommand remains the same)
}

// Trigger the SOS alert
fun triggerSOS(context: Context, emergencyContactDao: EmergencyContactDao) { // Pass emergencyContactDao
    val locationPermissionStatus = ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    if (locationPermissionStatus == PackageManager.PERMISSION_GRANTED) {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            location?.let {
                val lat = it.latitude
                val lon = it.longitude
                sendSOSMessage(lat, lon, context, emergencyContactDao) // Pass emergencyContactDao
            }
        }
    } else {
        Toast.makeText(context, "Location permission is required", Toast.LENGTH_SHORT).show()
    }
}

// Function to send the SOS message
fun sendSOSMessage(lat: Double, lon: Double, context: Context, emergencyContactDao: EmergencyContactDao) {
    CoroutineScope(Dispatchers.IO).launch {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.SEND_SMS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Generate the location URL
            val locationUrl = "https://www.google.com/maps?q=$lat,$lon"
            val message = "S.O.S. Emergency! I am at the following location: $locationUrl. Please help!"

            val contacts = emergencyContactDao.getAllContacts()
            contacts.forEach { contact ->
                launch(Dispatchers.IO) { //send each message in background.
                    try {
                        val smsManager = SmsManager.getDefault()
                        smsManager.sendTextMessage(contact.phoneNumber, null, message, null, null)
                        withContext(Dispatchers.Main) {
                            Toast.makeText(context, "SOS message sent to ${contact.name}", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(context, "Failed to send SOS message to ${contact.name}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        } else {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "SMS permission is required", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
