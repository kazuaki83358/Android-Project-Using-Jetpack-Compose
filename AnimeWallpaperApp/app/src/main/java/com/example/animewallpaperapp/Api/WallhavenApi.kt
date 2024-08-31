package com.example.animewallpaperapp.Api

object WallhavenApi {
    val service: WallhavenService by lazy {
        RetrofitClient.instance.create(WallhavenService::class.java)
    }
}
