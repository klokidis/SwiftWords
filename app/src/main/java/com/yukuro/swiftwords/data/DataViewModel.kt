package com.yukuro.swiftwords.data


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yukuro.swiftwords.model.UserDetails
import com.yukuro.swiftwords.model.toUser
import com.yukuro.swiftwords.model.toUserDetails
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
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
    val userDetails: UserDetails = UserDetails(),
    val isLoading: Boolean = true
)

class GetDataViewModel(private val userRepository: UserRepository) : ViewModel() {

    val getDataUiState: StateFlow<ItemDetailsUiState> =
        userRepository.getUserStream()
            .filterNotNull()
            .map { user ->
                ItemDetailsUiState(userDetails = user.toUserDetails(), isLoading = false)
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = ItemDetailsUiState(isLoading = true)
            )

    init {
        viewModelScope.launch {
            var checked = false
            while (!checked) {
                if (!getDataUiState.value.isLoading) {
                    checkAndResetStreak()
                    checked = true
                } else {
                    delay(1000)
                }
            }
        }
    }

    fun updateUserColor(newColor: Int) {
        viewModelScope.launch {
            val currentUser = getDataUiState.value.userDetails.toUser()
            userRepository.updateUser(currentUser.copy(color = newColor))
        }
    }

    fun updateTime(newTime: Long) {
        viewModelScope.launch {
            val currentUser = getDataUiState.value.userDetails.toUser()
            userRepository.updateUser(currentUser.copy(levelTime = newTime))
        }
    }

    fun updateInitialState(value: Boolean = false) {
        viewModelScope.launch {
            val currentUser = getDataUiState.value.userDetails.toUser()
            userRepository.updateUser(currentUser.copy(initializeProfile = value))
        }
    }

    fun changeProfilePic(imageId: Int) {
        viewModelScope.launch {
            val currentUser = getDataUiState.value.userDetails.toUser()
            userRepository.updateUser(currentUser.copy(profileSelected = imageId))
        }
    }

    fun checkAndResetStreak() {
        viewModelScope.launch {
            val currentUser = getDataUiState.value.userDetails.toUser()

            val dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy")

            // Parse the stored dailyDate and the current date
            val storedDate = try {
                LocalDate.parse(currentUser.dailyDate, dateFormat)
            } catch (e: Exception) {
                null
            }
            val currentDate = LocalDate.now()

            // Check if the storedDate is more than one day before the current date
            val loseTheStreak = if (storedDate != null) {
                ChronoUnit.DAYS.between(storedDate, currentDate) > 1
            } else {
                true // If dailyDate is null or not properly parsed, assume a new day
            }

            // Reset the streak if it's more than one day before the current date
            if (loseTheStreak) {
                userRepository.updateUser(currentUser.copy(streak = 0))
            }
        }
    }

    fun updateChecked() {
        viewModelScope.launch {
            val currentUser = getDataUiState.value.userDetails.toUser()
            userRepository.updateUser(currentUser.copy(checked = !currentUser.checked))
        }
    }

    fun updateName(name: String) {
        viewModelScope.launch {
            val currentUser = getDataUiState.value.userDetails.toUser()
            userRepository.updateUser(currentUser.copy(nickname = name))
        }
    }

    fun updateCharacter(character: Boolean) {
        viewModelScope.launch {
            val currentUser = getDataUiState.value.userDetails.toUser()
            userRepository.updateUser(currentUser.copy(character = character))
        }
    }

    fun increaseCurrentLevel(score: Int) {
        viewModelScope.launch {
            val currentUser = getDataUiState.value.userDetails.toUser()

            val calendar = Calendar.getInstance()
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val formattedDate = dateFormat.format(calendar.time)

            val isNewDay = currentUser.dailyDate != formattedDate
            val isLevelSpacing = currentUser.currentLevel > currentUser.endingLevel - 4

            val newCurrentLevel = currentUser.currentLevel + 1
            val newStarterLevel = newCurrentLevel - 2
            val newEndingLevel = newCurrentLevel + 20
            val highScore = if (score > currentUser.highScore) {
                score
            } else {
                currentUser.highScore
            }

            val updatedUser = if (isLevelSpacing) {
                val newStreak = when {
                    isNewDay -> currentUser.streak + 1
                    currentUser.streak == 0 && !isNewDay -> 1 //in case of error
                    else -> currentUser.streak
                }
                currentUser.copy(
                    starterLevel = newStarterLevel,
                    endingLevel = newEndingLevel,
                    currentLevel = newCurrentLevel,
                    dailyDate = formattedDate,
                    streak = newStreak,
                    highScore = highScore
                )
            } else {
                val newStreak = when {
                    isNewDay -> currentUser.streak + 1
                    currentUser.streak == 0 && !isNewDay -> 1
                    else -> currentUser.streak
                }
                currentUser.copy(
                    currentLevel = newCurrentLevel,
                    dailyDate = formattedDate,
                    streak = newStreak,
                    highScore = highScore
                )
            }

            userRepository.updateUser(updatedUser)
        }
    }

    fun increaseStreak() {
        viewModelScope.launch {
            val currentUser = getDataUiState.value.userDetails.toUser()

            val calendar = Calendar.getInstance()
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val formattedDate = dateFormat.format(calendar.time)

            val isNewDay = currentUser.dailyDate != formattedDate

            val newStreak = when {
                isNewDay -> currentUser.streak + 1
                currentUser.streak == 0 && !isNewDay -> 1
                else -> currentUser.streak
            }

            userRepository.updateUser(
                currentUser.copy(
                    dailyDate = formattedDate,
                    streak = newStreak
                )
            )
        }
    }

    fun checkHighScore(thisScore: Int) {
        viewModelScope.launch {
            val currentUser = getDataUiState.value.userDetails.toUser()
            if (thisScore > currentUser.highScore) {
                userRepository.updateUser(currentUser.copy(highScore = thisScore))
            }
        }
    }


    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

}