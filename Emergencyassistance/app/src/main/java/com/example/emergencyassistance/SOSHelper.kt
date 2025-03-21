package com.example.emergencyassistance

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.telephony.SmsManager
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import android.location.Location
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SOSHelper(private val context: Context) {

    fun triggerSOS(contact: String?) {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val fusedLocationClient: FusedLocationProviderClient =
                LocationServices.getFusedLocationProviderClient(context)
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                location?.let {
                    val lat = it.latitude
                    val lon = it.longitude
                    sendSOSMessage(contact, lat, lon)
                } ?: run {
                    CoroutineScope(Dispatchers.Main).launch {
                        Toast.makeText(context, "Could not get location.", Toast.LENGTH_SHORT).show()
                    }
                }
            }.addOnFailureListener { e ->
                Log.e("SOSHelper", "Failed to get location: ${e.message}")
                CoroutineScope(Dispatchers.Main).launch {
                    Toast.makeText(context, "Failed to get location.", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            CoroutineScope(Dispatchers.Main).launch {
                Toast.makeText(context, "Location permission is required.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun sendSOSMessage(contact: String?, lat: Double, lon: Double) {
        CoroutineScope(Dispatchers.IO).launch {
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.SEND_SMS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                if (contact != null) {
                    try {
                        val locationUrl = "https://www.google.com/maps/search/?api=1&query=$lat,$lon"
                        val message = "S.O.S. Emergency! I am at the following location: $locationUrl. Please help!"

                        val smsManager = SmsManager.getDefault()
                        smsManager.sendTextMessage(contact, null, message, null, null)

                        withContext(Dispatchers.Main) {
                            Toast.makeText(context, "SOS message sent to $contact", Toast.LENGTH_SHORT).show()
                        }

                    } catch (e: SecurityException) {
                        Log.e("SOSHelper", "SecurityException during SMS sending: ${e.message}")
                        withContext(Dispatchers.Main) {
                            Toast.makeText(context, "Failed to send SOS message due to security issues.", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        Log.e("SOSHelper", "Failed to send SOS message: ${e.message}")
                        withContext(Dispatchers.Main) {
                            Toast.makeText(context, "Failed to send SOS message.", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "Contact number is null.", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "SMS permission is required", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}