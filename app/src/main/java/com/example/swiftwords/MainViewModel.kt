package com.example.swiftwords

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class MainUiState(
    var listOfLettersForLevel: Array<Char> = arrayOf(),
    var listOfLettersForMode: Array<Char> = arrayOf(),
    val gameTime: Long = 40000L,
    val currentLevel : Int = 10
)

class MainViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()


    init {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    listOfLettersForLevel = generateNewRandomLetters()
                )
            }
        }
    }

    suspend fun getSetFromFile(context: Context): Set<String> {
        return withContext(Dispatchers.IO) {
            // Convert letters for the level to a set of lowercase characters once
            val lettersForLevel = uiState.value.listOfLettersForLevel
                .map { it.lowercaseChar() }
                .toSet()

            // Load words asynchronously
            val wordsDeferred = async { loadWordsFromAssets(context) }

            // Await the result and filter words
            val filteredWords = wordsDeferred.await()
                .map { it.lowercase() }
                .filter { word ->
                    // Check if all characters of the word are in the set of letters
                    word.all { it in lettersForLevel }
                }
                .toSet()

            // Update the state with filtered words
            filteredWords

        }
    }


    private suspend fun loadWordsFromAssets(context: Context): Set<String> {
        return withContext(Dispatchers.IO) {
            val words = mutableSetOf<String>()
            context.assets.open("engwords").bufferedReader().useLines { lines ->
                lines.forEach { words.add(it.trim()) }
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

    private fun generateNewRandomLetters(): Array<Char> {
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
        return combinedList.shuffled().toTypedArray()
    }


}

