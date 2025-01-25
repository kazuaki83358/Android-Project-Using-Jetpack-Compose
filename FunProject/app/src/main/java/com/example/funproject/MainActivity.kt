package com.example.funproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StupidApp()
        }
    }
}

@Composable
fun StupidApp() {
    val customBlue = Color(0xFF2b2d42)
    val customCyan = Color(0xFF8a817c)
    val customButton1 = Color(0xFF70e000)
    val customButton2 = Color(0xFFd90429)
    val customFont = FontFamily(
        Font(R.font.poppin)
    )
    var displayText by remember { mutableStateOf("Are you stupid? \uD83E\uDD14") }
    var noButtonPosition by remember { mutableStateOf(Pair(0.dp, 0.dp)) }

    // Screen dimensions
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = Brush.verticalGradient(colors = listOf(customBlue, customCyan))),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.padding(bottom = 180.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = displayText,
                fontSize = 24.sp,
                color = Color.White,
                fontFamily = customFont
            )

            Spacer(modifier = Modifier.height(354.dp))

            Row {
                Button(
                    modifier = Modifier.width(150.dp),
                    shape = RoundedCornerShape(12.dp),
                    onClick = {
                        displayText = "I knew it \uD83D\uDE02"
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = customButton1)
                ) {
                    Text(
                        text = "Yes",
                        color = Color.White,
                        fontFamily = customFont
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Box(
                    modifier = Modifier.offset(
                        x = noButtonPosition.first,
                        y = noButtonPosition.second
                    )
                ) {
                    Button(
                        modifier = Modifier.width(150.dp),
                        shape = RoundedCornerShape(12.dp),
                        onClick = {
                            // Calculate bounds for the "No" button
                            val buttonWidth = 150.dp
                            val buttonHeight = 48.dp // Approx height for Material Buttons
                            val horizontalPadding = 16.dp // Padding between buttons
                            val bottomPadding = 180.dp // Respect the bottom padding of the Column

                            // Calculate max/min X and Y offsets
                            val maxXOffset = (screenWidth - buttonWidth) / 2 - horizontalPadding
                            val minXOffset = -maxXOffset
                            val maxYOffset = (screenHeight - buttonHeight - bottomPadding) / 2
                            val minYOffset = -maxYOffset

                            // Generate random offsets within bounds
                            val newX = Random.nextInt(
                                minXOffset.value.toInt(),
                                maxXOffset.value.toInt()
                            ).dp
                            val newY = Random.nextInt(
                                minYOffset.value.toInt(),
                                maxYOffset.value.toInt()
                            ).dp

                            noButtonPosition = Pair(newX, newY)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = customButton2)
                    ) {
                        Text(
                            text = "No",
                            color = Color.White,
                            fontFamily = customFont
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    StupidApp()
}
