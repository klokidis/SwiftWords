package com.example.swiftwords.data

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Calendar
import java.util.Locale

data class ItemDetailsUiState(
    val userDetails: UserDetails? = null,
    val isLoading: Boolean = true
)

class GetDataViewModel(private val userRepository: UserRepository) : ViewModel() {

    val getDataUiState: StateFlow<ItemDetailsUiState> =
        userRepository.getUserStream()
            .map { user ->
                if (user == null) {
                    ItemDetailsUiState(isLoading = true)
                } else {
                    ItemDetailsUiState(userDetails = user.toUserDetails(), isLoading = false)
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = ItemDetailsUiState(isLoading = true)
            )

    fun updateUserColor(newColor: Int) {
        viewModelScope.launch {
            val currentUser = getDataUiState.value.userDetails?.toUser()
            if (currentUser != null) {
                userRepository.updateUser(currentUser.copy(color = newColor))
            }
        }
    }
    // Method to be called when the app launches to check and reset streak
    fun checkAndResetStreak() {
        viewModelScope.launch {
            val currentUser = getDataUiState.value.userDetails?.toUser() ?: return@launch

            val dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy")

            // Parse the stored dailyDate and the current date
            val storedDate = try {
                LocalDate.parse(currentUser.dailyDate, dateFormat)
            } catch (e: Exception) {
                null
            }
            val currentDate = LocalDate.now()

            // Check if the storedDate is more than one day before the current date
            val isNewDay = if (storedDate != null) {
                ChronoUnit.DAYS.between(storedDate, currentDate) > 1
            } else {
                true // If dailyDate is null or not properly parsed, assume a new day
            }

            // Reset the streak if it's a new day
            if (isNewDay) {
                userRepository.updateUser(currentUser.copy(streak = 0))
            }
        }
    }

    fun updateChecked() {
        viewModelScope.launch {
            val currentUser = getDataUiState.value.userDetails?.toUser()
            if (currentUser != null) {
                userRepository.updateUser(currentUser.copy(checked = !currentUser.checked))
            }
        }
    }

    fun increaseCurrentLevel(score: Int) {
        viewModelScope.launch {
            val currentUser = getDataUiState.value.userDetails?.toUser() ?: return@launch

            val calendar = Calendar.getInstance()
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val formattedDate = dateFormat.format(calendar.time)
            Log.d("klok", formattedDate)

            val isNewDay = currentUser.dailyDate != formattedDate
            val isLevelSpacing = currentUser.currentLevel > currentUser.endingLevel - 4

            val newCurrentLevel = currentUser.currentLevel + 1
            val newStarterLevel = newCurrentLevel - 2
            val newEndingLevel = newCurrentLevel + 26

            val updatedUser = if (isLevelSpacing) {
                currentUser.copy(
                    starterLevel = newStarterLevel,
                    endingLevel = newEndingLevel,
                    currentLevel = newCurrentLevel,
                    dailyDate = formattedDate,
                    streak = if (isNewDay) currentUser.streak + 1 else currentUser.streak
                )
            } else {
                currentUser.copy(
                    currentLevel = newCurrentLevel,
                    dailyDate = formattedDate,
                    streak = if (isNewDay) currentUser.streak + 1 else currentUser.streak
                )
            }

            userRepository.updateUser(updatedUser)
            checkHighScore(score)
        }
    }


    suspend fun checkHighScore(thisScore: Int) {
        viewModelScope.launch {
            val currentUser = getDataUiState.value.userDetails?.toUser()
            if (currentUser != null) {
                if (thisScore > currentUser.highScore) {
                    userRepository.updateUser(currentUser.copy(highScore = thisScore))
                }
            }
        }
    }


    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

}