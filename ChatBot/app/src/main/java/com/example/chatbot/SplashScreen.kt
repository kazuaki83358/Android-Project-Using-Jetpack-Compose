package com.example.chatbot

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.chatbot.ui.theme.SplashBackground
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {
    val customFont = FontFamily(
        Font(R.font.font1, FontWeight.Bold)
    )
    val logo = painterResource(id = R.drawable.bot2)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SplashBackground),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = logo,
            contentDescription = "logo",
            modifier = Modifier.size(190.dp), // Set the size of the icon
            tint = Color.Unspecified // Ensure no tint is applied to the icon
        )
        Text(
            text = "Chat Bot",
            style = TextStyle(
                color = Color.LightGray,
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = customFont
            )
        )
    }
    LaunchedEffect(Unit) {
        delay(2000)
        navController.navigate("Home") {
            popUpTo("Splash") { inclusive = true }
        }
    }
}
