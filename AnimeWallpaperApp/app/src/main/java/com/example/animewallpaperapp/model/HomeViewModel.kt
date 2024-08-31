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

    private var currentPage = 1
    private var lastPage = 1
    private var query = "anime"
    private var categories: String? = null
    private var purity: String? = null
    private var sorting: String? = null
    private var order: String? = "asc"

    init {
        fetchImages()
    }

    fun searchImages(
        query: String,
        categories: String? = "101",
        purity: String? = "111",
        sorting: String? = "relevance",
        order: String? = "desc"
    ) {
        this.query = query
        this.categories = categories
        this.purity = purity
        this.sorting = sorting
        this.order = order
        currentPage = 1
        _images.clear()
        fetchImages()
    }

    fun loadMoreImages() {
        if (loading || currentPage >= lastPage) return
        currentPage++
        fetchImages()
    }

    private fun fetchImages() {
        viewModelScope.launch {
            _loading.value = true
            try {
                val response = WallhavenApi.service.searchWallpapers(
                    query = query,
                    page = currentPage,
                    categories = categories,
                    purity = purity,
                    sorting = sorting,
                    order = order
                )
                lastPage = response.meta.lastPage
                _images.addAll(response.data)
            } catch (e: Exception) {
                println("Error fetching images: ${e.message}")
            } finally {
                _loading.value = false
            }
        }
    }
}
