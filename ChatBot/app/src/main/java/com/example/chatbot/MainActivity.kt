package com.example.chatbot

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.chatbot.model.ChatViewModel
import com.example.chatbot.ui.theme.ChatBotTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val chatViewModel = ViewModelProvider(this)[ChatViewModel::class.java]
        setContent {
            ChatBotTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "Splash") {
                    composable("Splash") {
                        SplashScreen(navController)
                    }
                    composable("Home") {
                        ChatPage()
                    }
                }
            }
        }
    }
}
