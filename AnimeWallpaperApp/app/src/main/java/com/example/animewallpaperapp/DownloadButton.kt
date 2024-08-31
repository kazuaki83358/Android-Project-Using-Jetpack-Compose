package com.example.animewallpaperapp

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.TextStyle
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun DownloadButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .padding(16.dp)
            .size(width = 200.dp, height = 60.dp), // Increase the button size
        shape = RoundedCornerShape(
            topStart = 56.dp, // Cut corner from top-left
            bottomEnd = 56.dp // Cut corner from bottom-right
        ),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE)) // Customize button color
    ) {
        Text(
            text = "Download",
            style = TextStyle(
                fontFamily = feelFreeFontFamily,
                fontSize = 16.sp
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DownloadButtonPreview() {
    // Call the DownloadButton composable with a mock onClick action
    DownloadButton(
        onClick = {
            // Mock action for preview
        }
    )
}
