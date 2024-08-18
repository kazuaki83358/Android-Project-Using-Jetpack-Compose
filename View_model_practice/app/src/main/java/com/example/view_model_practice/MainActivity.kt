package com.example.view_model_practice

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import com.example.view_model_practice.model.RandomNumberViewModel
import com.example.view_model_practice.model.RandomNumberViewModelFactory
import com.example.view_model_practice.ui.theme.View_model_practiceTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel : RandomNumberViewModel = ViewModelProvider(
                this,RandomNumberViewModelFactory())[RandomNumberViewModel::class.java]
            FirstPage(viewModel)
        }
    }
}
