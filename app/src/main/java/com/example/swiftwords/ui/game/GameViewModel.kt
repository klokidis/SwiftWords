package com.example.swiftwords.ui.game

import androidx.compose.ui.unit.IntSize
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
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
    var totalTime: Long = 40000L,
    var size: IntSize = IntSize.Zero,
    var value: Float = 1f,
    var currentTime: Long = 40000L,
    var isTimerRunning: Boolean = true,
    var score: Int = 0
)

class GameViewModel(time: Long) : ViewModel() {

    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            changeTime(time)
            runClock()
        }
        setScoreToZero()
    }

    suspend fun checkAnswer(
        answer: String,
        wordList: Set<String>,
        charArray: Array<Char>
    ): Boolean {
        return withContext(Dispatchers.Default) {
            if (answer.length > 1) {
                // Convert charArray to a set of lowercase characters for efficient lookup
                val charSet = charArray.map { it.lowercaseChar() }.toSet()
                val trimmedAnswer = answer.trim().lowercase(Locale.ROOT)
                val isInWordList = wordList.contains(trimmedAnswer)
                val canBeConstructed = trimmedAnswer.all { it in charSet }

                if (isInWordList && canBeConstructed) {
                    increaseScore()
                }

                isInWordList && canBeConstructed
            } else {
                false
            }
        }
    }

    private fun changeTime(newTime: Long) {
        _uiState.update { currentState ->
            currentState.copy(
                totalTime = newTime,
                currentTime = newTime
            )
        }
    }

    private fun setScoreToZero() {
        _uiState.update { currentState ->
            currentState.copy(
                score = 0
            )
        }
    }

    private fun increaseScore() {
        _uiState.update { currentState ->
            currentState.copy(
                score = uiState.value.score + 1
            )
        }
    }

    fun stopClock() {
        _uiState.update { currentState ->
            currentState.copy(
                isTimerRunning = false
            )
        }
    }

    private suspend fun runClock() {
        if (uiState.value.currentTime != 130000000L) {
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
}

class GameViewModelFactory(private val newTime: Long) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GameViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GameViewModel(newTime) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}