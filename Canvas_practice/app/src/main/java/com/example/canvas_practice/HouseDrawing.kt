package com.example.canvas_practice

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path

@Composable
fun HouseDrawing(){
    Column(modifier = Modifier.fillMaxSize()) {
        Canvas(modifier = Modifier.size(500.dp)) {
            val height = size.height
            val width = size.width
            //rectangle
            drawRect(
                color = Color.DarkGray,
                size = Size(width / 2, height / 3),
                topLeft = Offset(width / 4, height / 2)
            )
            //roof triangle
            drawPath(
                path = Path().apply {
                    moveTo(width/2 , height/3)
                    lineTo(width/4 , height/2)
                    lineTo(3*width/4,height/2)
                    close()
                },
                color = Color.Red
            )
            //gate for house
            val gate_height = height/6
            val gate_width = width/6
            drawRect(
                color = Color.Gray,
                size = Size(gate_width, gate_height),
                topLeft = Offset(
                    width/2 - gate_width/2,
                    height/2 + height/3 - gate_height
                )
            )
            val window_height = height/10
            val window_width = width/8
            drawRect(
                color = Color.Gray,
                size = Size(window_width, window_height),
                topLeft = Offset(
                    width/6 + width/10,
                    height/2 + height/15
                )
            )
            drawRect(
                color = Color.Gray,
                size = Size(window_width, window_height),
                topLeft = Offset(
                    width/2 + width/10,
                    height/2 + height/15
                )
            )
            drawCircle(
                color = Color.Yellow,
                radius = size.minDimension / 8,
                center = Offset(width * 0.85f , height * 0.15f)
                )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Preview(){
    HouseDrawing()
}
