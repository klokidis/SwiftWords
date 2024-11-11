package com.yukuro.swiftwords.ui.levels

import androidx.compose.ui.unit.Dp
import androidx.lifecycle.ViewModel
import com.yukuro.swiftwords.data.ColorPair
import com.yukuro.swiftwords.data.DataSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class UiState(
    val colors: List<ColorPair> =  DataSource().colorPairs,
    val padding: List<Pair<Dp, Dp>> = DataSource().paddingList
)


class LevelViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

}