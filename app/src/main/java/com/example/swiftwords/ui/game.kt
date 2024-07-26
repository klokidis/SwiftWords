package com.example.swiftwords.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.swiftwords.ui.theme.SwiftWordsTheme
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import kotlinx.coroutines.delay

@Composable
fun Game(listOfLetters: List<String>) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.padding(15.dp))
        Timer(10,Color.DarkGray,Color.Green,Color.Gray)
        Spacer(modifier = Modifier.padding(15.dp))
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            RowOfLetters(listOfLetters[0], listOfLetters[1], listOfLetters[2])
            RowOfLetters(listOfLetters[3], listOfLetters[4], listOfLetters[5])
            RowOfLetters(listOfLetters[6], listOfLetters[7], listOfLetters[8])
        }
        Spacer(modifier = Modifier.padding(30.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, end = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            CustomTextField()
            ElevatedButton(
                onClick = { },
                contentPadding = PaddingValues(0.dp),
                modifier = Modifier
                    .padding(start = 10.dp, top = 5.dp)
                    .width(70.dp)
                    .height(50.dp)
            ) {
                Text(
                    text = "check",
                    maxLines = 1,
                )
            }
        }

    }

}

@Composable
fun CustomTextField() {
    var textState by remember { mutableStateOf("") }
    OutlinedTextField(
        value = textState,
        modifier = Modifier
            .width(230.dp),
        onValueChange = { textState = it },
        label = { Text("") },
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.inverseOnSurface,
            unfocusedContainerColor = MaterialTheme.colorScheme.inverseOnSurface,
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent,

            ),
        shape = RoundedCornerShape(20.dp)
    )
}


@Composable
fun RowOfLetters(letter1: String, letter2: String, letter3: String) {
    Row(
        modifier = Modifier
            .wrapContentSize()
    ) {
        LetterBox(letter1)
        LetterBox(letter2)
        LetterBox(letter3)
    }
}

@Composable
fun LetterBox(letter: String) {
    ElevatedCard(
        modifier = Modifier
            .padding(5.dp)
            .size(60.dp)
            .shadow(
                9.dp,
                shape = RoundedCornerShape(15.dp),
                spotColor = Color.Blue
            )
            .clip(MaterialTheme.shapes.medium)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Text(text = letter)
        }
    }
}

@Composable
fun Timer(
    // total time of the timer
    totalTime: Long,

    // circular handle color
    handleColor: Color,

    // color of inactive bar / progress bar
    inactiveBarColor: Color,

    // color of active bar
    activeBarColor: Color,
    modifier: Modifier = Modifier,

    // set initial value to 1
    initialValue: Float = 1f,
    strokeWidth: Dp = 5.dp
) {
    // create variable for
    // size of the composable
    var size by remember {
        mutableStateOf(IntSize.Zero)
    }
    // create variable for value
    var value by remember {
        mutableStateOf(initialValue)
    }
    // create variable for current time
    var currentTime by remember {
        mutableStateOf(totalTime)
    }
    // create variable for isTimerRunning
    var isTimerRunning by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(key1 = currentTime, key2 = isTimerRunning) {
        if (currentTime > 0 && isTimerRunning) {
            delay(100L)
            currentTime -= 100L
            value = currentTime / totalTime.toFloat()
        }
    }
    Box(
        modifier = modifier
            .fillMaxWidth()
            .onSizeChanged {
                size = it
            }
    ) {
        // draw the timer
        Canvas(modifier = modifier) {
            val center = Offset(size.width / 2f, size.height / 2f)
            val lineLength = size.width / 1.2f

            // Draw the inactive line
            drawLine(
                color = inactiveBarColor,
                start = Offset(center.x - lineLength / 2, center.y),
                end = Offset(center.x + lineLength / 2, center.y),
                strokeWidth = strokeWidth.toPx(),
                cap = StrokeCap.Round
            )

            // Draw the active line based on the current value
            drawLine(
                color = activeBarColor,
                start = Offset(center.x - lineLength / 2, center.y),
                end = Offset(center.x - lineLength / 2 + lineLength * value, center.y),
                strokeWidth = strokeWidth.toPx(),
                cap = StrokeCap.Round
            )

            // Draw the pointer/cap at the end of the active line
            drawCircle(
                color = handleColor,
                radius = (strokeWidth * 1.5f).toPx(),
                center = Offset(center.x - lineLength / 2 + lineLength * value, center.y)
            )
        }

    }
}


@Preview(showBackground = true)
@Composable
fun GamePreview() {
    SwiftWordsTheme {
        Game(listOf("A", "B", "C", "D", "E", "F", "G", "H", "O"))
    }
}