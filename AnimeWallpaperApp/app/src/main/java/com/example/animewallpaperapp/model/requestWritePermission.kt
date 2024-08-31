package com.example.animewallpaperapp.model

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.provider.MediaStore

private const val REQUEST_CODE_CREATE_DOCUMENT = 1

fun requestWritePermission(context: Context) {
    val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
        addCategory(Intent.CATEGORY_OPENABLE)
        type = "image/jpeg"
        putExtra(Intent.EXTRA_TITLE, "downloaded_image.jpg")
    }
    (context as Activity).startActivityForResult(intent, REQUEST_CODE_CREATE_DOCUMENT)
}
