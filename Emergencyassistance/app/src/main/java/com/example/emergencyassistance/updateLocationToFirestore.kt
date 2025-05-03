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
    val user = auth.currentUser ?: return // Make sure the user is logged in

    val uid = user.uid
    val name = user.displayName ?: "Unknown User" // Use displayName or a default if null

    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    // Get the latest location
    fusedLocationClient.lastLocation.addOnSuccessListener { location ->
        if (location != null) {
            val nearbyUser = NearbyUser(
                uid = uid,
                name = name,
                latitude = location.latitude,
                longitude = location.longitude,
                isSOSActive = isSOSActive
            )

            // Log the data to ensure it's correct before uploading
            Log.d("NearbyUserData", "User ID: $uid, Name: $name, Lat: ${location.latitude}, Long: ${location.longitude}, SOS Active: $isSOSActive")

            // Store the data in Firestore
            firestore.collection("nearby_users")
                .document(uid) // Use UID as the document ID
                .set(nearbyUser)
                .addOnSuccessListener {
                    Log.d("FirestoreUpdate", "Location and SOS status updated successfully.")
                }
                .addOnFailureListener { exception ->
                    Log.e("FirestoreUpdate", "Failed to update location and SOS status: ${exception.message}")
                }
        } else {
            Log.e("LocationError", "Location is null.")
        }
    }.addOnFailureListener { exception ->
        Log.e("LocationError", "Failed to retrieve last location: ${exception.message}")
    }
}
