package com.example.todoapp

import android.window.SplashScreen
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController){
    val customFont = FontFamily(
        Font(R.font.font1,FontWeight.Bold)
    )
   Column(
       modifier = Modifier
           .fillMaxSize()
           .padding(top = 280.dp),
       horizontalAlignment = Alignment.CenterHorizontally
   ) {
       Image(painter = painterResource(id = R.drawable.todo), contentDescription = "todo")

       Text(text = "Todo List", modifier = Modifier.padding(top = 10.dp),
           style = TextStyle(
               fontSize = 20.sp,
               fontWeight = FontWeight.Bold,
               fontFamily = customFont
           )
       )

       LaunchedEffect(Unit) {
           delay(2000)
           navController.navigate("Home"){
               popUpTo("Splash"){inclusive = true}
           }
       }
   }
}

