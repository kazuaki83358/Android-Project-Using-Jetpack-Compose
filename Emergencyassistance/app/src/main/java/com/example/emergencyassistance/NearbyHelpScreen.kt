package com.example.emergencyassistance

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NearbyHelpScreen(navController: NavController) {
    val context = LocalContext.current
    val nearbyUsers = remember { mutableStateListOf<NearbyUser>() }

    fun fetchNearbyUsers() {
        nearbyUsers.clear()
        nearbyUsers.add(NearbyUser("John Doe", 28.704060, 77.102493))
    }

    LaunchedEffect(Unit) {
        fetchNearbyUsers()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nearby Help") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFf20089)
                )
            )
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Users within 2 km", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))

                if (nearbyUsers.isEmpty()) {
                    Text("No users nearby.", fontSize = 16.sp)
                } else {
                    LazyColumn {
                        items(nearbyUsers) { user ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .padding(16.dp)
                                        .fillMaxWidth(),
                                    horizontalAlignment = Alignment.Start
                                ) {
                                    Text("Name: ${user.name}", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                                    Text("Location: ${user.latitude}, ${user.longitude}", fontSize = 16.sp)

                                    Button(
                                        onClick = {
                                            val uri = Uri.parse(user.locationLink)
                                            val intent = Intent(Intent.ACTION_VIEW, uri)
                                            context.startActivity(intent)
                                        },
                                        modifier = Modifier.padding(top = 8.dp)
                                    ) {
                                        Text("Open in Google Maps")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    )
}
