package com.example.swiftwords.ui

import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.swiftwords.ui.game.GameViewModel


object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            GameViewModel(
            )
        }
    }
}