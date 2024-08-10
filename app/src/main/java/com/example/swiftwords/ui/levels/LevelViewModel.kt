package com.example.swiftwords.ui.levels

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class UiState(
    val example : Int = 0
)


class LevelViewModel: ViewModel()  {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun calculatePaddingValues(currentLevel: Int): List<Pair<Dp, Dp>> {
        val paddingChange = 50.dp
        var leftPadding = 0.dp
        var rightPadding = 0.dp
        var step = 0
        val paddingValues = mutableListOf<Pair<Dp, Dp>>()

        for (i in currentLevel - 3  ..currentLevel + 20) {
            if (i > 0) {
                when (step) {
                    in 0..2 -> {
                        leftPadding += paddingChange
                        if (rightPadding != 0.dp) {
                            rightPadding -= paddingChange
                        }
                    }

                    3 -> {
                        step = -4
                    }

                    in -4..-1 -> {
                        rightPadding += paddingChange
                        if (leftPadding != 0.dp) {
                            leftPadding -= paddingChange
                        }
                    }
                }
                step += 1
                paddingValues.add(leftPadding to rightPadding)
            }
        }
        return paddingValues
    }
}