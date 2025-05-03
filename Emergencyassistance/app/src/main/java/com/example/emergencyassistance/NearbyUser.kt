package com.example.emergencyassistance

data class NearbyUser(
    val uid: String = "",
    val name: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val isSOSActive: Boolean = false
) {
    val locationLink: String
        get() = "https://www.google.com/maps/search/?api=1&query=$latitude,$longitude"
}
