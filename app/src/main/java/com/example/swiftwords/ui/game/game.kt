package com.example.swiftwords.ui.game

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
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.swiftwords.ui.AppViewModelProvider
import com.example.swiftwords.R
import kotlinx.coroutines.launch

@Composable
fun Game(
    listOfLetters: Array<Char>,
    wordList: Set<String>,
    viewModel: GameViewModel = viewModel(factory = AppViewModelProvider.Factory),
    navigateUp: () -> Unit
) {
    val gameUiState by viewModel.uiState.collectAsState()

    val coroutineScope = rememberCoroutineScope()
    var textState by rememberSaveable { mutableStateOf("") }
    var isCorrect by rememberSaveable { mutableStateOf<Boolean?>(null) }
    var isLoading by rememberSaveable { mutableStateOf(false) }

    // Function to check the answer and update the UI state
    val checkAnswer: () -> Unit = remember { // remember so it doesn't composition
        {
            isLoading = true
            coroutineScope.launch {
                isCorrect = viewModel.checkAnswer(textState, wordList, listOfLetters)
                isLoading = false
                textState = ""  // Reset textState after isCorrect is updated
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.padding(5.dp))

        TimerBar(gameUiState.value, navigateUp, gameUiState.currentTime)

        Spacer(modifier = Modifier.padding(10.dp))

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            RowOfLetters(listOfLetters[0], listOfLetters[1], listOfLetters[2])
            RowOfLetters(listOfLetters[3], listOfLetters[4], listOfLetters[5])
            RowOfLetters(listOfLetters[6], listOfLetters[7], listOfLetters[8])
        }

        Spacer(modifier = Modifier.padding(10.dp))
        Row{
            Text(
                text = when {
                    isLoading -> "Loading..."
                    isCorrect == true -> "Correct answer!"
                    isCorrect == false -> "Incorrect answer."
                    else -> "Please enter an answer."
                },
                style = MaterialTheme.typography.bodyLarge // Optional: Customize text style
            )
            Text(
                text = "score: ${gameUiState.score}",
                style = MaterialTheme.typography.bodyLarge // Optional: Customize text style
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, end = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            CustomTextField(
                textState,
                { newText -> textState = newText },
                checkAnswer
            )

            ElevatedButton(
                onClick = checkAnswer,
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
fun CustomTextField(textState: String, onValueChange: (String) -> Unit, onDone: () -> Unit) {
    OutlinedTextField(
        value = textState,
        singleLine = true,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(
            onDone = { onDone() }
        ),
        modifier = Modifier
            .width(250.dp),
        onValueChange = onValueChange,
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
fun RowOfLetters(letter1: Char, letter2: Char, letter3: Char) {
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
fun LetterBox(letter: Char) {
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
            Text(text = letter.toString())
        }
    }
}

@Composable
fun Timer(
    value: Float,
    inactiveBarColor: Color,
    activeBarColor: Color,
    modifier: Modifier = Modifier,
    strokeWidth: Dp = 10.dp
) {
    var size by remember {
        mutableStateOf(IntSize.Zero)
    }
    Box(
        modifier = modifier
            .fillMaxWidth(0.9f)
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
        }
    }
}

@Composable
fun TimerBar(value: Float, navigateUp: () -> Unit, currentTime: Long) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        IconButton(
            onClick = navigateUp,
            modifier = Modifier.size(27.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = stringResource(R.string.exit),
                modifier = Modifier.size(27.dp)
            )
        }
        Timer(
            value,
            Color.DarkGray,
            Color(0xFF76ffcf)
        )
        Text(
            text = if (currentTime > 1000L) {
                currentTime.toString().take(if (currentTime < 10000L) 1 else 2)
            } else {
                "0." + currentTime.toString().take(1)
            },
            modifier = Modifier.width(25.dp),
        )
    }
}