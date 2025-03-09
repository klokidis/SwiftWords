package com.yukuro.swiftwords.viewmodels

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yukuro.swiftwords.R
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ProfileUiState(
    val firesWalk: List<Int> = listOf(
        R.drawable.fire1_walk,
        R.drawable.fire2_walk,
        R.drawable.fire3_walk,
        R.drawable.fire4_walk,
        R.drawable.fire5_walk,
        R.drawable.fire6_walk,
    ),
    val fires: List<Int> = listOf(
        R.drawable.fire_on,
        R.drawable.fire2,
        R.drawable.fire3,
        R.drawable.fire4,
        R.drawable.fire5,
        R.drawable.fire6,
    ),
    val padding: List<Dp> = listOf(),
    val visible: Boolean = false
)


class ProfileViewmodel : ViewModel() {

    private val _profileUiState = MutableStateFlow(ProfileUiState())
    val profileUiState: StateFlow<ProfileUiState> = _profileUiState.asStateFlow()

    // Declare a Job to track the bouble visibility
    private var bubbleScope: Job? = null

    fun streakCalculation(streak: Int): Int {
        return when {
            streak < 5 -> 0
            streak < 15 -> 1
            streak < 30 -> 2
            streak < 40 -> 3
            streak < 50 -> 4
            else -> 5
        }
    }

    fun boublePadding(bouble: Int): Dp {
        return when (bouble) {
            0 -> 2.dp
            1 -> 50.dp
            2 -> 100.dp
            3 -> 150.dp
            4 -> 190.dp
            5 -> 240.dp
            else -> 10.dp
        }
    }

    fun getTextBouble(bouble: Int): Int {
        return when (bouble) {
            0 -> R.string.first_fire
            1 -> R.string.second_fire
            2 -> R.string.third_fire
            3 -> R.string.fourth_fire
            4 -> R.string.fifth_fire
            5 -> R.string.sixth_fire
            else -> R.string.sixth_fire
        }
    }

    fun changeVisible() {

        bubbleScope?.cancel()

        bubbleScope = viewModelScope.launch {
            _profileUiState.update { currentState ->
                currentState.copy(
                    visible = true,
                )
            }
            delay(2500)
            _profileUiState.update { currentState ->
                currentState.copy(
                    visible = false,
                )
            }
        }
    }
}