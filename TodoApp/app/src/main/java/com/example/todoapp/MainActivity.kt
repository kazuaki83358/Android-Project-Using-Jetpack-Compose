package com.example.todoapp

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Navigation()
            }
        }
    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun Navigation(){
        val todoViewModel = ViewModelProvider(this)[TodoViewModel::class.java]
        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = "Splash") {
            composable("Splash"){
                SplashScreen(navController)
            }
            composable("Home"){
                TodoListPage(todoViewModel)
            }
        }
    }
}

