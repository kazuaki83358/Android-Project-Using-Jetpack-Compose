package com.example.dictonaryapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.dictonaryapp.ui.theme.DictonaryAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            // Setup the navigation graph
            SetupNavGraph(navController = navController)
        }
    }
}

@Composable
fun SetupNavGraph(navController: NavHostController) {
    // Create an instance of the DictionaryViewModel
    val dictionaryViewModel: DictionaryViewModel = viewModel()

    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") {
            // SplashScreen that navigates to "home" after 2 seconds
            SplashScreen(navController = navController)
        }
        composable("home") {
            // Pass the viewModel to HomeScreen
            HomeScreen(navController = navController, viewModel = dictionaryViewModel)
        }
    }
}
