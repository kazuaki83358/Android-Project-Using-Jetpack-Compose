package com.example.animewallpaperapp

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage

// Define the FontFamily using the 'feel_free' font
val feelFreeFontFamily = FontFamily(
    Font(R.font.feel_free) // Ensure 'feel_free' is the correct font file name
)

@Composable
fun Download(imageId: String?, navController: NavController) {
    if (imageId == null) {
        // Handle the case where imageId is null
        Log.e("Download", "imageId is null")
        return
    }

    // Construct the image URL correctly using the imageId
    val imageUrl = "https://w.wallhaven.cc/full/${imageId.take(2)}/wallhaven-$imageId.jpg"

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFF212529)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
            ) {
                // Use Coil's AsyncImage for loading the image
                AsyncImage(
                    model = imageUrl,
                    contentDescription = "Full Size Wallpaper",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit,
                    placeholder = painterResource(id = R.drawable.placeholder), // Ensure it's a supported format
                    error = painterResource(id = R.drawable.error) // Ensure it's a supported format
                )
            }
            DownloadButton(
                onClick = {
                    // Handle download action here
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDownload() {
    // Create a mock NavController for preview
    val navController = rememberNavController()

    // Provide a non-null imageId for preview
    Download(imageId = "12345", navController = navController)
}
