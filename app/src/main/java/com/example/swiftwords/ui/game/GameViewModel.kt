package com.example.swiftwords.ui.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GameViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    init {
        runClock()
    }

    private fun runClock() {
        viewModelScope.launch {
            delay(1300L)
            while (uiState.value.isTimerRunning) {
                if (uiState.value.currentTime > 0L && uiState.value.isTimerRunning) {
                    delay(10L)
                    _uiState.update { currentState ->
                        currentState.copy(
                            currentTime = uiState.value.currentTime - 10L,
                            value = (uiState.value.currentTime - 100L) / uiState.value.totalTime.toFloat()
                        )
                    }
                }else{
                    _uiState.update { currentState ->
                        currentState.copy(
                            isTimerRunning = false
                        )
                    }
                }
            }
        }
    }

}