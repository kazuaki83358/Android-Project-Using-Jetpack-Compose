package com.example.dictonaryapp

import android.content.Context
import androidx.room.Room

object DatabaseProvider {
    private var instance: AppDatabase? = null

    // Synchronized to ensure thread safety
    @Synchronized
    private fun provideDatabase(context: Context): AppDatabase {
        if (instance == null) {
            instance = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "dictionary-db"
            ).build()
        }
        return instance!!
    }

    fun provideWordDao(context: Context): WordDao {
        return provideDatabase(context).wordDao()
    }
}
