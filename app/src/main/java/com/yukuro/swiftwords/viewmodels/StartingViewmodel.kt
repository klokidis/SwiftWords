package com.yukuro.swiftwords.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


data class StartingUiState(
    val dialogueState: Int = 1,
    val character: Int = 2,
)

class StartingViewmodel : ViewModel() {

    private val _uiState = MutableStateFlow(StartingUiState())
    val uiState: StateFlow<StartingUiState> = _uiState.asStateFlow()

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