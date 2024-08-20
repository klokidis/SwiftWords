package com.example.swiftwords.ui

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.swiftwords.data.DataViewmodel
import com.example.swiftwords.InventoryApplication
import com.example.swiftwords.data.GetDataViewModel
import com.example.swiftwords.ui.levels.LevelViewModel


object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            LevelViewModel()
        }
        initializer {
            SwiftWordsMainViewModel()
        }
        initializer {
            DataViewmodel(inventoryApplication().container.userRepository)
        }
        initializer {
            GetDataViewModel(inventoryApplication().container.userRepository)
        }
    }
}

fun CreationExtras.inventoryApplication(): InventoryApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as InventoryApplication)
