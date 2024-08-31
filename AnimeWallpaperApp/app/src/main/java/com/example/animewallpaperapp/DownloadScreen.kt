package com.example.animewallpaperapp

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

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

    val imageUrl = "https://w.wallhaven.cc/full/${imageId.take(2)}/wallhaven-$imageId.jpg"
    val context = LocalContext.current
    var downloadTriggered by remember { mutableStateOf(false) }

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
                    .fillMaxWidth()
                    .padding(top = 55.dp) // Add top padding here
            ) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = "Full Size Wallpaper",
                    modifier = Modifier.fillMaxWidth(), // Ensure the image takes only the required width
                    contentScale = ContentScale.Fit,
                    placeholder = painterResource(id = R.drawable.placeholder),
                    error = painterResource(id = R.drawable.error)
                )
            }
            // Add a Text composable for your name
            Text(
                text = "Go Ahead",
                color = Color.White,
                fontFamily = feelFreeFontFamily,
                style = TextStyle(fontSize = 46.sp),
                modifier = Modifier.padding(bottom = 16.dp)
            )
            // Add a Text composable below the image for download status
            Text(
                text = if (downloadTriggered) "Downloading..." else "",
                color = Color.White,
                style = TextStyle(fontSize = 16.sp),
                modifier = Modifier.padding(bottom = 16.dp)
            )
            // Pass the imageId to CustomButton and handle click state
            CustomButton(imageId, context, onDownloadTriggered = {
                downloadTriggered = true
                Toast.makeText(context, "Downloaded", Toast.LENGTH_SHORT).show()
            })
        }
    }
}

@Composable
fun CustomButton(imageId: String?, context: Context, onDownloadTriggered: () -> Unit) {
    var downloadTriggered by remember { mutableStateOf(false) }

    // Trigger download logic based on the state
    LaunchedEffect(downloadTriggered) {
        if (downloadTriggered && imageId != null) {
            downloadImage(imageId, context)
        }
    }

    Button(
        onClick = {
            downloadTriggered = true
            onDownloadTriggered() // Call the provided callback
        },
        modifier = Modifier
            .padding(16.dp)
            .wrapContentSize()
            .then(Modifier.padding(horizontal = 32.dp)),
        shape = RoundedCornerShape(
            topStart = 0.dp,
            topEnd = 40.dp,
            bottomStart = 40.dp,
            bottomEnd = 0.dp
        ),
        colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
    ) {
        Text(
            text = "Download",
            style = TextStyle(fontSize = 16.sp, color = Color.White)
        )
    }
}

private suspend fun downloadImage(imageId: String, context: Context) {
    // Perform the download in a coroutine
    val imageUrl = "https://w.wallhaven.cc/full/${imageId.take(2)}/wallhaven-$imageId.jpg"
    val client = OkHttpClient()
    val request = Request.Builder().url(imageUrl).build()

    try {
        val response = withContext(Dispatchers.IO) { client.newCall(request).execute() }
        if (!response.isSuccessful) throw IOException("Failed to download image")

        val inputStream = response.body?.byteStream()
        val bitmap = BitmapFactory.decodeStream(inputStream)
        saveImageToGallery(context, bitmap)
    } catch (e: IOException) {
        e.printStackTrace() // Handle error appropriately
    }
}

private fun saveImageToGallery(context: Context, bitmap: Bitmap) {
    val contentResolver = context.contentResolver
    val contentValues = ContentValues().apply {
        put(MediaStore.Images.Media.DISPLAY_NAME, "downloaded_image.jpg")
        put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
    }

    val uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
    uri?.let {
        contentResolver.openOutputStream(it).use { outputStream ->
            if (outputStream != null) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            }
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
