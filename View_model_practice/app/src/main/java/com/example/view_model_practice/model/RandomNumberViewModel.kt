package com.example.view_model_practice.model

import androidx.lifecycle.ViewModel
import kotlin.random.Random

class RandomNumberViewModel : ViewModel() {
    private var _random = 0
    val randomNumber : Int
        get() = _random

    fun generateRandomNumber(){
        _random = Random.nextInt(100)
    }
}