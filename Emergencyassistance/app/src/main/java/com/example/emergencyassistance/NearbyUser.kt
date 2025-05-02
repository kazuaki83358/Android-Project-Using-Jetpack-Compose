package com.example.emergencyassistance

data class NearbyUser(
    val name: String,
    val latitude: Double,
    val longitude: Double
) {
    val locationLink: String
        get() = "https://www.google.com/maps/search/?api=1&query=$latitude,$longitude"
}
