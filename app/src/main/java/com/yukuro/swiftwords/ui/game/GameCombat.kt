package com.yukuro.swiftwords.ui.game

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yukuro.swiftwords.R
import com.yukuro.swiftwords.data.DataSource
import kotlinx.coroutines.launch

@Composable
fun GameCombat(
    newTime: () -> Long,
    wordList: Set<String>,
    colorCodePlayerOne: Int,
    colorCodePlayerTwo: Int,
    navigateUp: () -> Unit,
    setOfLetters: Set<Char>,
    listOfLetters: List<Char>,
    characterIsFemale: Boolean,
    playCorrectSound: () -> Unit,
    playIncorrectSound: () -> Unit,
    generateRandomLettersForMode: () -> Unit,
    exitChangingMode: () -> Unit,
    viewModel: GameViewModel = viewModel(factory = GameViewModelFactory(newTime)),
) {
    val gameUiState by viewModel.uiState.collectAsState()
    val isTimerRunning by remember { derivedStateOf { gameUiState.isTimerRunning } }
    val coroutineScope = rememberCoroutineScope()
    var inputTextStatePlayerOne by rememberSaveable { mutableStateOf("") }
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
                    } else {
                        inputTextStatePlayerTwo = ""
                    }
                    if (player == 1) {
                        when (outPutNumberPlayerOne) {
                            1 -> {
                                playCorrectSound()
                            }

                            else -> {
                                playIncorrectSound()
                            }
                        }
                    } else {
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
                DataSource().colorPairs[colorCodePlayerOne].darkColor,
                DataSource().colorPairs[colorCodePlayerTwo].darkColor
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

        UpperLevelUi(
            navigateUp,
            true,
            exitChangingMode,
            true,
            { gameUiState.score },
            { gameUiState.value },
            { gameUiState.currentTime },
            {},
            generateRandomLettersForMode,
            0,
            { false },
            colorCodePlayerOne,
            5,
            currentLevel = 0,
            stopClockOnExit = viewModel::stopClockOnExit,
            calculatePassingScore = viewModel::calculatePassingScore,
            showExitButton = false,
            showText = false
        )

        Spacer(modifier = Modifier.padding(2.dp))

        Spacer(modifier = Modifier.weight(0.5f))

        OutPutMessageCombat(
            isLoading,
            { outPutNumberPlayerTwo },
            { gameUiState.scorePlayerTwo },
            { gameUiState.scorePlayerOne },
            DataSource().colorPairs[colorCodePlayerTwo].darkColor,
            DataSource().colorPairs[colorCodePlayerOne].darkColor
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, end = 25.dp), // our CustomTextField has padding start = 20.dp, end = 5.dp so we balance it with new row padding
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
}

@Composable
private fun OutPutMessageCombat(
    isLoading: Boolean,
    isCorrect: () -> Int?,
    scorePlayer: () -> Int,
    scoreEnemy: () -> Int,
    colorPlayer: Color,
    colorEnemy: Color
) {
    var lastMessage by rememberSaveable { mutableStateOf("Enter an answer.") }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.padding(start = 10.dp)
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
fun TextScoreCombat(score: () -> Int, color: Color) {
    // Remember the score value to avoid unnecessary recompositions
    val scoreValue by remember {
        derivedStateOf {
            score().toString()
        }
    }

    // Only recomposes if scoreValue changes
    Text(
        scoreValue,
        color = color,
        style = MaterialTheme.typography.titleSmall.copy(fontSize = 18.sp)
    )
}
