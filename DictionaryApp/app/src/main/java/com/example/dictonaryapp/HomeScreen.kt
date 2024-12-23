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
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Search bar layout - covering the full width and touching the top
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF0066FF))
                .padding(top = 25.dp, start = 16.dp, end = 16.dp)
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
                        .background(Color.White, shape = RoundedCornerShape(30.dp))
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
                            .background(Color.Transparent)
                    )
                }

                // Search Icon
                IconButton(onClick = {
                    viewModel.searchWord(searchText)
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_search_24),
                        contentDescription = "Search",
                        tint = Color.White
                    )
                }
            }
        }

        // Content Column to display search results, loading state, etc.
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 12.dp)
        ) {
            // Display loading indicator if isLoading is true
            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(Alignment.Center)
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
                    .padding(top = 12.dp)
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
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
            .background(Color.White, shape = RoundedCornerShape(8.dp))
            .padding(16.dp) // Padding inside the card
    ) {
        // Word and phonetic
        Text(
            text = dictionaryResult.word,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Text(
            text = dictionaryResult.phonetic ?: "No phonetic available",
            fontSize = 16.sp,
            color = Color.Gray
        )

        // Meaning(s)
        dictionaryResult.meanings.forEach { meaning ->
            Spacer(modifier = Modifier.height(8.dp)) // Space between meanings
            Text(
                text = meaning.partOfSpeech,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            meaning.definitions.forEach { definition ->
                Text(
                    text = definition.definition,
                    fontSize = 16.sp,
                    color = Color.Black
                )
                definition.example?.let {
                    Text(
                        text = "\"$it\"",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
                Spacer(modifier = Modifier.height(8.dp)) // Space between definitions
            }
        }
    }
}
