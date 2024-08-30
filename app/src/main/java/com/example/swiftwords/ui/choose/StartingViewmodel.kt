package com.example.swiftwords.ui.choose

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


data class UiState(
    val dialogueState: Int = 1,
    val character: Int = 2,
)

class StartingViewmodel : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun increaseState(){
        _uiState.update { currentState ->
            currentState.copy(
                dialogueState = currentState.dialogueState + 1,
            )
        }
    }

    fun updateCharacter(value: Int){
        _uiState.update { currentState ->
            currentState.copy(
                character = value,
            )
        }
    }

    fun decreaseState(){
        _uiState.update { currentState ->
            currentState.copy(
                dialogueState = currentState.dialogueState - 1,
            )
        }
    }

}