package com.example.swiftwords.ui.game

import androidx.compose.ui.unit.IntSize
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

data class GameUiState(
    var totalTime: Long = 30000L,
    var size: IntSize = IntSize.Zero,
    var value: Float = 1f,
    var currentTime: Long = 30000L,
    var isTimerRunning: Boolean = true
)

class GameViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            runClock()
        }
    }

    suspend fun checkAnswer(answer: String, wordList: Set<String>, charArray: Array<Char>): Boolean {
        return withContext(Dispatchers.Default) {
            // Convert charArray to a set of lowercase characters for efficient lookup
            val charSet = charArray.map { it.lowercaseChar() }.toSet()
            val trimmedAnswer = answer.trim().lowercase(Locale.ROOT)
            val isInWordList = wordList.contains(trimmedAnswer)
            val canBeConstructed = trimmedAnswer.all { it in charSet }

            isInWordList && canBeConstructed
        }
    }


    private suspend fun runClock() {
        delay(1300L)
        while (uiState.value.isTimerRunning) {
            if (uiState.value.currentTime > 0L && uiState.value.isTimerRunning) {
                delay(50L)
                _uiState.update { currentState ->
                    currentState.copy(
                        currentTime = uiState.value.currentTime - 50L,
                        value = (uiState.value.currentTime) / uiState.value.totalTime.toFloat()
                    )
                }
            } else {
                _uiState.update { currentState ->
                    currentState.copy(
                        isTimerRunning = false
                    )
                }
            }
        }
    }
}