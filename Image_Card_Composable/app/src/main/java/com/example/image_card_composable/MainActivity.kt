package com.example.image_card_composable

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.image_card_composable.ui.theme.Image_Card_ComposableTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val painter = painterResource(id = R.drawable.luffy)
            val descriptor = "luffy"
            val title = "Luffy Pirate \t\t\t king"
            Box(modifier = Modifier
                .fillMaxWidth(0.5f)
                .padding(16.dp)){
                ImageCard(painter = painter, contentDescriptor = descriptor, title = title)
            }
        }
    }
}

@Composable
fun ImageCard(
    painter: Painter,
    contentDescriptor: String,
    title:String,
    modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .width(150.dp)
            .padding(16.dp),
        shape = RoundedCornerShape(15.dp),
        elevation = CardDefaults.cardElevation(5.dp)
    ) {
        Box(modifier = Modifier.height(200.dp)){
            Image(painter = painter, contentDescription = contentDescriptor, contentScale = ContentScale.Crop )
            Box (
                modifier = Modifier.fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black
                            ),
                            startY = 300f
                        )
                    )
            ){

            }

            Box(modifier = Modifier
                .fillMaxSize()
                .padding(12.dp), contentAlignment = Alignment.BottomStart){
                Text(title, style = TextStyle(
                    color = Color.White,
                    fontSize = 16.sp
                ))

            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Image_Card_ComposableTheme {
        val painter = painterResource(id = R.drawable.luffy)
        ImageCard(
            painter = painter,
            contentDescriptor = "Sample Image",
            title = "Luffy Pirate \t\t\t king"
        )
    }
}