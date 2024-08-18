package com.example.view_model_practice.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class RandomNumberViewModelFactory : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RandomNumberViewModel::class.java)) {
            return RandomNumberViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
