package com.example.emergencyassistance

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.ui.graphics.Color
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NearbyHelpScreen(navController: NavController) {
    val context = LocalContext.current
    val firestore = FirebaseFirestore.getInstance()
    val nearbyUsers = remember { mutableStateListOf<NearbyUser>() }
    val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        if (currentUserId == null) {
            Toast.makeText(context, "User not logged in", Toast.LENGTH_SHORT).show()
            return@LaunchedEffect
        }

        firestore.collection("users").document(currentUserId).get()
            .addOnSuccessListener { currentUserDoc ->
                val currentLocation = currentUserDoc.getGeoPoint("location")
                if (currentLocation == null) {
                    Toast.makeText(context, "Your location is not available", Toast.LENGTH_SHORT).show()
                    isLoading = false
                    return@addOnSuccessListener
                }

                val myLat = currentLocation.latitude
                val myLon = currentLocation.longitude

                firestore.collection("users")
                    .whereEqualTo("isSOSActive", true)
                    .get()
                    .addOnSuccessListener { result ->
                        nearbyUsers.clear()
                        for (doc in result) {
                            if (doc.id == currentUserId) continue  // Skip self
                            val name = doc.getString("name") ?: "Unknown"
                            val geoPoint = doc.getGeoPoint("location")
                            val isSOSActive = doc.getBoolean("isSOSActive") ?: false
                            if (geoPoint != null && isSOSActive) {
                                val distance = calculateDistance(myLat, myLon, geoPoint.latitude, geoPoint.longitude)
                                if (distance <= 2.0) {
                                    nearbyUsers.add(
                                        NearbyUser(
                                            uid = doc.id,
                                            name = name,
                                            latitude = geoPoint.latitude,
                                            longitude = geoPoint.longitude,
                                            isSOSActive = isSOSActive
                                        )
                                    )
                                }
                            }
                        }
                        isLoading = false
                    }
                    .addOnFailureListener {
                        Toast.makeText(context, "Failed to fetch users: ${it.message}", Toast.LENGTH_SHORT).show()
                        isLoading = false
                    }
            }
            .addOnFailureListener {
                Toast.makeText(context, "Failed to get current user location: ${it.message}", Toast.LENGTH_SHORT).show()
                isLoading = false
            }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nearby Help") },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFf20089))
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Users within 2 km", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))

            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.padding(top = 16.dp))
            } else if (nearbyUsers.isEmpty()) {
                Text("No users nearby.", fontSize = 16.sp)
            } else {
                LazyColumn {
                    items(nearbyUsers) { user ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth(),
                                horizontalAlignment = Alignment.Start
                            ) {
                                Text("Name: ${user.name}", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                                Text("Location: ${user.latitude}, ${user.longitude}", fontSize = 16.sp)
                                Button(
                                    onClick = {
                                        val uri = Uri.parse(user.locationLink) // Use the computed location link
                                        val intent = Intent(Intent.ACTION_VIEW, uri)
                                        context.startActivity(intent)
                                    },
                                    modifier = Modifier.padding(top = 8.dp)
                                ) {
                                    Text("Open in Google Maps")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
    val R = 6371.0 // Earth radius in km
    val dLat = Math.toRadians(lat2 - lat1)
    val dLon = Math.toRadians(lon2 - lon1)
    val a = sin(dLat / 2).pow(2.0) +
            cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
            sin(dLon / 2).pow(2.0)
    val c = 2 * atan2(sqrt(a), sqrt(1 - a))
    return R * c
}
