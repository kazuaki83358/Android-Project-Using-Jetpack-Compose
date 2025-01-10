package com.example.firebase_firestore_practice.screen

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.firebase_firestore_practice.Person
import com.example.firebase_firestore_practice.R
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

private val personCollectionRef = Firebase.firestore.collection("person")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainApp(navController: NavController, modifier: Modifier = Modifier) {

    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var rollNumber by remember { mutableStateOf("") }
    var branchName by remember { mutableStateOf("") }

    val customBlue = Color(0xFF343a40)
    val customCyan = Color(0xFF212529)
    val buttonBlue = Color(0xFF006d77)

    val customFont = FontFamily(
        Font(R.font.poopin) // Replace with your actual font resource name
    )

    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(brush = Brush.verticalGradient(colors = listOf(customBlue, customCyan))),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.padding(top = 50.dp),
            text = "Welcome",
            fontSize = 42.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            fontFamily = customFont
        )

        Spacer(modifier = Modifier.height(20.dp))

        TextField(
            value = firstName,
            onValueChange = { firstName = it },
            label = { Text("First Name") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(Color(0x80FFFFFF), shape = RoundedCornerShape(12.dp)),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent
            )
        )

        TextField(
            value = lastName,
            onValueChange = { lastName = it },
            label = { Text("Last Name") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(Color(0x80FFFFFF), shape = RoundedCornerShape(12.dp)),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent
            )
        )

        TextField(
            value = rollNumber,
            onValueChange = { rollNumber = it },
            label = { Text("Roll Number") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(Color(0x80FFFFFF), shape = RoundedCornerShape(12.dp)),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent
            ),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number) // Ensures numeric input
        )

        TextField(
            value = branchName,
            onValueChange = { branchName = it },
            label = { Text("Branch Name") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(Color(0x80FFFFFF), shape = RoundedCornerShape(12.dp)),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent
            )
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                if (firstName.isBlank() || lastName.isBlank() || rollNumber.isBlank() || branchName.isBlank()) {
                    Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                } else {
                    val rollNumberInt = rollNumber.toIntOrNull() ?: 0 // Default to 0 if conversion fails
                    val person = Person(firstName, lastName, rollNumberInt, branchName)
                    savePerson(person, context)
                }
            },
            modifier = Modifier
                .height(50.dp)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = buttonBlue)
        ) {
            Text(
                text = "Sign in",
                color = Color.White,
                fontSize = 18.sp
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                navController.navigate("second_screen")
            },
            modifier = Modifier
                .height(50.dp)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = buttonBlue)
        ) {
            Text(
                text = "Retrieve Data",
                color = Color.White,
                fontSize = 18.sp
            )
        }
    }
}

private fun savePerson(person: Person, context: android.content.Context) = CoroutineScope(Dispatchers.IO).launch {
    try {
        personCollectionRef.add(person).await()
        withContext(Dispatchers.Main) {
            Toast.makeText(context, "Successfully saved data", Toast.LENGTH_LONG).show() // Use passed context
        }
    } catch (e: Exception) {
        withContext(Dispatchers.Main) {
            Toast.makeText(context, e.message, Toast.LENGTH_LONG).show() // Use passed context
        }
    }
}
