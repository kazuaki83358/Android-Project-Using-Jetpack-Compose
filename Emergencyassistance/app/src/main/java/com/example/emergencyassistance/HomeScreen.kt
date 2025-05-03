package com.example.emergencyassistance

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.telephony.SmsManager
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    val context = LocalContext.current
    val emergencyContactDao = AppDatabase.getDatabase(context).emergencyContactDao()

    var permissionsGranted by remember { mutableStateOf(false) }

    val requiredPermissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.SEND_SMS,
        Manifest.permission.RECORD_AUDIO
    )

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        permissionsGranted = requiredPermissions.all { permissions[it] == true }
        if (permissionsGranted) startVoiceCommand(context)
        else Toast.makeText(context, "All permissions are required.", Toast.LENGTH_SHORT).show()
    }

    LaunchedEffect(Unit) {
        val notGranted = requiredPermissions.filter {
            ContextCompat.checkSelfPermission(context, it) != PackageManager.PERMISSION_GRANTED
        }
        if (notGranted.isEmpty()) {
            permissionsGranted = true
            startVoiceCommand(context)
        } else {
            permissionLauncher.launch(requiredPermissions)
        }
    }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                title = { Text("Emergency Assistance", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFea9ab2)),
                actions = {
                    IconButton(onClick = { navController.navigate("nearby_help_screen") }) {
                        Icon(Icons.Default.Notifications, contentDescription = "Nearby Help", tint = Color.White)
                    }
                }
            )
        },
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
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Emergency Assistance",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Red,
                modifier = Modifier.padding(vertical = 40.dp)
            )

            SOSButton(
                permissionsGranted = permissionsGranted,
                context = context,
                onTrigger = { triggerSOS(context, emergencyContactDao) }
            )

            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = "Tap the SOS button to alert your emergency contacts.",
                fontSize = 16.sp,
                color = Color(0xFF218380),
                modifier = Modifier.padding(horizontal = 10.dp)
            )
        }
    }
}

@Composable
private fun SOSButton(
    permissionsGranted: Boolean,
    context: Context,
    onTrigger: () -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(200.dp)
            .background(Color(0xFFf20089), shape = CircleShape)
            .padding(20.dp)
    ) {
        Button(
            onClick = {
                if (permissionsGranted) onTrigger()
                else Toast.makeText(context, "Permissions required to send SOS.", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier.fillMaxSize(),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
        ) {
            Text("S.O.S", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }
    }
}

fun startVoiceCommand(context: Context) {
    try {
        val intent = Intent(context, VoiceCommandService::class.java)
        ContextCompat.startForegroundService(context, intent)
        Log.d("HomeScreen", "VoiceCommandService started.")
    } catch (e: Exception) {
        Log.e("HomeScreen", "Error starting service: ${e.message}")
        Toast.makeText(context, "Could not start voice command.", Toast.LENGTH_SHORT).show()
    }
}
fun triggerSOS(context: Context, emergencyContactDao: EmergencyContactDao) {
    if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
        LocationServices.getFusedLocationProviderClient(context).lastLocation
            .addOnSuccessListener { location ->
                location?.let {
                    // Update isSOSActive to true
                    val isSOSActive = true
                    updateLocationToFirestore(context, "User Name", isSOSActive)

                    // Send the SOS message with the location
                    sendSOSMessage(it.latitude, it.longitude, context, emergencyContactDao)
                } ?: Toast.makeText(context, "Unable to fetch location.", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(context, "Location fetch failed.", Toast.LENGTH_SHORT).show()
            }
    } else {
        Toast.makeText(context, "Location permission is needed.", Toast.LENGTH_SHORT).show()
    }
}

fun sendSOSMessage(lat: Double, lon: Double, context: Context, emergencyContactDao: EmergencyContactDao) {
    CoroutineScope(Dispatchers.IO).launch {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
            val message = "S.O.S. Emergency! I'm at https://www.google.com/maps?q=$lat,$lon. Please help!"
            val contacts = emergencyContactDao.getAllContacts()

            contacts.forEach { contact ->
                try {
                    SmsManager.getDefault().sendTextMessage(contact.phoneNumber, null, message, null, null)
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "Message sent to ${contact.name}.", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "Failed to send to ${contact.name}.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        } else {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "SMS permission required.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
