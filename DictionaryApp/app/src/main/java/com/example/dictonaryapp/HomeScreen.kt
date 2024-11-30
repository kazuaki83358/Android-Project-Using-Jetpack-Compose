package com.example.dictonaryapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction

@Composable
fun HomeScreen(navController: NavHostController, viewModel: DictionaryViewModel) {
    var searchText by remember { mutableStateOf("") }

    // Collecting the search results and loading state from the ViewModel
    val searchResults by viewModel.searchResults.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    // Main Column layout that will contain search results
    Column(
        modifier = Modifier
            .fillMaxSize() // Ensure the column takes up the full screen
            .background(Color.White) // Set background to white
    ) {
        // Search bar layout - covering the full width and touching the top
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF0066FF)) // Blue color for search bar
                .padding(top = 25.dp, start = 16.dp, end = 16.dp, bottom = 3.dp) // Adjust top padding to zero
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Search Text Field with Rounded Corners
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(Color.White, shape = RoundedCornerShape(30.dp)) // Rounded corners
                        .padding(horizontal = 16.dp, vertical = 10.dp)
                ) {
                    BasicTextField(
                        value = searchText,
                        onValueChange = { searchText = it },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Search
                        ),
                        keyboardActions = KeyboardActions(
                            onSearch = {
                                viewModel.searchWord(searchText)
                            }
                        ),
                        textStyle = androidx.compose.ui.text.TextStyle(
                            fontSize = 18.sp,
                            color = Color.Black
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.Transparent) // Transparent background to see the rounded box
                    )
                }

                // Search Icon
                IconButton(onClick = {
                    viewModel.searchWord(searchText)
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_search_24),
                        contentDescription = "Search",
                        tint = Color.White // White icon for contrast
                    )
                }
            }
        }

        // Content Column to display search results, loading state, etc.
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 12.dp) // Adjust padding to avoid overlap with the search bar
        ) {
            // Display loading indicator if isLoading is true
            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(Alignment.Center) // Align in the center of the screen
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp)
                    )
                }
            }

            // LazyColumn to display search results
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 12.dp) // Adjust padding to avoid overlap with the search bar
            ) {
                items(searchResults) { result ->
                    // Display each word and its meanings
                    WordCard(result)

                    // Divider between results
                    Divider(
                        modifier = Modifier.padding(vertical = 8.dp),
                        color = Color.Gray,
                        thickness = 1.dp
                    )
                }
            }
        }
    }
}
@Composable
fun WordCard(dictionaryResult: DictionaryResult) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = 16.dp)
        .background(Color.White, shape = RoundedCornerShape(8.dp)) // Card-like background
    ) {
        // Word and phonetic
        Text(
            text = dictionaryResult.word,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(end = 2.dp) // Added right padding
        )
        Text(
            text = dictionaryResult.phonetic ?: "No phonetic available",
            fontSize = 16.sp,
            color = Color.Gray,
            modifier = Modifier.padding(end = 2.dp) // Added right padding
        )

        // Meaning(s)
        dictionaryResult.meanings.forEach { meaning ->
            Text(
                text = meaning.partOfSpeech,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(end = 2.dp) // Added right padding
            )
            meaning.definitions.forEach { definition ->
                Text(
                    text = definition.definition,
                    fontSize = 16.sp,
                    color = Color.Black,
                    modifier = Modifier.padding(end = 2.dp) // Added right padding
                )
                definition.example?.let {
                    Text(
                        text = "\"$it\"",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(end = 2.dp) // Added right padding
                    )
                }
                Spacer(modifier = Modifier.height(8.dp)) // Space between definitions
            }
        }
    }
}