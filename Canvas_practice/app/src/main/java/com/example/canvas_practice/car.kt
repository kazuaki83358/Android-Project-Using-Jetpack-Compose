package com.example.canvas_practice

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun AnimatedCar() {
    val infiniteTransition = rememberInfiniteTransition()
    val carPosition by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 5000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val height = size.height
        val width = size.width

        val carX = carPosition * width * 1.2f - width * 0.2f // Adjust for smooth entry/exit

        // Car Body
        drawRoundRect(
            color = Color.Red,
            size = Size(width / 2 - 10f, height / 9 - 80f),
            topLeft = Offset(carX + width / 4, height / 2 - 350f),
            cornerRadius = CornerRadius(50f, 50f)
        )

        drawRoundRect(
            color = Color.Red,
            size = Size(width / 2 + 350f, height / 9 - 50f),
            topLeft = Offset(carX + width / 9 - 40f, height / 2 - 200f),
            cornerRadius = CornerRadius(30f, 30f)
        )

        // Window
        drawRoundRect(
            color = Color.LightGray,
            size = Size(width / 7, height / 12 - 70f),
            topLeft = Offset(carX + width / 3 + 250f, height / 2 - 330f),
            cornerRadius = CornerRadius(20f, 20f)
        )

        // Wheels
        drawCircle(
            color = Color.Gray,
            radius = 100f,
            center = Offset(carX + width / 4, height / 2)
        )

        drawCircle(
            color = Color.Black,
            radius = 50f,
            center = Offset(carX + width / 4, height / 2)
        )

        drawCircle(
            color = Color.Gray,
            radius = 100f,
            center = Offset(carX + width - 300f, height / 2)
        )

        drawCircle(
            color = Color.Black,
            radius = 50f,
            center = Offset(carX + width - 300f, height / 2)
        )

        //Road
        drawLine(
            color = Color.Black,
            start = Offset(carX + width / 5 - 2500f, height / 2 + 100f),
            end = Offset(width , height/2+100f),
            strokeWidth = 15f,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CarPreview() {
    AnimatedCar() // Use AnimatedCar for preview
}