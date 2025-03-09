package com.yukuro.swiftwords.viewmodels

import androidx.compose.ui.unit.IntSize
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.yukuro.swiftwords.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

data class GameUiState(
    val totalTime: Long = 40000L,
    val size: IntSize = IntSize.Zero,
    val value: Float = 1f,
    val currentTime: Long = 40000L,
    val isTimerRunning: Boolean = true,
    val score: Int = 0,
    val scorePlayerOne: Int = 0,
    val scorePlayerTwo: Int = 0,
    val alreadyAnswered: Set<String> = setOf(),
    val alreadyAnsweredPlayerOne: Set<String> = setOf(),
    val alreadyAnsweredPlayerTwo: Set<String> = setOf(),
)

class GameViewModel(time: () -> Long) : ViewModel() {

    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    // Declare a Job to track the coroutine
    private var clockJob: Job? = null

    init {
        viewModelScope.launch {
            updateTime(time())
            runClock()
        }
        setScoreToZero()
    }

    suspend fun checkAnswer(
        answer: () -> String,
        wordList: Set<String>,
        charArray: Set<Char>
    ): Int {
        return withContext(Dispatchers.Default) {
            if (answer().length > 1) {
                // Convert charArray to a set of lowercase characters for efficient lookup
                val charSet = charArray.map { it.lowercaseChar() }.toSet()
                val trimmedAnswer = answer().trim().lowercase(Locale.ROOT)
                val isInWordList = wordList.contains(trimmedAnswer)
                val canBeConstructed = trimmedAnswer.all { it in charSet }
                val newWord = !uiState.value.alreadyAnswered.contains(answer())

                return@withContext when {
                    !newWord -> 3 // Word already answered
                    isInWordList && canBeConstructed -> {
                        increaseScore()
                        addWord(trimmedAnswer)
                        1 // Everything is good
                    }
                    else -> 2 // False
                }
            } else {
                2 // False
            }
        }
    }

    suspend fun checkAnswerCombat(
        answer: () -> String,
        wordList: Set<String>,
        charArray: Set<Char>,
        player: Int
    ): Int {
        return withContext(Dispatchers.Default) {
            if (answer().length > 1) {
                // Convert charArray to a set of lowercase characters for efficient lookup
                val charSet = charArray.map { it.lowercaseChar() }.toSet()
                val trimmedAnswer = answer().trim().lowercase(Locale.ROOT)
                val isInWordList = wordList.contains(trimmedAnswer)
                val canBeConstructed = trimmedAnswer.all { it in charSet }
                val newWord = if (player == 1) {
                    !uiState.value.alreadyAnsweredPlayerOne.contains(answer())
                } else {
                    !uiState.value.alreadyAnsweredPlayerTwo.contains(answer())
                }

                return@withContext when {
                    !newWord -> 3 // Word already answered
                    isInWordList && canBeConstructed -> {
                        increaseScoreCombat(player)
                        addWordCombat(trimmedAnswer, player)
                        1 // Everything is good
                    }

                    else -> 2 // False
                }
            } else {
                2 // False
            }
        }
    }


    fun calculatePassingScore(level: Int): Int {
        return when {
            level < 1 -> 5
            level < 8 -> 5
            level < 20 -> 7
            level < 40 -> 10
            level < 50 -> 11
            level < 60 -> 12
            level < 70 -> 13
            level < 90 -> 14
            level < 130 -> 16
            level < 200 -> 17
            level < 250 -> 18
            level < 300 -> 20
            level < 350 -> 23
            else -> 30
        }
    }

    private fun updateTime(newTime: Long) {
        _uiState.update { currentState ->
            currentState.copy(
                isTimerRunning = true,
                totalTime = newTime,
                currentTime = newTime,
                value = newTime / newTime.toFloat()
            )
        }
    }

    fun addTime() {
        _uiState.update { currentState ->
            currentState.copy(
                currentTime = if ((currentState.currentTime + 1500) > currentState.totalTime) {
                    currentState.totalTime
                } else {
                    currentState.currentTime + 1500
                }
            )
        }
    }

    fun removeTime() {
        _uiState.update { currentState ->
            currentState.copy(
                currentTime = currentState.currentTime - 1500
            )
        }
    }

    private fun addWord(newWord: String) {
        _uiState.update { currentState ->
            currentState.copy(
                alreadyAnswered = currentState.alreadyAnswered.plus(newWord)
            )
        }
    }

    private fun addWordCombat(newWord: String, player: Int) {
        _uiState.update { currentState ->
            if (player == 1) {
                currentState.copy(
                    alreadyAnsweredPlayerOne = currentState.alreadyAnsweredPlayerOne.plus(newWord)
                )
            } else {
                currentState.copy(
                    alreadyAnsweredPlayerTwo = currentState.alreadyAnsweredPlayerTwo.plus(newWord)
                )
            }
        }
    }


    fun restartGame(newTime: Long) {
        // Cancel the existing job if it's still active
        clockJob?.cancel()

        // Start a new coroutine and assign it to clockJob
        clockJob = viewModelScope.launch {
            updateTime(newTime)
            runClock()
        }
        setScoreToZero()
    }


    private fun setScoreToZero() {
        _uiState.update { currentState ->
            currentState.copy(
                score = 0,
                alreadyAnswered = setOf(),
                scorePlayerOne = 0,
                scorePlayerTwo = 0,
                alreadyAnsweredPlayerOne = setOf(),
                alreadyAnsweredPlayerTwo = setOf()
            )
        }
    }

    private fun increaseScore() {
        _uiState.update { currentState ->
            currentState.copy(
                score = currentState.score + 1
            )
        }
    }

    private fun increaseScoreCombat(player: Int) {
        _uiState.update { currentState ->
            if (player == 1) {
                currentState.copy(
                    scorePlayerOne = currentState.scorePlayerOne + 1
                )
            } else {
                currentState.copy(
                    scorePlayerTwo = currentState.scorePlayerTwo + 1
                )
            }
        }
    }

    fun stopClock() {
        _uiState.update { currentState ->
            currentState.copy(
                isTimerRunning = false
            )
        }
    }

    fun stopClockOnExit() { //stops the clock so it doesn't run on background
        viewModelScope.launch {
            delay(2000L) //delay 2 seconds so it doesn't appear instant
            _uiState.update { currentState ->
                currentState.copy(
                    isTimerRunning = false
                )
            }
        }
    }

    private suspend fun runClock() {
        if (uiState.value.currentTime != 130000000L) {//this means its on unlimited time
            delay(1300L)
            while (uiState.value.isTimerRunning) {
                if (uiState.value.currentTime > 0L && uiState.value.isTimerRunning) {
                    delay(50L)
                    _uiState.update { currentState ->
                        currentState.copy(
                            currentTime = currentState.currentTime - 50L,
                            value = (currentState.currentTime) / currentState.totalTime.toFloat()
                        )
                    }
                } else {
                    stopClock()
                }
            }
        }
    }

    fun getFireImage(streakLevel: Int): Int {
        return when {
            streakLevel < 5 -> R.drawable.fire_on
            streakLevel < 15 -> R.drawable.fire2
            streakLevel < 30 -> R.drawable.fire3
            streakLevel < 40 -> R.drawable.fire4
            streakLevel < 50 -> R.drawable.fire5
            else -> R.drawable.fire6
        }
    }
}

class GameViewModelFactory(private val newTime: () -> Long) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GameViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GameViewModel(newTime) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}