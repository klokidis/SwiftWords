package com.yukuro.swiftwords.viewmodels

import androidx.compose.ui.unit.Dp
import androidx.lifecycle.ViewModel
import com.yukuro.swiftwords.data.ColorPair
import com.yukuro.swiftwords.data.DataSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class LevelUiState(
    val colors: List<ColorPair> =  DataSource().colorPairs,
    val padding: List<Pair<Dp, Dp>> = DataSource().paddingList
)


class LevelViewModel : ViewModel() {

    private val _levelUiState = MutableStateFlow(LevelUiState())
    val levelUiState: StateFlow<LevelUiState> = _levelUiState.asStateFlow()

}