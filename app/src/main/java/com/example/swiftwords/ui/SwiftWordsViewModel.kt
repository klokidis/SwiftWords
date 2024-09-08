package com.example.swiftwords.ui

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

data class MainUiState(
    var setOfLettersForLevel: Set<Char> = setOf(),
    var setOfLettersForMode: Set<Char> = setOf(),
    var listOfLettersForLevel: List<Char> = setOfLettersForLevel.toList(),
    var listOfLettersForMode: List<Char> = setOfLettersForMode.toList(),
    val gameTime: Long = 40000L,
    val currentLevel: Int = 1,
    val isMode: Boolean = false,
    val todayDate: String = "",
    val gameMode: Int = 0
)

class SwiftWordsMainViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    // Declare a Job to track the coroutine
    private var lettersChangingJob: Job? = null

    init {
        generateRandomLettersForBoth()
        updateDateAtHourChange()
    }

    suspend fun loadWordsFromAssets(context: Context): Set<String> {
        return withContext(Dispatchers.IO) {
            val words = mutableSetOf<String>()
            context.assets.open("engwords").bufferedReader().useLines { lines ->
                lines.forEach { words.add(it.trim().lowercase(Locale.ROOT)) }
            }
            words
        }
    }

    fun shuffleLetters() {
        _uiState.update { currentState ->
            currentState.copy(
                listOfLettersForLevel = currentState.listOfLettersForLevel.shuffled(),
                listOfLettersForMode = currentState.listOfLettersForMode.shuffled()
            )
        }
    }

    private fun initialiseLists() {
        _uiState.update { currentState ->
            currentState.copy(
                listOfLettersForMode = currentState.setOfLettersForMode.toList(),
                listOfLettersForLevel = currentState.setOfLettersForLevel.toList()
            )
        }
    }


    private fun updateDateAtHourChange() {
        viewModelScope.launch {
            while (isActive) {
                getDate()  // Update the date

                // Calculate the delay until the start of the next hour
                val currentTime = Calendar.getInstance()
                val nextHour = (currentTime.clone() as Calendar).apply {
                    add(Calendar.HOUR_OF_DAY, 1)  // Move to the next hour
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }

                val delayUntilNextHour = nextHour.timeInMillis - currentTime.timeInMillis

                delay(delayUntilNextHour)  // Wait until the next hour
            }
        }
    }

    private fun getDate() {
        // Get the current date and format it
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
        val formattedDate = dateFormat.format(calendar.time)

        // Update the UiState with the current date
        _uiState.update { currentState ->
            currentState.copy(
                todayDate = formattedDate
            )
        }
    }

    fun changeTime(newTime: Long) {
        _uiState.update { currentState ->
            currentState.copy(
                gameTime = newTime,
            )
        }
    }

    fun changeGameState(isMode: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(
                isMode = isMode,
            )
        }
    }

    fun generateRandomLettersForMode() {
        _uiState.update { currentState ->
            currentState.copy(
                setOfLettersForMode = generateNewRandomLetters()
            )
        }
        initialiseLists()
    }

    fun changeGameMode(number : Int) {
        _uiState.update { currentState ->
            currentState.copy(
                gameMode = number
            )
        }
    }

    fun changingLetters(run: Boolean, playChangeSound: () -> Unit) {
        // Cancel the existing job if it's still active
        lettersChangingJob?.cancel()

        if (run) {
            lettersChangingJob = viewModelScope.launch {
                runChangingLetters(true, playChangeSound)
            }
        }
    }

    private suspend fun runChangingLetters(run: Boolean, playChangeSound: () -> Unit) {
        var elapsedTime = 0 // Initialize counter for elapsed time

        while (run && elapsedTime < 40000) { // Continue only if less than 40 seconds
            delay(5000)
            generateRandomLettersForMode()
            playChangeSound()
            Log.d("klok", "running")

            elapsedTime += 5000 // Increment elapsed time by 5 seconds after each delay
        }

        Log.d("klok", "Stopped after 40 seconds")
    }


    fun generateRandomLettersForBoth() {
        _uiState.update { currentState ->
            currentState.copy(
                setOfLettersForLevel = generateNewRandomLetters(),
                setOfLettersForMode = generateNewRandomLetters(),
            )
        }
        initialiseLists()
    }

    private fun generateNewRandomLetters(): Set<Char> {
        val vowels = mutableListOf('A', 'E', 'I', 'O')
        val consonants = ('A'..'Z').filter { it !in vowels }.toMutableList()

        val randomVowels = mutableListOf<Char>()
        repeat(3) {
            val randomVowel = vowels.random()
            randomVowels.add(randomVowel)
            vowels.remove(randomVowel)
        }

        val randomConsonants = mutableListOf<Char>()
        repeat(3) {
            val randomConsonant = consonants.random()
            randomConsonants.add(randomConsonant)
            consonants.remove(randomConsonant)
        }

        val remainingLetters = (vowels + consonants).toMutableList()
        val randomOthers = mutableListOf<Char>()
        repeat(3) {
            val randomOther = remainingLetters.random()
            randomOthers.add(randomOther)
            remainingLetters.remove(randomOther)
        }

        val combinedList = randomVowels + randomConsonants + randomOthers

        return combinedList.shuffled().toSet()
    }
}

