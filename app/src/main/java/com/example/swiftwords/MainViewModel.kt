package com.example.swiftwords

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class MainUiState(
    var listOfLettersForLevel: Array<Char> = arrayOf(),
    var listOfLettersForMode: Array<Char> = arrayOf(),
)

class MainViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()


    init {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    listOfLettersForLevel = generateRandomLetters()
                )
            }
        }
    }

    suspend fun loadWordsFromAssets(context: Context): List<String> {
        return withContext(Dispatchers.IO) {
            val words = mutableListOf<String>()
            context.assets.open("engwords").bufferedReader().useLines { lines ->
                lines.forEach { words.add(it.trim()) }
            }
            words
        }
    }

    fun isWordValid(word: String, words: List<String>): Boolean {
        return words.contains(word)
    }


    private fun generateRandomLetters(): Array<Char> {
        val vowels = mutableListOf('A', 'E', 'I', 'O', 'U')
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
