package com.example.swiftwords.ui.game

import androidx.compose.ui.unit.IntSize

data class GameUiState (
    var totalTime: Long = 25000L,
    var size: IntSize = IntSize.Zero,
    var value: Float = 1f,
    var currentTime: Long = 25000L,
    var isTimerRunning: Boolean = true
)