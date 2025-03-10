package com.yukuro.swiftwords.viewmodels

import android.content.Context
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
    val gameMode: Int = 0,
    val colorCodePlayerOne: Int = 0,
    val colorCodePlayerTwo: Int = 1,
)

class SwiftWordsMainViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    // Declare a Job to track the coroutine
    private var lettersChangingJob: Job? = null

    init {
        generateRandomLettersForBoth()
        updateDateAtMidnight()
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


    private fun updateDateAtMidnight() {
        viewModelScope.launch {
            while (isActive) {
                getDate()  // Update the date

                // Get the current time and calculate the time remaining until midnight
                val currentTime = Calendar.getInstance()
                val nextMidnight = (currentTime.clone() as Calendar).apply {
                    add(Calendar.DAY_OF_YEAR, 1)  // Move to the next day
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }

                val delayUntilMidnight = nextMidnight.timeInMillis - currentTime.timeInMillis

                delay(delayUntilMidnight)  // Wait until midnight
            }
        }
    }

    private fun getDate() {
        // Get the current date and format it
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
        val formattedDate = dateFormat.format(calendar.time)

        // Update the StartingUiState with the current date
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

    fun changeColorCodePlayerCombat(colorCode: Int, player: Int) {
        if(player ==1 ) {
            _uiState.update { currentState ->
                currentState.copy(
                    colorCodePlayerOne = colorCode,
                )
            }
        } else{
            _uiState.update { currentState ->
                currentState.copy(
                    colorCodePlayerTwo = colorCode,
                )
            }
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

    fun changeGameMode(number: Int) {
        _uiState.update { currentState ->
            currentState.copy(
                gameMode = number
            )
        }
    }

    fun changingLetters(run: Boolean, playChangeSound: () -> Unit, gameTime: Long) {
        // Cancel the existing job if it's still active
        lettersChangingJob?.cancel()

        if (run) {
            lettersChangingJob = viewModelScope.launch {
                runChangingLetters(true, playChangeSound, gameTime)
            }
        }
    }

    private suspend fun runChangingLetters(
        run: Boolean,
        playChangeSound: () -> Unit,
        gameTime: Long
    ) {
        var elapsedTime = 0 // Initialize counter for elapsed time

        while (run && elapsedTime < gameTime) { // Continue only if less than game time
            delay(5000)
            generateRandomLettersForMode()
            playChangeSound()

            elapsedTime += 5000 // Increment elapsed time by 5 seconds after each delay
        }
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

    fun generateRandomLettersForBothOnExit() { //has a delay so the new words don't appear before exiting screen
        viewModelScope.launch {
            delay(500)
            _uiState.update { currentState ->
                currentState.copy(
                    setOfLettersForLevel = generateNewRandomLetters(),
                    setOfLettersForMode = generateNewRandomLetters(),
                )
            }
            initialiseLists()
        }
    }

    private fun generateNewRandomLetters(): Set<Char> {
        val vowels = mutableListOf('A', 'E', 'I', 'O')
        val consonants = ('A'..'Z').filter { it !in vowels }.toMutableList()
        // List of consonants that should not appear more than twice
        val restrictedConsonants = listOf('V', 'W', 'Y', 'Z', 'Q')

        // Remove restricted consonants from the consonants list
        val allowedConsonants = consonants.filter { it !in restrictedConsonants }.toMutableList()

        val randomVowels = mutableListOf<Char>()
        repeat(3) {
            val randomVowel = vowels.random()
            randomVowels.add(randomVowel)
            vowels.remove(randomVowel)
        }

        val randomConsonants = mutableListOf<Char>()
        repeat(3) {
            val randomConsonant = allowedConsonants.random()
            randomConsonants.add(randomConsonant)
            allowedConsonants.remove(randomConsonant)
        }

        val remainingLetters = (vowels + allowedConsonants + restrictedConsonants).toMutableList()
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

