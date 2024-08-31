package com.example.animewallpaperapp.Api

import WallhavenService

object WallhavenApi {
    val service: WallhavenService by lazy {
        RetrofitClient.instance.create(WallhavenService::class.java)
    }
}
