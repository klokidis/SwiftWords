package com.example.swiftwords.ui.levels

import androidx.compose.ui.unit.Dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.swiftwords.data.ColorPair
import com.example.swiftwords.data.DataSource
import com.example.swiftwords.data.ItemDetailsUiState
import com.example.swiftwords.data.UserRepository
import com.example.swiftwords.data.toUser
import com.example.swiftwords.data.toUserDetails
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class UiState(
    val colors: List<ColorPair> =  DataSource().colorPairs,
    val padding: List<Pair<Dp, Dp>> = DataSource().paddingList
)


class LevelViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    val uiStateData: StateFlow<ItemDetailsUiState> =
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
            val currentUser = uiStateData.value.userDetails?.toUser()
            if (currentUser != null) {
                userRepository.updateUser(currentUser.copy(color = newColor))
            }
        }
    }


    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}
