package com.example.swiftwords.ui.loading

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.swiftwords.ui.AppViewModelProvider
import com.example.swiftwords.data.GetDataViewModel
import com.example.swiftwords.ui.SwiftWordsApp
import com.example.swiftwords.ui.choose.ChooseCharacter

@Composable
fun Loading(
    viewModel: GetDataViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val dataUiState by viewModel.getDataUiState.collectAsState()

    // Extract the relevant UI state
    val isLoading = remember(dataUiState.isLoading) { dataUiState.isLoading }
    val userId = remember(dataUiState.userDetails?.id) { dataUiState.userDetails?.id }

    // Handle different states based on extracted values
    when {
        isLoading -> LoadingView()
        userId == 2 -> ChooseCharacter()
        else -> SwiftWordsApp(dataUiState, viewModel)
    }
}

@Composable
fun LoadingView() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Loading...")
    }
}
