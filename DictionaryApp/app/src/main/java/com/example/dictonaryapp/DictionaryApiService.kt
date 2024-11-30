package com.example.dictonaryapp

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface DictionaryApiService {
    // Updated GET request to match the API URL structure
    @GET("api/v2/entries/en/{word}")
    suspend fun getWordMeaning(@Path("word") word: String): Response<List<DictionaryResult>>
}
