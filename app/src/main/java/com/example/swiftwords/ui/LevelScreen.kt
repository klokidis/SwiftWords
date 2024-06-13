package com.example.swiftwords.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color

@Composable
fun LevelScreen(){
    val animatedColor by animateColorAsState(
        if (true) MaterialTheme.colorScheme.background else Color.Blue,
        label = "color"
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .drawBehind {
                drawRect(animatedColor)
            }
    ) {

    }
}

