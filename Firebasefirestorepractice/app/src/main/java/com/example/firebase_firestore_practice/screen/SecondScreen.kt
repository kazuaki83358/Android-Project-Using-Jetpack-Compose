package com.example.firebase_firestore_practice.screen

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.firebase_firestore_practice.Person
import com.example.firebase_firestore_practice.R
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

@Composable
fun SecondScreen(modifier: Modifier = Modifier) {
    val customBlue = Color(0xFF343a40)
    val customCyan = Color(0xFF212529)
    val customFont = FontFamily(Font(R.font.poopin))
    val context = LocalContext.current

    val personCollectionRef = Firebase.firestore.collection("person")

    // Using mutableStateListOf to manage the state of person list
    val personList = remember { mutableStateListOf<Person>() }

    // Use a Firestore listener to listen for real-time updates
    LaunchedEffect(Unit) {
        personCollectionRef.addSnapshotListener { querySnapshot, exception ->
            if (exception != null) {
                Log.e("Firestore", "Error getting documents: ", exception)
                Toast.makeText(context, "Error retrieving data: ${exception.message}", Toast.LENGTH_LONG).show()
                return@addSnapshotListener
            }

            if (querySnapshot != null) {
                // Clear existing list and add new data
                personList.clear()
                for (document in querySnapshot.documents) {
                    val person = document.toObject<Person>()
                    Log.d("Firestore", "Person: ${person?.firstName} ${person?.lastName}")
                    if (person != null) {
                        personList.add(person)
                    }
                }
            } else {
                Log.d("Firestore", "No data found")
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(brush = Brush.verticalGradient(colors = listOf(customBlue, customCyan))),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.padding(top = 50.dp),
            text = "Retrieved Data",
            fontSize = 42.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            fontFamily = customFont
        )

        // Display the data using LazyColumn
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(personList) { person ->
                // For each item in the personList, display a Card
                PersonItem(person = person)
            }
        }
    }
}

@Composable
fun PersonItem(person: Person) {
    // Display individual person's data in a Card
    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
            .background(Color.Gray.copy(alpha = 0.2f)),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Name: ${person.firstName} ${person.lastName}",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Text(
                text = "Roll Number: ${person.rollNumber}",
                fontSize = 16.sp,
                color = Color.Black
            )
            Text(
                text = "Branch: ${person.branchName}",
                fontSize = 16.sp,
                color = Color.Black
            )
        }
    }
}
