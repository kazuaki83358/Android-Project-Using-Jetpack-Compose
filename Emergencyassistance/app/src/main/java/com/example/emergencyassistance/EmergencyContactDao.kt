package com.example.emergencyassistance

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface EmergencyContactDao {
    // Insert a contact
    @Insert
    suspend fun insertContact(contact: EmergencyContact)

    // Delete a contact
    @Delete
    suspend fun deleteContact(contact: EmergencyContact)

    // Get all contacts
    @Query("SELECT * FROM emergency_contacts")
    suspend fun getAllContacts(): List<EmergencyContact>
}


