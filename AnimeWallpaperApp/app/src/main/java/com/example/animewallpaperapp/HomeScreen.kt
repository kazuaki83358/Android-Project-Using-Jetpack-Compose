package com.example.animewallpaperapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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
    val searchText = remember { mutableStateOf(TextFieldValue("")) }
    val images by remember { derivedStateOf { homeViewModel.images } }
    val loading by remember { derivedStateOf { homeViewModel.loading } }

    LaunchedEffect(searchText.value.text) {
        homeViewModel.searchImages(searchText.value.text)
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFF212529)
    ) {
        Column {
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
                LazyColumn(
                    contentPadding = PaddingValues(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    state = rememberLazyListState().also { state ->
                        LaunchedEffect(state.firstVisibleItemIndex) {
                            val lastVisibleItem = state.layoutInfo.visibleItemsInfo.lastOrNull()
                            if (lastVisibleItem != null && lastVisibleItem.index == images.size - 1) {
                                homeViewModel.loadMoreImages()
                            }
                        }
                    }
                ) {
                    items(images) { wallpaper ->
                        ImageCard(wallpaper = wallpaper)
                    }
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

    // Use fixed size or proportional size
    val imageWidth = 300.dp  // Adjust width as needed
    val imageHeight = 200.dp // Adjust height as needed

    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .padding(8.dp)
            .width(imageWidth)
            .height(imageHeight),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Box(
            modifier = Modifier
                .width(imageWidth)
                .height(imageHeight)
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