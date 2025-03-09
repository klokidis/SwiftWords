package com.yukuro.swiftwords.ui

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.yukuro.swiftwords.InventoryApplication
import com.yukuro.swiftwords.data.GetDataViewModel
import com.yukuro.swiftwords.viewmodels.StartingViewmodel
import com.yukuro.swiftwords.viewmodels.SoundViewModel
import com.yukuro.swiftwords.viewmodels.LevelViewModel
import com.yukuro.swiftwords.viewmodels.ProfileViewmodel
import com.yukuro.swiftwords.viewmodels.SwiftWordsMainViewModel


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
