package com.example.emergencyassistance

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "emergency_contacts")
data class EmergencyContact(
    @PrimaryKey(autoGenerate = true) val id: Long = 0, // Primary key with auto-generation
    val name: String,
    val phoneNumber: String
)
