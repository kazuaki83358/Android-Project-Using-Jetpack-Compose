package com.example.emergencyassistance

import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

@Composable
fun checkLocationPermission(onPermissionGranted: () -> Unit) {
    val context = LocalContext.current

    val permissionStatus = ContextCompat.checkSelfPermission(
        context,
        android.Manifest.permission.ACCESS_FINE_LOCATION
    )

    // Handle permission request if not granted
    if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
        onPermissionGranted() // Permission granted, execute next steps
    } else {
        val permissionLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
            onResult = { isGranted ->
                if (isGranted) {
                    onPermissionGranted() // Proceed after granting permission
                } else {
                    // Handle permission denial (e.g., show a message to the user)
                }
            }
        )
        permissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
    }
}

@Composable
fun checkSmsPermission(onPermissionGranted: () -> Unit) {
    val context = LocalContext.current

    val permissionStatus = ContextCompat.checkSelfPermission(
        context,
        android.Manifest.permission.SEND_SMS
    )

    // Handle permission request if not granted
    if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
        onPermissionGranted() // Permission granted, execute next steps
    } else {
        val permissionLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
            onResult = { isGranted ->
                if (isGranted) {
                    onPermissionGranted() // Proceed after granting permission
                } else {
                    // Handle permission denial (e.g., show a message to the user)
                }
            }
        )
        permissionLauncher.launch(android.Manifest.permission.SEND_SMS)
    }
}
