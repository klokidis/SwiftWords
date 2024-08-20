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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun LetterByLetterText(
    text: String,
    modifier: Modifier = Modifier,
    delay: Long = 50L,
) {
    var visibleText by rememberSaveable { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            animateText(text, delay) { newText ->
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

private suspend fun animateText(text: String, timeDelay: Long, callback: (String) -> Unit) {
    for (i in text.indices) {
        delay(timeDelay) // Delay between each letter
        callback(text.substring(0, i + 1))
    }
}