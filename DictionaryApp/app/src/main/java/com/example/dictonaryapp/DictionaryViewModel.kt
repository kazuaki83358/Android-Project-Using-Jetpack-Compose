package com.example.dictonaryapp

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Response

class DictionaryViewModel : ViewModel() {
    // StateFlow to hold the search results
    private val _searchResults = MutableStateFlow<List<DictionaryResult>>(emptyList())
    val searchResults: StateFlow<List<DictionaryResult>> = _searchResults

    // StateFlow to track loading state
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    // Function to search for a word from the API
    fun searchWord(query: String) {
        if (query.isNotBlank()) {
            _isLoading.value = true // Set loading to true while searching
            viewModelScope.launch {
                try {
                    // Making the API call
                    val response: Response<List<DictionaryResult>> = RetrofitInstance.api.getWordMeaning(query)

                    if (response.isSuccessful && response.body() != null) {
                        // Update the _searchResults with the successful API response
                        _searchResults.value = response.body()!!
                    } else {
                        _searchResults.value = emptyList() // No results
                    }
                } catch (e: Exception) {
                    // Handle network or other errors
                    _searchResults.value = emptyList()
                } finally {
                    _isLoading.value = false // Set loading to false after the API call completes
                }
            }
        } else {
            // Clear the results if query is blank
            _searchResults.value = emptyList()
        }
    }
}
