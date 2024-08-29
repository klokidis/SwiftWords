package com.example.swiftwords.ui.elements

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.swiftwords.ui.AppViewModelProvider
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random


@Composable
fun LetterByLetterText(
    text: String,
    modifier: Modifier = Modifier,
    delay: Long = 40L,
) {
    val soundViewModel: SoundViewModel = viewModel(factory = AppViewModelProvider.Factory)
    var visibleText by rememberSaveable { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            animateText(text, delay,soundViewModel) { newText ->
                visibleText = newText
            }
        }
    }

    // Display the text with wrapping
    Column(modifier = modifier.padding(10.dp)) {
        var lineWidth by remember { mutableStateOf(0.dp) } //CHANGE TO VIEWMODEL

        Text(
            text = visibleText,
            style = MaterialTheme.typography.bodyMedium,
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
    soundViewModel: SoundViewModel,
    callback: (String) -> Unit
) {
    var lastPlayed = -1 // Initialize with an invalid number

    for (i in text.indices) {
        val pitch = 0.8f

        if (i % 2 == 0) {
            var randomInt: Int
            do {
                randomInt = Random.nextInt(1, 6)
            } while (randomInt == lastPlayed || (randomInt == 5 && lastPlayed == 4) || (randomInt == 4 && lastPlayed == 5))

            // Update the lastPlayed variable
            lastPlayed = randomInt

            // Play sound with the corresponding pitch
            when (randomInt) {
                1 -> soundViewModel.playV1(pitch)
                2 -> soundViewModel.playV2(pitch)
                3 -> soundViewModel.playV3(pitch)
                4 -> soundViewModel.playV4(pitch)
                5 -> soundViewModel.playV5(pitch)
            }
        }
        delay(timeDelay) // Delay between each letter
        callback(text.substring(0, i + 1))
    }
}