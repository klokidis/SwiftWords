package com.example.swiftwords

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class MainUiState(
    var listOfLettersForLevel: List<Char> = generateRandomLetters(),
    var listOfLettersForMode: List<Char> = generateRandomLetters(),
)

private fun generateRandomLetters(): List<Char> {
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
    return combinedList.shuffled()
}


class MainViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()


}
