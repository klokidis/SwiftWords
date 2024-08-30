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
import com.example.swiftwords.ui.choose.StartingScreen

@Composable
fun Loading(
    viewModel: GetDataViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val dataUiState by viewModel.getDataUiState.collectAsState()

    // Handle different states based on extracted values
    when {
        dataUiState.isLoading -> LoadingView()
        dataUiState.userDetails?.initializeProfile == false -> SwiftWordsApp(dataUiState, viewModel)
        else -> StartingScreen(dataViewmodel = viewModel)
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
