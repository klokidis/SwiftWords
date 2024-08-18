package com.example.swiftwords.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.swiftwords.data.UserDetails
import com.example.swiftwords.data.UserRepository
import com.example.swiftwords.data.toUser
import com.example.swiftwords.data.toUserDetails
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

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
            val currentUser = getDataUiState.value.userDetails?.toUser()
            if (currentUser != null) {
                userRepository.updateUser(currentUser.copy(currentLevel = currentUser.currentLevel + 1))
                checkLevelSpacing()
                checkHighScore(score)
            }
        }
    }

    suspend fun checkHighScore(thisScore: Int) {
        viewModelScope.launch {
            val currentUser = getDataUiState.value.userDetails?.toUser()
            if (currentUser != null) {
                if(thisScore > currentUser.highScore) {
                    userRepository.updateUser(currentUser.copy(highScore = thisScore))
                }
            }
        }
    }

    private suspend fun checkLevelSpacing() {
        val currentUser = getDataUiState.value.userDetails?.toUser()
        if (currentUser != null) {
            if (currentUser.currentLevel > currentUser.endingLevel - 4) {
                userRepository.updateUser(
                    currentUser.copy(
                        starterLevel = currentUser.currentLevel - 2,
                        endingLevel = currentUser.currentLevel + 18
                    )
                )
            }
        }
    }


    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

}