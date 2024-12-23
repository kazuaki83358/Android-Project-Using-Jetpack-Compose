package com.example.dictonaryapp

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "words")
data class WordEntity(
    @PrimaryKey val word: String,
    val phonetic: String? = null,  // Optional parameter
    val meanings: String,
    val origin: String? = null     // Optional parameter
)
