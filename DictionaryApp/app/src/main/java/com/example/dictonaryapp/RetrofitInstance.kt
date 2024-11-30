package com.example.dictonaryapp

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Retrofit Instance to make API calls
object RetrofitInstance {
    // Update the base URL to only include the root URL
    private const val BASE_URL = "https://api.dictionaryapi.dev/"

    val api: DictionaryApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL) // Keep only the root URL here
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DictionaryApiService::class.java)
    }
}
