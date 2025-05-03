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
    name: String,
    isSOSActive: Boolean
) {
    val firestore = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()
    val uid = auth.currentUser?.uid ?: return

    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    // Get the latest location
    fusedLocationClient.lastLocation.addOnSuccessListener { location ->
        if (location != null) {
            val user = NearbyUser(
                uid = uid,
                name = name,
                latitude = location.latitude,
                longitude = location.longitude,
                isSOSActive = isSOSActive // Update SOS status here
            )

            firestore.collection("nearby_users")
                .document(uid)
                .set(user)
                .addOnSuccessListener {
                    Log.d("FirestoreUpdate", "Location and SOS status updated successfully.")
                }
                .addOnFailureListener { exception ->
                    Log.e("FirestoreUpdate", "Failed to update location and SOS status: ${exception.message}")
                }
        }
    }.addOnFailureListener { exception ->
        Log.e("LocationError", "Failed to retrieve last location: ${exception.message}")
    }
}
