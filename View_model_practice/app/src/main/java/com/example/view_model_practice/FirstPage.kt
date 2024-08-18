package com.example.view_model_practice

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.view_model_practice.model.RandomNumberViewModel
import com.example.view_model_practice.ui.theme.BackgroundBlack

@Composable
fun FirstPage(viewModel: RandomNumberViewModel){
    var number by remember {
        mutableIntStateOf(viewModel.randomNumber)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundBlack),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "$number",
            style = TextStyle(
                fontStyle = FontStyle.Italic,
                fontSize = 35.sp,
                color = Color.White
            )
        )
        Spacer(modifier = Modifier.height(120.dp))
        Button(onClick = {
            viewModel.generateRandomNumber()
            number = viewModel.randomNumber
        }, colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF00a5cf),
            contentColor = Color.White
        )) {
            Text(text = "Random",
                style = TextStyle(
                    fontStyle = FontStyle.Italic,
                    fontSize = 20.sp,
                    color = Color.White
                ))
        }
    }
}
