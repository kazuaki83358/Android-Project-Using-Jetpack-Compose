package com.example.canvas_practice

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun Flower() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val centerX = size.width / 2
        val centerY = size.height / 2

        // Draw a circle in the center for the flower
        drawCircle(
            color = Color.Yellow,
            radius = 50f,
            center = Offset(centerX, centerY)
        )
        val petalRadius = 40f
        val petalOffset = 100f
        //upper one
        drawOval(
            color = Color.Magenta,
            size = Size(80f,150f),
            topLeft = Offset(centerX-45f,centerY-200f)
        )
        //bottom one
        drawOval(
            color = Color.Magenta,
            size = Size(80f,150f),
            topLeft = Offset(centerX-45f,centerY+45f)
        )
        //left one
        drawOval(
            color = Color.Magenta,
            size = Size(150f,80f),
            topLeft = Offset(centerX-200f,centerY-45f)
        )
        //right one
        drawOval(
            color = Color.Magenta,
            size = Size(150f,80f),
            topLeft = Offset(centerX+50f,centerY-45f)
        )
        //above left one
        rotate(degrees = 45f, pivot = Offset(centerX, centerY)) {
            drawOval(
                color = Color.Magenta,
                size = Size(150f, 80f),
                topLeft = Offset(centerX-200f,centerY-40f)
            )
        }
        rotate(degrees = -45f, pivot = Offset(centerX, centerY)) {
            drawOval(
                color = Color.Magenta,
                size = Size(150f, 80f),
                topLeft = Offset(centerX+50f,centerY-45f)
            )
        }
        //below left one
        rotate(degrees = -29f, pivot = Offset(centerX, centerY)) {
            drawOval(
                color = Color.Magenta,
                size = Size(150f, 80f),
                topLeft = Offset(centerX-200f,centerY-20f)
            )
        }
        //below right one
        rotate(degrees = 45f, pivot = Offset(centerX, centerY)) {
            drawOval(
                color = Color.Magenta,
                size = Size(150f, 80f),
                topLeft = Offset(centerX+50f,centerY-40f)
            )
        }
        //steam
        drawLine(
            color = Color.Black,
            start = Offset(centerX,centerY+190f),
            strokeWidth = 15f,
            end = Offset(centerX,centerY+850f)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun FlowerPreview() {
    Flower()
}
