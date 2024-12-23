package com.example.dictonaryapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class DictionaryViewModelFactory(private val wordDao: WordDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DictionaryViewModel::class.java)) {
            return DictionaryViewModel(wordDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

