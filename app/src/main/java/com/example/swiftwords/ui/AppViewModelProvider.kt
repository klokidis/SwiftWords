package com.example.swiftwords.ui

import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.swiftwords.MainViewModel
import com.example.swiftwords.ui.levels.LevelViewModel


object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            LevelViewModel()
        }
        initializer {
            MainViewModel()
        }
    }
}