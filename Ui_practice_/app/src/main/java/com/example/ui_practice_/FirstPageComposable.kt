package com.example.ui_practice_

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui_practice_.R
import com.example.ui_practice_.ui.theme.BackgroundBlack

@Composable
fun FirstPage() {
    val customFontFamily = FontFamily(Font(R.font.angelina))
    val likeIcon: Painter = painterResource(id = R.drawable.like)
    val searchIcon : Painter = painterResource(id = R.drawable.search)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundBlack)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth().padding(top = 15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Instagram",
                style = TextStyle(
                    fontFamily = customFontFamily,
                    color = Color.White,
                    fontSize = 23.sp
                ),
                modifier = Modifier
                    .padding(12.dp)
                    .weight(1f) // Add padding to the right
            )

            IconButton(onClick = { /* Handle Like Click */ }) {
                Icon(
                    painter = likeIcon,
                    contentDescription = "Like",
                    tint = Color.White // Set icon color to white
                )
            }
            Spacer(modifier = Modifier.width(2.dp))

            IconButton(onClick = { /*TODO*/ }) {
                Icon(painter = searchIcon ,
                    contentDescription = "search",
                    tint = Color.White
                )
            }
            Spacer(modifier = Modifier.width(9.dp))
        }
        //now rest design should be out of row
    }
}

@Preview(showBackground = true)
@Composable
fun FirstPagePreview() {
    FirstPage()
}
