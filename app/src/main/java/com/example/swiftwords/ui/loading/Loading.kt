package com.example.swiftwords.ui.loading

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.swiftwords.ui.AppViewModelProvider
import com.example.swiftwords.ui.GetDataViewModel
import com.example.swiftwords.ui.SwiftWordsApp
import com.example.swiftwords.ui.choose.ChooseCharacter

@Composable
fun Loading(
    context: Context,
    viewModel: GetDataViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val dataUiState by viewModel.getDataUiState.collectAsState()

    when {
        dataUiState.isLoading -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Loading...")
            }
        }
        dataUiState.userDetails == null -> {
            ChooseCharacter()
        }
        else -> {
            SwiftWordsApp(context)
        }
    }
}