package com.example.animewallpaperapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.rememberImagePainter
import com.example.animewallpaperapp.Api.WallpaperInfoResponse
import com.example.animewallpaperapp.model.HomeViewModel
import kotlinx.coroutines.flow.firstOrNull

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(homeViewModel: HomeViewModel = remember { HomeViewModel() }) {
    // State to hold the text of the search bar
    val searchText = remember { mutableStateOf(TextFieldValue("")) }
    val images by remember { derivedStateOf { homeViewModel.images } }
    val loading by remember { derivedStateOf { homeViewModel.loading } }

    // Update images when viewModel images change
    LaunchedEffect(homeViewModel.images) {
        // Optional: Handle side-effects when images update
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFF212529) // Background color matching splash screen
    ) {
        Column {
            // Top App Bar with Search TextField
            TopAppBar(
                title = {
                    TextField(
                        value = searchText.value,
                        onValueChange = { searchText.value = it },
                        placeholder = { Text("Search wallpapers...", color = Color.Gray) },
                        trailingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.search),
                                contentDescription = "Search Icon",
                                tint = Color.Black
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 8.dp, end = 12.dp)
                            .clip(RoundedCornerShape(18.dp)),
                        singleLine = true,
                        maxLines = 1,
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = Color.White, // Use containerColor instead of backgroundColor
                            focusedIndicatorColor = Color.Black,
                            unfocusedIndicatorColor = Color.Gray,
                            cursorColor = Color.Black
                        )
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF212529),
                    titleContentColor = Color.White
                )
            )

            // Display loading spinner or single image
            if (loading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                val image = images.firstOrNull() // Display the first image if available
                image?.let {
                    ImageCard(wallpaper = it)
                } ?: Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "No images available", color = Color.White)
                }
            }
        }
    }
}

@Composable
fun ImageCard(wallpaper: WallpaperInfoResponse) {
    val imagePainter = rememberImagePainter(
        data = wallpaper.thumbs.large,
        builder = {
            crossfade(true)
            placeholder(R.drawable.placeholder) // Ensure this drawable exists
            error(R.drawable.error) // Ensure this drawable exists
        }
    )

    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .height(200.dp), // Adjust height as needed
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp) // Adjust height as needed
        ) {
            AsyncImage(
                model = wallpaper.thumbs.large,
                contentDescription = "Wallpaper Image",
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
        }
        Text(
            text = wallpaper.id,
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            color = Color.White
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}