package com.example.animewallpaperapp

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.animewallpaperapp.Api.WallpaperInfoResponse
import com.example.animewallpaperapp.model.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, homeViewModel: HomeViewModel = remember { HomeViewModel() }) {
    var searchText by remember { mutableStateOf(TextFieldValue("")) }
    val images by remember { derivedStateOf { homeViewModel.images } }
    val loading by remember { derivedStateOf { homeViewModel.loading } }

    LaunchedEffect(searchText.text) {
        if (searchText.text.isNotEmpty()) {
            homeViewModel.searchImages(
                query = searchText.text,
                categories = "101",
                purity = "111",
                sorting = "relevance",
                order = "desc"
            )
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFF212529)
    ) {
        Column {
            TopAppBar(
                title = {
                    TextField(
                        value = searchText,
                        onValueChange = { searchText = it },
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
                            containerColor = Color.White,
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

            if (loading && images.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    contentPadding = PaddingValues(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    state = rememberLazyGridState().also { state ->
                        LaunchedEffect(state.firstVisibleItemIndex) {
                            val lastVisibleItem = state.layoutInfo.visibleItemsInfo.lastOrNull()
                            if (lastVisibleItem != null && lastVisibleItem.index == images.size - 1) {
                                homeViewModel.loadMoreImages()
                            }
                        }
                    }
                ) {
                    items(images) { wallpaper ->
                        ImageCard(
                            wallpaper = wallpaper,
                            onClick = {
                                wallpaper.id.let { id ->
                                    navController.navigate("download/$id")
                                } ?: run {
                                    // Log the error if wallpaper.id is null
                                    Log.e("HomeScreen", "Invalid imageId: ${wallpaper.id}")
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ImageCard(wallpaper: WallpaperInfoResponse, onClick: () -> Unit) {
    val imagePainter = rememberImagePainter(
        data = wallpaper.thumbs.large,
        builder = {
            crossfade(true)
            placeholder(R.drawable.placeholder) // Ensure this drawable exists
            error(R.drawable.error) // Ensure this drawable exists
        }
    )

    // Fixed size for the card
    val imageWidth = 300.dp  // Adjust width as needed
    val imageHeight = 200.dp // Adjust height as needed

    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .padding(8.dp)
            .width(imageWidth)
            .height(imageHeight)
            .clickable(onClick = onClick), // Handle click event
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Box(
            modifier = Modifier
                .width(imageWidth)
                .height(imageHeight)
        ) {
            Image(
                painter = imagePainter,
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
