package com.example.emergencyassistance

import android.Manifest
import android.content.Context
import android.util.Log
import androidx.annotation.RequiresPermission
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
fun updateLocationToFirestore(
    context: Context,
    isSOSActive: Boolean
) {
    val firestore = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()
    val user = auth.currentUser ?: return

    val uid = user.uid
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    // Step 1: Fetch user name from Firestore
    firestore.collection("users").document(uid).get()
        .addOnSuccessListener { document ->
            val name = document.getString("name") ?: "Unknown User"

            // Step 2: Get location
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    val nearbyUser = NearbyUser(
                        uid = uid,
                        name = name,
                        latitude = location.latitude,
                        longitude = location.longitude,
                        isSOSActive = isSOSActive
                    )

                    // Log
                    Log.d("NearbyUserData", "User ID: $uid, Name: $name, Lat: ${location.latitude}, Long: ${location.longitude}, SOS Active: $isSOSActive")

                    // Step 3: Upload to Firestore
                    firestore.collection("nearby_users")
                        .document(uid)
                        .set(nearbyUser)
                        .addOnSuccessListener {
                            Log.d("FirestoreUpdate", "Location and SOS status updated successfully.")
                        }
                        .addOnFailureListener { exception ->
                            Log.e("FirestoreUpdate", "Failed to update: ${exception.message}")
                        }
                } else {
                    Log.e("LocationError", "Location is null.")
                }
            }.addOnFailureListener { exception ->
                Log.e("LocationError", "Failed to retrieve location: ${exception.message}")
            }
        }
        .addOnFailureListener { exception ->
            Log.e("UserNameFetchError", "Failed to fetch user name: ${exception.message}")
        }
}
