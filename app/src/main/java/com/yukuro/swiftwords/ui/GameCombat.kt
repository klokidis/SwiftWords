package com.yukuro.swiftwords.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yukuro.swiftwords.R
import com.yukuro.swiftwords.data.DataSource
import com.yukuro.swiftwords.viewmodels.GameViewModel
import com.yukuro.swiftwords.viewmodels.GameViewModelFactory
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun GameCombat(
    newTime: () -> Long,
    wordList: Set<String>,
    colorTheme: Int,
    colorCodePlayerOne: Int,
    colorCodePlayerTwo: Int,
    navigateUp: () -> Unit,
    setOfLetters: Set<Char>,
    listOfLetters: List<Char>,
    characterIsFemale: Boolean,
    playCorrectSound: () -> Unit,
    playIncorrectSound: () -> Unit,
    generateRandomLettersForMode: () -> Unit,
    viewModel: GameViewModel = viewModel(factory = GameViewModelFactory(newTime)),
) {
    val gameUiState by viewModel.uiState.collectAsState()
    val isTimerRunning by remember { derivedStateOf { gameUiState.isTimerRunning } }
    val coroutineScope = rememberCoroutineScope()
    var inputTextStatePlayerOne by rememberSaveable { mutableStateOf("") }// this can go to a viewmodel
    var inputTextStatePlayerTwo by rememberSaveable { mutableStateOf("") }
    var outPutNumberPlayerOne by rememberSaveable { mutableStateOf<Int?>(null) } // 3 Word already answered 1 correct 2 false
    var outPutNumberPlayerTwo by rememberSaveable { mutableStateOf<Int?>(null) } // 3 Word already answered 1 correct 2 false
    var isLoading by rememberSaveable { mutableStateOf(false) }
    val scroll = rememberScrollState()
    val checkAnswer: (player: Int) -> Unit =
        remember(setOfLetters) { // remember so it doesn't composition
            { player ->
                isLoading = true
                coroutineScope.launch {
                    if (player == 1) {
                        outPutNumberPlayerOne = viewModel.checkAnswerCombat(
                            {
                                inputTextStatePlayerOne
                            },
                            wordList,
                            setOfLetters,
                            player
                        )
                    } else {
                        outPutNumberPlayerTwo = viewModel.checkAnswerCombat(
                            {
                                inputTextStatePlayerTwo
                            },
                            wordList,
                            setOfLetters,
                            player
                        )
                    }
                    isLoading = false
                    if (player == 1) {
                        inputTextStatePlayerOne = ""
                        when (outPutNumberPlayerOne) {
                            1 -> {
                                playCorrectSound()
                            }

                            else -> {
                                playIncorrectSound()
                            }
                        }
                    } else {
                        inputTextStatePlayerTwo = ""
                        when (outPutNumberPlayerTwo) {
                            1 -> {
                                playCorrectSound()
                            }

                            else -> {
                                playIncorrectSound()
                            }
                        }
                    }
                }
            }
        }
    // Handle the back press
    BackHandler {
        navigateUp()
        viewModel.stopClockOnExit()
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scroll),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Column(
            modifier = Modifier
                .graphicsLayer {
                    rotationX = 180f
                    rotationY = 180f
                }, // Flips everything upside down
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutPutMessageCombat(
                isLoading,
                { outPutNumberPlayerOne },
                { gameUiState.scorePlayerOne },
                { gameUiState.scorePlayerTwo },
                colorCodePlayerOne,
                colorCodePlayerTwo
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 25.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                CustomTextField(
                    { inputTextStatePlayerOne },
                    { newText -> inputTextStatePlayerOne = newText },
                    { checkAnswer(1) },
                    { isTimerRunning },
                    modifier = Modifier.weight(1f),
                    true
                )
            }
            CustomKeyboard(
                listOfLetters = listOfLetters,
                colorCodePlayerOne,
                onClick = { newText ->
                    inputTextStatePlayerOne += newText
                },
                onEnter = { checkAnswer(1) },
                onRemove = { inputTextStatePlayerOne = inputTextStatePlayerOne.dropLast(1) }
            )
        }

        Spacer(modifier = Modifier.weight(0.5f))

        Spacer(modifier = Modifier.padding(2.dp))

        ChangingColorTimer(
            value = { gameUiState.value }, // Pass the value directly
            scoreOne = { gameUiState.scorePlayerOne },
            scoreTwo = { gameUiState.scorePlayerTwo },
            colorCodeOne = colorCodePlayerOne,
            colorCodeTwo = colorCodePlayerTwo,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )

        Spacer(modifier = Modifier.padding(2.dp))

        Spacer(modifier = Modifier.weight(0.5f))

        OutPutMessageCombat(
            isLoading,
            { outPutNumberPlayerTwo },
            { gameUiState.scorePlayerTwo },
            { gameUiState.scorePlayerOne },
            colorCodePlayerTwo,
            colorCodePlayerOne
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 10.dp,
                    end = 25.dp
                ), // our CustomTextField has padding start = 20.dp, end = 5.dp so we balance it with new row padding
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            CustomTextField(
                { inputTextStatePlayerTwo },
                { newText -> inputTextStatePlayerTwo = newText },
                { checkAnswer(2) },
                { isTimerRunning },
                modifier = Modifier.weight(1f),
                true
            )
        }
        CustomKeyboard(
            listOfLetters = listOfLetters,
            colorCodePlayerTwo,
            onClick = { newText ->
                inputTextStatePlayerTwo += newText
            },
            onEnter = { checkAnswer(2) },
            onRemove = { inputTextStatePlayerTwo = inputTextStatePlayerTwo.dropLast(1) }
        )
        Spacer(modifier = Modifier.weight(1f))
    }
    Box(
        modifier = Modifier,
        contentAlignment = Alignment.TopStart
    ) {
        IconButton(
            onClick = {
                navigateUp()
                viewModel.stopClockOnExit()
            },
            modifier = Modifier
                .size(35.dp)
                .padding(start = 7.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = stringResource(R.string.exit),
                modifier = Modifier.size(35.dp)
            )
        }
    }
    DisplayResults(
        time = newTime,
        isVisible = !isTimerRunning,
        restartGame = {
            inputTextStatePlayerOne = ""
            inputTextStatePlayerTwo = ""
            outPutNumberPlayerOne = null
            outPutNumberPlayerTwo = null
        },
        navigateUp = navigateUp,
        restart = viewModel::restartGame,
        characterIsFemale = characterIsFemale,
        generateRandomLettersForMode = generateRandomLettersForMode,
        stopClockOnExit = viewModel::stopClockOnExit,
        playerOneScore = { gameUiState.scorePlayerOne },
        playerTwoScore = { gameUiState.scorePlayerTwo },
        colorOne = DataSource().colorPairs[colorCodePlayerOne].darkColor,
        colorTwo = DataSource().colorPairs[colorCodePlayerTwo].darkColor,
        colorTheme = DataSource().colorPairs[colorTheme].darkColor
    )
}

@Composable
private fun OutPutMessageCombat(
    isLoading: Boolean,
    isCorrect: () -> Int?,
    scorePlayer: () -> Int,
    scoreEnemy: () -> Int,
    colorPlayer: Int,
    colorEnemy: Int
) {
    var lastMessage by rememberSaveable { mutableStateOf("Enter an answer.") }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        TextScoreCombat(scoreEnemy, colorEnemy)
        Spacer(modifier = Modifier.padding(7.dp))
        Text(
            text = when {
                isLoading -> lastMessage // Keep the same message while loading
                isCorrect() == 1 -> {
                    lastMessage = stringResource(R.string.correct)
                    lastMessage
                }

                isCorrect() == 2 -> {
                    lastMessage = stringResource(R.string.incorrect)
                    lastMessage
                }

                isCorrect() == 3 -> {
                    lastMessage = stringResource(R.string.answered)
                    lastMessage
                }

                else -> {
                    lastMessage = stringResource(R.string.enter_answer)
                    lastMessage
                }
            },
            style = MaterialTheme.typography.titleSmall.copy(
                fontSize = 20.sp,
                color = when (isCorrect()) {
                    1 -> Color(0xFF006D2F)  // Correct color
                    2 -> Color(0xFF8D0C0C) // Incorrect color
                    3 -> Color(0xFF8D310C) // repeat color
                    else -> Color.Unspecified  // Default color when isCorrect is null
                }
            )
        )
        Spacer(modifier = Modifier.padding(7.dp))
        TextScoreCombat(scorePlayer, colorPlayer)
    }
}

@Composable
fun TextScoreCombat(score: () -> Int, color: Int) {
    // Remember the score value to avoid unnecessary recompositions
    val scoreValue by remember {
        derivedStateOf {
            score().toString()
        }
    }

    // Only recomposes if scoreValue changes
    Text(
        scoreValue,
        color = DataSource().colorPairs[color].darkColor,
        style = MaterialTheme.typography.titleSmall.copy(fontSize = 20.sp)
    )
}

@Composable
fun DisplayResults(
    time: () -> Long,
    isVisible: Boolean,
    restartGame: () -> Unit,
    navigateUp: () -> Unit,
    restart: (Long) -> Unit,
    characterIsFemale: Boolean,
    colorOne: Color,
    colorTwo: Color,
    generateRandomLettersForMode: () -> Unit,
    stopClockOnExit: () -> Unit,
    playerOneScore: () -> Int,
    playerTwoScore: () -> Int,
    colorTheme: Color,
) {
    var buttonsEnabled by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    val playerWonText by remember(playerOneScore(), playerTwoScore()) {
        mutableIntStateOf(
            if (playerOneScore() > playerTwoScore()) {
                R.string.player_one_won
            } else if (playerOneScore() < playerTwoScore()) {
                R.string.player_two_won
            } else {
                R.string.player_tie
            }
        )
    }

    if (isVisible) {

        // Launch a coroutine to enable the buttons after a delay
        LaunchedEffect(Unit) {
            delay(800L)
            buttonsEnabled = true
        }

        Dialog(onDismissRequest = { }) {

            BackHandler {
                navigateUp()
                stopClockOnExit()
            }

            Card(
                colors = CardDefaults.cardColors(
                    MaterialTheme.colorScheme.secondary
                )
            ) {
                Column(
                    modifier = Modifier
                        .padding(10.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(
                            id = if (characterIsFemale) {
                                R.drawable.female_half
                            } else {
                                R.drawable.male_half
                            }
                        ),
                        modifier = Modifier.size(260.dp),
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.padding(1.dp))
                    Text(
                        text = buildAnnotatedString { //separates the colors on the text
                            withStyle(style = SpanStyle(color = colorOne)) {
                                append("${playerOneScore()}")
                            }
                            append(" vs ")
                            withStyle(style = SpanStyle(color = colorTwo)) {
                                append("${playerTwoScore()}")
                            }
                        }, style = MaterialTheme.typography.titleSmall.copy(
                            fontSize = 20.sp
                        ),
                        color = colorTheme
                    )
                    Spacer(modifier = Modifier.padding(1.dp))
                    Text(
                        text = buildAnnotatedString { //separates the colors on the text
                            append(stringResource(playerWonText))
                        }, style = MaterialTheme.typography.titleSmall.copy(
                            fontSize = 18.sp
                        ),
                        color = colorTheme
                    )
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextButton(
                            onClick = {
                                navigateUp()
                                stopClockOnExit()
                            },
                            enabled = buttonsEnabled
                        ) {
                            Text(
                                stringResource(R.string.exit),
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontSize = 18.sp
                                ),
                                color = if (buttonsEnabled) {
                                    colorTheme
                                } else {
                                    Color.Gray
                                }
                            )
                        }
                        TextButton(
                            onClick = {
                                restartGame()
                                coroutineScope.launch {
                                    generateRandomLettersForMode()
                                    restart(time())
                                }
                            },
                            enabled = buttonsEnabled
                        ) {
                            Text(
                                stringResource(R.string.play_again),
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontSize = 18.sp
                                ),
                                color = if (buttonsEnabled) {
                                    colorTheme
                                } else {
                                    Color.Gray
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ChangingColorTimer(
    value: () -> Float,
    colorCodeOne: Int,
    colorCodeTwo: Int,
    scoreOne: () -> Int,
    scoreTwo: () -> Int,
    modifier: Modifier = Modifier,
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    strokeWidth: Dp = 10.dp
) {
    // Calculate the winning color code based on scores
    val winningColorCode by remember {
        derivedStateOf {
            if (scoreOne() > scoreTwo()) {
                colorCodeOne
            } else {
                colorCodeTwo
            }
        }
    }
    val animatedColor by animateColorAsState(
        DataSource().colorPairs[winningColorCode].darkColor,
        animationSpec = tween(durationMillis = 1000),
        label = ""
    )
    var size by remember {
        mutableStateOf(IntSize.Zero)
    }
    val inactiveBarColor = if (isDarkTheme) {
        Color.Gray.darken()
    } else {
        Color.White.darken()
    }
    Box(
        modifier = modifier
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
                color = animatedColor,
                start = Offset(center.x - lineLength / 2, center.y),
                end = Offset(center.x - lineLength / 2 + lineLength * currentValue, center.y),
                strokeWidth = strokeWidth.toPx(),
                cap = StrokeCap.Round
            )
        }
    }
}