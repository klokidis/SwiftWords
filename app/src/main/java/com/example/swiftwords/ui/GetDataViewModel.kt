package com.example.swiftwords.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.swiftwords.data.UserDetails
import com.example.swiftwords.data.UserRepository
import com.example.swiftwords.data.toUserDetails
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class ItemDetailsUiState(
    val userDetails: UserDetails? = null,
    val isLoading: Boolean = true
)

class GetDataViewModel(userRepository: UserRepository) : ViewModel() {

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


    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

}