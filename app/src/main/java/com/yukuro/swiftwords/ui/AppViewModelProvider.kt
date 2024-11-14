package com.yukuro.swiftwords.ui

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.yukuro.swiftwords.InventoryApplication
import com.yukuro.swiftwords.data.GetDataViewModel
import com.yukuro.swiftwords.ui.choose.StartingViewmodel
import com.yukuro.swiftwords.ui.elements.SoundViewModel
import com.yukuro.swiftwords.ui.levels.LevelViewModel
import com.yukuro.swiftwords.ui.profile.ProfileViewmodel


object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            LevelViewModel()
        }
        initializer {
            ProfileViewmodel()
        }
        initializer {
            SwiftWordsMainViewModel()
        }
        initializer {
            StartingViewmodel()
        }
        initializer {
            GetDataViewModel(inventoryApplication().container.userRepository)
        }
        initializer {
            SoundViewModel(inventoryApplication())
        }
    }
}

fun CreationExtras.inventoryApplication(): InventoryApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as InventoryApplication)
