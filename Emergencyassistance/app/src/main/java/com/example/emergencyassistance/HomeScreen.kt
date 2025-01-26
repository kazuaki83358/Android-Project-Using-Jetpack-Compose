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
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun HomeScreen(navController: NavController) {
    val customPink = Color(0xFFf20089)
    val customBlue = Color(0xFF218380)
    var locationPermissionGranted by remember { mutableStateOf(false) }
    var smsPermissionGranted by remember { mutableStateOf(false) }
    val playWrite = FontFamily(
        Font(R.font.mplus)
    )
    val playFareBold = FontFamily(
        Font(R.font.playfairbold)
    )
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
        // Check permissions and request if needed
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

    Scaffold(
        containerColor = Color.White, // Set background color to white
        bottomBar = {
            BottomAppBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp), // Adjust the height as needed
                containerColor = Color(0xFFea9ab2) // Light pink color
            ) {
                // Add Contact Button with Icon (no Spacer before it)
                IconButton(
                    onClick = { navController.navigate("add_emergency_contact") },
                    modifier = Modifier.weight(1f) // Spread the buttons across the width
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.personadd), // Correct way to reference drawable
                        contentDescription = "Add Contact",
                        tint = Color.White,
                        modifier = Modifier.size(25.dp)
                    )
                }

                // View Contacts Button with Increased Icon Size (no Spacer after it)
                IconButton(
                    onClick = { navController.navigate("contact_list") },
                    modifier = Modifier.weight(1f) // Spread the buttons across the width
                ) {
                    Icon(
                        imageVector = Icons.Default.List, // Replace with an icon for viewing a list
                        contentDescription = "View Contacts",
                        tint = Color.White,
                        modifier = Modifier.size(32.dp) // Increase size as needed
                    )
                }
            }
        }
    )
    { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.White), // Ensure white background always
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Emergency Assistance Text
            Text(
                text = "Emergency Assistance",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = playFareBold,
                color = Color.Red,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 40.dp, bottom = 40.dp) // Added more padding at top and bottom
            )
            Spacer(modifier = Modifier.height(60.dp))

            // Larger SOS Button
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(200.dp) // Increased size
                    .background(customPink, shape = CircleShape)
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

            // Add text for SOS functionality
            TextButton(onClick = {
                // Show information about the SOS button functionality
            }) {
                Text(
                    text = "Tap the SOS button to alert your emergency contacts.",
                    fontSize = 16.sp,
                    fontFamily = playWrite,
                    color = customBlue
                )
            }
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

// Function to trigger the SOS message
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
                        if (contacts.isEmpty()) {
                            // Show a toast if no emergency contacts are added
                            Toast.makeText(context, "No emergency contacts added.", Toast.LENGTH_SHORT).show()
                        } else {
                            // Send SOS message to all contacts if there are any
                            for (contact in contacts) {
                                sendSOSMessage(contact.phoneNumber, lat, lon, context)
                            }
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
    if (ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.SEND_SMS
        ) == PackageManager.PERMISSION_GRANTED
    ) {
        val message = "S.O.S. Emergency! I am at location: $lat, $lon. Please help!"
        if (contact != null) {
            try {
                val smsManager = SmsManager.getDefault()
                smsManager.sendTextMessage(contact, null, message, null, null)
                Toast.makeText(context, "SOS message sent to $contact", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(context, "Failed to send SOS message to $contact", Toast.LENGTH_SHORT).show()
            }
        }
    } else {
        Toast.makeText(context, "SMS permission is required", Toast.LENGTH_SHORT).show()
    }
}
