package com.example.dictonaryapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Response

class DictionaryViewModel(private val wordDao: WordDao) : ViewModel() {

    // StateFlow to hold the search results
    private val _searchResults = MutableStateFlow<List<DictionaryResult>>(emptyList())
    val searchResults: StateFlow<List<DictionaryResult>> = _searchResults

    // StateFlow to track loading state
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    // Gson instance for JSON conversion
    private val gson = Gson()

    // Function to search for a word
    fun searchWord(query: String) {
        if (query.isBlank()) {
            _searchResults.value = emptyList() // Clear results if query is empty
            return
        }

        _isLoading.value = true // Start loading

        viewModelScope.launch {
            try {
                // Try to fetch cached data first
                val cachedWord = wordDao.getWord(query)
                if (cachedWord != null) {
                    // Use cached data if available
                    _searchResults.value = listOf(cachedWord.toDictionaryResult())
                } else {
                    // If not cached, fetch from API
                    fetchFromApi(query)
                }
            } catch (e: Exception) {
                // Handle errors like network issues, database issues, etc.
                _searchResults.value = emptyList()
            } finally {
                _isLoading.value = false // End loading
            }
        }
    }

    // Function to fetch word data from the API and cache it
    private suspend fun fetchFromApi(query: String) {
        try {
            val response: Response<List<DictionaryResult>> = RetrofitInstance.api.getWordMeaning(query)
            if (response.isSuccessful && response.body() != null) {
                val results = response.body()!!
                _searchResults.value = results

                // Cache the first result in the database
                val firstResult = results[0]
                wordDao.insertWord(firstResult.toWordEntity())
            } else {
                // Handle no results or unsuccessful API response
                _searchResults.value = emptyList()
            }
        } catch (e: Exception) {
            // Handle network or API errors
            _searchResults.value = emptyList()
        }
    }

    // Convert DictionaryResult to WordEntity
    private fun DictionaryResult.toWordEntity(): WordEntity {
        return WordEntity(
            word = this.word,
            phonetic = this.phonetic, // Can be null if not available
            meanings = gson.toJson(this.meanings), // Serialize meanings to JSON
            origin = this.origin // Can be null if not available
        )
    }

    // Convert WordEntity to DictionaryResult
    private fun WordEntity.toDictionaryResult(): DictionaryResult {
        val meaningsType = object : TypeToken<List<Meaning>>() {}.type
        val meanings: List<Meaning> = gson.fromJson(this.meanings, meaningsType)
        return DictionaryResult(
            word = this.word,
            phonetic = this.phonetic, // Return phonetic as-is (it can be null)
            phonetics = null, // Currently not handled in the cache, set to null for simplicity
            origin = this.origin, // Return origin as-is (it can be null)
            meanings = meanings
        )
    }
}
