package com.example.swiftwords.ui.game

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextButton
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.swiftwords.R
import com.example.swiftwords.ui.theme.SwiftWordsTheme
import kotlinx.coroutines.launch
import kotlin.reflect.KSuspendFunction1

@Composable
fun Game(
    newTime: () -> Long,
    setOfLetters: Set<Char>,
    wordList: Set<String>,
    viewModel: GameViewModel = viewModel(factory = GameViewModelFactory(newTime)),
    navigateUp: () -> Unit
) {
    val gameUiState by viewModel.uiState.collectAsState()
    val isTimerRunning by remember { derivedStateOf { gameUiState.isTimerRunning } }

    val coroutineScope = rememberCoroutineScope()
    var textState by rememberSaveable { mutableStateOf("") }
    var isCorrect by rememberSaveable { mutableStateOf<Boolean?>(null) }
    var isLoading by rememberSaveable { mutableStateOf(false) }
    var lastMessage by rememberSaveable { mutableStateOf("Please enter an answer.") }
    val scroll = rememberScrollState()

    // Function to check the answer and update the UI state
    val checkAnswer: () -> Unit = remember { // remember so it doesn't composition
        {
            isLoading = true
            coroutineScope.launch {
                isCorrect = viewModel.checkAnswer({ textState }, wordList, setOfLetters)
                isLoading = false
                textState = ""  // Reset textState after isCorrect is updated
            }
        }
    }
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scroll),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.padding(5.dp))

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
                // most values pass as () -> type to fix the unnecessary recomposition of the ui
                Timer(
                    { gameUiState.value },
                    Color.DarkGray,
                    Color(0xFF76ffcf)
                )
                TimerText { gameUiState.currentTime }
            }
            Spacer(modifier = Modifier.padding(10.dp))

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val listOfLetters = setOfLetters.toList()

                RowOfLetters(listOfLetters[0], listOfLetters[1], listOfLetters[2]) { isCorrect }
                RowOfLetters(listOfLetters[3], listOfLetters[4], listOfLetters[5]) { isCorrect }
                RowOfLetters(listOfLetters[6], listOfLetters[7], listOfLetters[8]) { isCorrect }
            }

            Spacer(modifier = Modifier.padding(10.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = when {
                        isLoading -> lastMessage // Keep the same message while loading
                        isCorrect == true -> {
                            lastMessage = "Correct answer!"
                            lastMessage
                        }

                        isCorrect == false -> {
                            lastMessage = "Incorrect answer."
                            lastMessage
                        }

                        else -> {
                            lastMessage = "Please enter an answer."
                            lastMessage
                        }
                    }
                )
                Spacer(modifier = Modifier.padding(5.dp))
                TextScore { gameUiState.score }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 5.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                CustomTextField(
                    { textState },
                    { newText -> textState = newText },
                    checkAnswer,
                    { isTimerRunning },
                )
                CustomButton(checkAnswer) { isTimerRunning }
            }

        }
        if (!isTimerRunning) {
            DisplayResults(gameUiState.score, viewModel::restartGame, newTime, navigateUp)
        }
    }
}

@Composable
fun CustomButton(checkAnswer: () -> Unit, isTimerRunning: () -> Boolean) {
    ElevatedButton(
        onClick = checkAnswer,
        colors = ButtonDefaults.elevatedButtonColors(
            containerColor = MaterialTheme.colorScheme.secondary
        ),
        contentPadding = PaddingValues(0.dp),
        enabled = isTimerRunning(),
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

@Composable
fun CustomTextField(
    textState: () -> String,
    onValueChange: (String) -> Unit,
    onDone: () -> Unit,
    isTimerRunning: () -> Boolean
) {
    OutlinedTextField(
        value = textState(),
        singleLine = true,
        enabled = isTimerRunning(),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(
            onDone = { onDone() }
        ),
        modifier = Modifier
            .width(250.dp),
        onValueChange = onValueChange,
        label = { Text("") },
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.secondary,
            unfocusedContainerColor = MaterialTheme.colorScheme.secondary,
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent,
        ),
        shape = RoundedCornerShape(20.dp)
    )
}


@Composable
fun RowOfLetters(letter1: Char, letter2: Char, letter3: Char, isCorrect:() -> Boolean?) {
    Row(
        modifier = Modifier.wrapContentSize()
    ) {
        LetterBox(letter1, isCorrect)
        LetterBox(letter2, isCorrect)
        LetterBox(letter3, isCorrect)
    }
}


@Composable
fun TimerText(currentTime: () -> Long) {
    // Remember the score value to avoid unnecessary recompositions
    val formattedTime by remember(currentTime) {
        derivedStateOf {
            val time = currentTime()
            if (time != 130000000L) {
                if (time > 1000L) {
                    time.toString().take(if (time < 10000L) 1 else 2)
                } else {
                    "0." + time.toString().take(1)
                }
            } else {
                ""
            }
        }
    }

    Text(
        text = formattedTime,
        modifier = Modifier.width(25.dp)
    )
}

@Composable
fun TextScore(score: () -> Int) {
    // Remember the score value to avoid unnecessary recompositions
    val scoreValue by remember {
        derivedStateOf {
            score().toString()
        }
    }

    // Only recomposes if scoreValue changes
    Text(
        text = "score: $scoreValue",
        style = MaterialTheme.typography.bodyLarge // Optional: Customize text style
    )
}

@Composable
fun LetterBox(letter: Char, isCorrect: () -> Boolean?, isDarkTheme: Boolean = isSystemInDarkTheme()) {
    // Compute shadowDp based on the theme
    val shadowDp = if (isDarkTheme) 25.dp else 9.dp

    // Determine shadowColor based on correctness and theme
    val shadowColor = remember(isCorrect(), isDarkTheme) {
        when {
            isCorrect() == true && isDarkTheme -> Color(0xFF078b2c) // Dark theme correct color
            isCorrect() == true && !isDarkTheme -> Color(0xFF00c555) // Light theme correct color
            isCorrect() == false && isDarkTheme -> Color(0xFFcb2020) // Dark theme incorrect color
            isCorrect() == false && !isDarkTheme -> Color(0xFFe80000) // Light theme incorrect color
            else -> Color.Blue // Default color if isCorrect is null
        }
    }

    // Smoothly animate the color change
    val animatedColor by animateColorAsState(
        targetValue = shadowColor,
        animationSpec = tween(durationMillis = 250),
        label = ""
    )

    ElevatedCard(
        modifier = Modifier
            .padding(5.dp)
            .size(60.dp)
            .shadow(
                shadowDp, // Use the calculated shadow dp value
                shape = RoundedCornerShape(15.dp),
                spotColor = animatedColor // Use the animated color
            )
            .clip(MaterialTheme.shapes.medium)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.onPrimary),
            contentAlignment = Alignment.Center,
        ) {
            Text(text = letter.toString())
        }
    }
}

@Composable
fun Timer(
    value: () -> Float,
    inactiveBarColor: Color,
    activeBarColor: Color,
    modifier: Modifier = Modifier,
    strokeWidth: Dp = 10.dp,
    isDarkTheme: Boolean = isSystemInDarkTheme()
) {
    var size by remember {
        mutableStateOf(IntSize.Zero)
    }
    val color = if(!isDarkTheme) activeBarColor else Color(0xFF082952)
    Box(
        modifier = modifier
            .fillMaxWidth(0.9f)
            .onSizeChanged {
                size = it
            }
    ) {
        // draw the timer
        Canvas(modifier = modifier) {
            val sizePx = size
            val center = Offset(sizePx.width / 2f, sizePx.height / 2f)
            val lineLength = sizePx.width / 1.2f
            val currentValue = value()

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
                color = color,
                start = Offset(center.x - lineLength / 2, center.y),
                end = Offset(center.x - lineLength / 2 + lineLength * currentValue, center.y),
                strokeWidth = strokeWidth.toPx(),
                cap = StrokeCap.Round
            )
        }
    }
}

@Composable
fun DisplayResults(
    score: Int,
    restart: KSuspendFunction1<Long, Unit>,
    time: () -> Long,
    navigateUp: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.4f)),
    ) {
        Card(
            modifier = Modifier
                .align(Alignment.Center),
        ) {
            Column(
                modifier = Modifier
                    .padding(10.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.sage),
                    modifier = Modifier.size(220.dp),
                    contentDescription = null
                )
                if (score >= 10) {
                    Text(
                        "Congrats!! you passed with score: $score",
                        style = MaterialTheme.typography.titleMedium
                    )
                } else {
                    Text(
                        "you failed :( with score: $score",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(
                        onClick = navigateUp,
                    ) {
                        Text("Exit")
                    }
                    TextButton(
                        onClick = {
                            coroutineScope.launch {
                                restart(time())
                            }
                        },
                    ) {
                        Text("Try Again (-1 life)")
                    }
                    TextButton(
                        onClick = {
                            coroutineScope.launch {
                                restart(time())
                            }
                        },
                        enabled = score >= 10
                    ) {
                        Text("Next Level")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Preview(viewModel: GameViewModel = viewModel(factory = GameViewModelFactory({ 50L }))) {
    SwiftWordsTheme {
        DisplayResults(5, viewModel::restartGame, { 50L }, {})
    }
}
@Preview(showBackground = true)
@Composable
fun Preview2() {
    SwiftWordsTheme {
        LetterBox('a', { true },true)
    }
}