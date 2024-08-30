package com.example.swiftwords.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

data class UserUiState(
    val userDetails: UserDetails = UserDetails(),
)

data class UserDetails(
    val id: Int = 0,
    val initializeProfile: Boolean = true,
    val nickname: String = "",
    val character: Boolean = false,//0 for male 1 for female
    val currentLevel: Int = 1,
    val streak: Int = 0,
    val highScore: Int = 1,
    val dailyDate: String = "",
    val lives: Int = 5,
    val lifeDate: String = "",//add set of char level
    val setOfLetters: String = "",
    val color: Int = 0,
    val starterLevel: Int = 1,
    val endingLevel: Int = 20,
    val checked: Boolean = false,
)

fun UserDetails.toUser(): User = User(
    id = id,
    initializeProfile= initializeProfile,
    nickname = nickname,
    character = character,//0 for male 1 for female
    currentLevel = currentLevel,
    streak = streak,
    highScore = highScore,
    dailyDate = dailyDate,
    lives = lives,
    lifeDate = lifeDate,
    setOfLetters = setOfLetters,
    color = color,
    starterLevel = starterLevel,
    endingLevel = endingLevel,
    checked = checked
)


fun User.toUserUiState(isEntryValid: Boolean = false): UserUiState = UserUiState(
    userDetails = this.toUserDetails()
)

fun User.toUserDetails(): UserDetails = UserDetails(
    id = id,
    initializeProfile = initializeProfile,
    nickname = nickname,
    character = character, //0 for male 1 for female
    currentLevel = currentLevel,
    streak = streak,
    highScore = highScore,
    dailyDate = dailyDate,
    lives = lives,
    lifeDate = lifeDate,
    color = color,
    starterLevel = starterLevel,
    endingLevel = endingLevel,
    checked = checked
)

class DataViewmodel(private val userRepository: UserRepository) : ViewModel() {
    var userUiState by mutableStateOf(UserUiState())
        private set

    suspend fun saveUser() {
        userRepository.insertUser(userUiState.userDetails.toUser())
    }
}