package com.example.animewallpaperapp.Api

import com.google.gson.annotations.SerializedName

data class SearchResponse(
    val data: List<WallpaperInfoResponse>,
    val meta: Meta
)

data class WallpaperInfoResponse(
    val id: String,
    val url: String,
    @SerializedName("short_url") val shortUrl: String,
    val views: Int,
    val favorites: Int,
    val purity: String,
    val category: String,
    val colors: List<String>,
    val path: String,
    val thumbs: Thumbs
)

data class Thumbs(
    val large: String,
    val original: String,
    val small: String
)

data class Meta(
    @SerializedName("current_page") val currentPage: Int,
    @SerializedName("last_page") val lastPage: Int,
    @SerializedName("total") val total: Int
)
