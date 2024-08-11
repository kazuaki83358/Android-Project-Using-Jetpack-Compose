package com.example.basic_sizing_row_column

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.basic_sizing_row_column.ui.theme.Basic_sizing_row_columnTheme
import kotlin.random.Random

class SecondActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Greeting2()
        }
    }
}

@Composable
fun Greeting2() {
    // State to hold the text value
    val textState = remember { mutableStateOf("0.0") }

    // Custom font setup
    val customFont = FontFamily(
        Font(R.font.font1, FontWeight.Normal)
    )
    //Custom color
    val myCustomColor = Color(0xFF2f4550)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(myCustomColor),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = textState.value, // Use the text state value
            style = TextStyle(
                fontSize = 20.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontFamily = customFont
            ),
            modifier = Modifier.padding(bottom = 20.dp) // Adjust padding
        )
        Button(onClick = {
            // Update the text state with a random value
            textState.value = Random.nextInt(0, 1000).toString()
        }, modifier = Modifier.padding(top = 50.dp)) {
            Text(text = "Random",
                style = TextStyle(
                    fontSize = 15.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                ))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    Basic_sizing_row_columnTheme {
        Greeting2()
    }
}