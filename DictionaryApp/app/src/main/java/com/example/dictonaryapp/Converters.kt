package com.example.dictonaryapp

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    @TypeConverter
    fun fromMeaningsList(meanings: List<Meaning>): String {
        return Gson().toJson(meanings)
    }

    @TypeConverter
    fun toMeaningsList(data: String): List<Meaning> {
        val listType = object : TypeToken<List<Meaning>>() {}.type
        return Gson().fromJson(data, listType)
    }
}
