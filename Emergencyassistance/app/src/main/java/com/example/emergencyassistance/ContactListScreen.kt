package com.example.emergencyassistance

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun ContactListScreen(navController: NavController) {
    val context = LocalContext.current
    val contactList = remember { mutableStateListOf<EmergencyContact>() }

    // Load contacts from Room when the screen is displayed
    LaunchedEffect(Unit) {
        loadContacts(context, contactList)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Emergency Contacts",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Display the list of contacts
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(contactList) { contact ->
                ContactItem(contact = contact, onDelete = {
                    deleteContact(context, contact, contactList)
                })
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Button to navigate back to Add Emergency Contact screen
        Button(
            onClick = { navController.popBackStack() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Back")
        }
    }
}

@Composable
fun ContactItem(contact: EmergencyContact, onDelete: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(text = contact.name, fontWeight = FontWeight.Bold)
            Text(text = contact.phoneNumber)
        }

        // Delete Button
        IconButton(onClick = { onDelete() }) {
            Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete Contact")
        }
    }
}

fun loadContacts(context: Context, contactList: MutableList<EmergencyContact>) {
    val db = AppDatabase.getDatabase(context)
    val emergencyContactDao = db.emergencyContactDao()

    CoroutineScope(Dispatchers.IO).launch {
        val contacts = emergencyContactDao.getAllContacts()
        withContext(Dispatchers.Main) {
            contactList.clear()
            contactList.addAll(contacts)
        }
    }
}

fun deleteContact(context: Context, contact: EmergencyContact, contactList: MutableList<EmergencyContact>) {
    val db = AppDatabase.getDatabase(context)
    val emergencyContactDao = db.emergencyContactDao()

    CoroutineScope(Dispatchers.IO).launch {
        emergencyContactDao.deleteContact(contact)
        withContext(Dispatchers.Main) {
            contactList.remove(contact)
            Toast.makeText(context, "Contact Deleted", Toast.LENGTH_SHORT).show()
        }
    }
}
