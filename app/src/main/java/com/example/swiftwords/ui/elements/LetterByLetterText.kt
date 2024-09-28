package com.example.swiftwords.ui.elements

import android.util.Log
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay


@Composable
fun LetterByLetterText(
    text: String,
    modifier: Modifier = Modifier,
    delay: Long = 40L,
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    soundViewModel: SoundViewModel,
    characterIsMale: Boolean,
) {
    var visibleText by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(Unit) {
        animateText(text, delay, characterIsMale, soundViewModel) { newText ->
            visibleText = newText
        }
    }

    // Display the text with wrapping
    Column(modifier = modifier.padding(10.dp)) {
        var lineWidth by remember { mutableStateOf(0.dp) } //CHANGE TO VIEWMODEL

        Text(
            text = visibleText,
            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 25.sp),
            color = if (isDarkTheme) {
                Color.White
            } else {
                Color.Black
            },
            modifier = Modifier
                .onGloballyPositioned {
                    lineWidth = it.size.width.dp
                }
                .padding(end = 4.dp) // Extra padding to ensure text doesn't wrap too tightly
        )
    }
}

private suspend fun animateText(
    text: String,
    timeDelay: Long,
    characterIsMale: Boolean,
    soundViewModel: SoundViewModel,
    callback: (String) -> Unit
) {

    for (i in text.indices) {
        val currentLetter = text[i]
        val pitch = if (characterIsMale) {
            1.3f
        } else {
            2f
        }
        Log.d("kloki", currentLetter.toString())

        if (i % 2 == 0) {
            soundViewModel.playLetterSound(currentLetter, pitch)
        }
        // Delay between each letter
        delay(timeDelay)

        // Update the callback with the current substring
        callback(text.substring(0, i + 1))
    }
}
