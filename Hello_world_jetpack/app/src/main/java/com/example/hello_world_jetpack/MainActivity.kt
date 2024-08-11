package com.example.hello_world_jetpack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
           Greeting(name = "Nishant")
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(
        text = "Hello $name !",
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Greeting(name = "Nishant")
}
