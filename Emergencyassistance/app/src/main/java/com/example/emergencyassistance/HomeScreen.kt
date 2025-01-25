package com.example.emergencyassistance

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.telephony.SmsManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.google.android.gms.location.LocationServices
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun HomeScreen(navController: NavController) {
    var locationPermissionGranted by remember { mutableStateOf(false) }
    var smsPermissionGranted by remember { mutableStateOf(false) }

    val context = LocalContext.current

    // Requesting Permissions
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
        // Check if permissions are granted and launch requests if needed
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
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Emergency Assistance",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Red
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                navController.navigate("add_emergency_contact")
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Emergency Contact")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Red, Round SOS Button
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(150.dp)
                .background(Color.Red, shape = CircleShape)
                .padding(20.dp)
        ) {
            Button(
                onClick = {
                    if (locationPermissionGranted && smsPermissionGranted) {
                        triggerSOS(context)
                    } else {
                        Toast.makeText(context, "Permissions are required to send an SOS", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(0.dp),
                colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = Color.Transparent)
            ) {
                Text(
                    text = "S.O.S",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Add some text or icon about the SOS functionality
        TextButton(onClick = {
            // Show information about the SOS button functionality
        }) {
            Text(text = "Tap the SOS button to alert your emergency contacts.", fontSize = 16.sp)
        }
    }
}

fun triggerSOS(context: Context) {
    if (ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    ) {
        // Fetch user's current location
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            location?.let {
                val lat = it.latitude
                val lon = it.longitude

                // Fetch emergency contacts from Room database
                val db = AppDatabase.getDatabase(context)
                val emergencyContactDao = db.emergencyContactDao()

                CoroutineScope(Dispatchers.IO).launch {
                    val contacts = emergencyContactDao.getAllContacts()
                    withContext(Dispatchers.Main) {
                        for (contact in contacts) {
                            sendSOSMessage(contact.phoneNumber, lat, lon, context)
                        }
                    }
                }
            } ?: run {
                // Handle case where location is null
                Toast.makeText(context, "Unable to get location", Toast.LENGTH_SHORT).show()
            }
        }
    } else {
        Toast.makeText(context, "Location permission is required", Toast.LENGTH_SHORT).show()
    }
}

fun sendSOSMessage(contact: String?, lat: Double, lon: Double, context: Context) {
    // Check if SEND_SMS permission is granted
    if (ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.SEND_SMS
        ) == PackageManager.PERMISSION_GRANTED
    ) {
        // Create an SOS message with location details
        val message = "S.O.S. Emergency! I am at location: $lat, $lon. Please help!"

        // Send the message (you can use SmsManager or any API for messaging)
        if (contact != null) {
            try {
                val smsManager = SmsManager.getDefault()
                smsManager.sendTextMessage(contact, null, message, null, null)
                Toast.makeText(context, "SOS message sent to $contact", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                // Handle any potential exceptions that might occur while sending the SMS
                Toast.makeText(context, "Failed to send SOS message to $contact", Toast.LENGTH_SHORT).show()
            }
        }
    } else {
        // Handle missing permission for SMS
        Toast.makeText(context, "SMS permission is required", Toast.LENGTH_SHORT).show()
    }
}
