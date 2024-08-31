package com.example.animewallpaperapp.Api

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface WallhavenService {

    @GET("/api/v1/w/{id}")
    suspend fun getWallpaperInfo(@Path("id") wallpaperId: String): WallpaperInfoResponse

    @GET("/api/v1/search")
    suspend fun searchWallpapers(
        @Query("q") query: String? = null, // Optional search query
        @Query("apikey") apiKey: String? = "EQKlRrtZyjmiNE4V2az5Qocukt1Dgylh", // Optional API key for authenticated users (NSFW access)
        @QueryMap otherParams: Map<String, String> = emptyMap() // Additional search parameters
    ): SearchResponse
}