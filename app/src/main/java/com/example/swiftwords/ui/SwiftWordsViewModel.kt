package com.example.swiftwords.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import java.util.Locale

data class MainUiState(
    var setOfLettersForLevel: Set<Char> = setOf(),
    var setOfLettersForMode: Set<Char> = setOf(),
    val gameTime: Long = 40000L,
    val currentLevel: Int = 11,
    val isMode: Boolean = false
)

class SwiftWordsMainViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    init {
        generateRandomLettersForBoth()
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

    private fun generateRandomLettersForMode() {
        _uiState.update { currentState ->
            currentState.copy(
                setOfLettersForMode = generateNewRandomLetters()
            )
        }
    }

    private fun generateRandomLettersForLevel() {
        _uiState.update { currentState ->
            currentState.copy(
                setOfLettersForLevel = generateNewRandomLetters()
            )
        }
    }

    fun generateRandomLettersForBoth() {
        _uiState.update { currentState ->
            currentState.copy(
                setOfLettersForLevel = generateNewRandomLetters(),
                setOfLettersForMode = generateNewRandomLetters()
            )
        }
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

