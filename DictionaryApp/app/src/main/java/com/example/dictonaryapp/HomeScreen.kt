package com.example.dictonaryapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@Composable
fun HomeScreen(navController: NavHostController){
    val anglina = FontFamily(
        Font(R.font.feel_free) // Replace with your font resource name
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFadb5bd)), // Dark background
        contentAlignment = Alignment.Center
    ){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Home page",
                color = Color.Black, // Text color
                fontSize = 50.sp, // Font size
                fontFamily = anglina, //Custom font
                fontWeight = FontWeight.Bold, // Font weight
                modifier = Modifier.padding(top = 16.dp) // Space between image and text
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Preview2() {
    val navController = rememberNavController()
    HomeScreen(navController = navController)
}