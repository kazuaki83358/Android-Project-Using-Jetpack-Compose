package com.example.animewallpaperapp.model

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.animewallpaperapp.Api.WallhavenApi
import com.example.animewallpaperapp.Api.WallpaperInfoResponse
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val _images = mutableStateListOf<WallpaperInfoResponse>()
    val images: List<WallpaperInfoResponse> get() = _images

    private val _loading = mutableStateOf(false)
    val loading: Boolean get() = _loading.value

    init {
        fetchImages()
    }

    fun searchImages(query: String) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val response = WallhavenApi.service.searchWallpapers(query = query)
                println("API Response: ${response.data}") // Debugging line
                _images.clear()
                _images.addAll(response.data)
            } catch (e: Exception) {
                println("Error fetching images: ${e.message}") // Debugging line
            } finally {
                _loading.value = false
            }
        }
    }

    private fun fetchImages() {
        searchImages("Demon Slayer") // Fetch default images or initial set
    }
}
