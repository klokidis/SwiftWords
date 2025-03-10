package com.yukuro.swiftwords.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.shrinkHorizontally
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextButton
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yukuro.swiftwords.R
import com.yukuro.swiftwords.data.ColorPair
import com.yukuro.swiftwords.data.DataSource
import com.yukuro.swiftwords.notifications.scheduleDailyNotification
import com.yukuro.swiftwords.viewmodels.GameViewModel
import com.yukuro.swiftwords.viewmodels.GameViewModelFactory
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun Game(
    dateNow: String,
    dataDate: () -> String,
    newTime: () -> Long,
    wordList: Set<String>,
    colorCode: Int,
    isMode: Boolean,
    gameModeNumber: Int,
    increaseScore: (Int) -> Unit,
    navigateUp: () -> Unit,
    checkHighScore: (Int) -> Unit,
    setOfLetters: Set<Char>,
    highScore: Int,
    checked: () -> Boolean,
    changeChecked: () -> Unit,
    increaseStreak: () -> Unit,
    listOfLetters: List<Char>,
    shuffle: () -> Unit,
    exitChangingMode: () -> Unit,
    launchChanging: () -> Unit,
    currentLevel: Int,
    streakLevel: Int,
    characterIsFemale: Boolean,
    playCorrectSound: () -> Unit,
    playIncorrectSound: () -> Unit,
    generateRandomLettersForBoth: () -> Unit,
    generateRandomLettersForBothOnExit: () -> Unit,
    generateRandomLettersForMode: () -> Unit,
    viewModel: GameViewModel = viewModel(factory = GameViewModelFactory(newTime)),
) {
    val gameUiState by viewModel.uiState.collectAsState()
    val isTimerRunning by remember { derivedStateOf { gameUiState.isTimerRunning } }
    val coroutineScope = rememberCoroutineScope()
    var inputTextState by rememberSaveable { mutableStateOf("") }
    var outPutNumber by rememberSaveable { mutableStateOf<Int?>(null) } // 3 Word already answered 1 correct 2 false
    var isLoading by rememberSaveable { mutableStateOf(false) }
    var onExitButtonPressed by rememberSaveable { mutableStateOf(false) }
    val scroll = rememberScrollState()
    val context = LocalContext.current
    val checkAnswer: () -> Unit = remember(setOfLetters) { // remember so it doesn't composition
        {
            isLoading = true
            coroutineScope.launch {
                outPutNumber = viewModel.checkAnswer(
                    { inputTextState },
                    wordList,
                    setOfLetters
                )
                isLoading = false
                inputTextState = ""  // Reset textState after isCorrect is updated
                when (outPutNumber) {
                    1 -> {
                        playCorrectSound()
                        if (gameModeNumber == 3 && isMode) {
                            viewModel.addTime()
                        }
                    }

                    else -> {
                        playIncorrectSound()
                        if (gameModeNumber == 3 && isMode) {
                            viewModel.removeTime()
                        }
                    }
                }
            }
        }
    }
    // Handle the back press
    BackHandler {
        navigateUp()
        if (isMode) {
            exitChangingMode()
        }
        if (!onExitButtonPressed && gameUiState.score >= viewModel.calculatePassingScore(
                currentLevel
            ) && !isMode
        ) {
            increaseScore(gameUiState.score)
            scheduleDailyNotification(context, streakLevel)
            generateRandomLettersForBothOnExit()
        }
        viewModel.stopClockOnExit()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scroll),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.padding(2.dp))
        UpperLevelUi(
            navigateUp,
            isMode,
            exitChangingMode,
            onExitButtonPressed,
            { gameUiState.score },
            { gameUiState.value },
            { gameUiState.currentTime },
            increaseScore,
            generateRandomLettersForBothOnExit,
            streakLevel,
            checked,
            colorCode,
            gameModeNumber,
            currentLevel = currentLevel,
            stopClockOnExit = viewModel::stopClockOnExit,
            calculatePassingScore = viewModel::calculatePassingScore,
            showExitButton = true,
            showText = true
        )

        if (!checked()) {
            Spacer(modifier = Modifier.padding(10.dp))
        } else {
            Spacer(modifier = Modifier.weight(1f))
        }

        MiddleLevelUi(
            checked,
            listOfLetters,
            colorCode,
            { outPutNumber }, //fixing recomposition
            { gameUiState.value },
            { gameUiState.currentTime },
        )

        if (!checked()) {
            Spacer(modifier = Modifier.padding(10.dp))
        } else {
            Spacer(modifier = Modifier.weight(1f))
        }

        OutPutMessage(
            isLoading,
            { outPutNumber },
            viewModel::calculatePassingScore,
            currentLevel,
            { gameUiState.score },
            isMode
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, end = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            CustomTextField(
                { inputTextState },
                { newText -> inputTextState = newText },
                checkAnswer,
                { isTimerRunning },
                modifier = Modifier.weight(1f),
                checked()
            )
            if (!checked()) {
                CustomButton(
                    checkAnswer = checkAnswer,
                    colorCode = colorCode,
                    isTimerRunning = { isTimerRunning },
                )
            } else {
                Spacer(modifier = Modifier.size(25.dp))
            }
        }
        Spacer(modifier = Modifier.weight(0.2f))
        if (checked()) {
            CustomKeyboard(
                listOfLetters = listOfLetters,
                colorCode,
                onClick = { newText ->
                    inputTextState += newText
                },
                onEnter = checkAnswer,
                onRemove = { inputTextState = inputTextState.dropLast(1) }
            )
        }
        Spacer(modifier = Modifier.weight(1f))

        BottomButtons(
            checked,
            changeChecked,
            shuffle,
            { gameUiState.score },
            viewModel::calculatePassingScore,
            currentLevel,
            viewModel::stopClock,
            isMode
        )
    }
    DisplayResults(
        { gameUiState.score },
        viewModel::restartGame,
        newTime,
        navigateUp,
        increaseScore,
        generateRandomLettersForBoth = generateRandomLettersForBoth,
        generateRandomLettersForBothOnExit = generateRandomLettersForBothOnExit,
        generateRandomLettersForMode = generateRandomLettersForMode,
        calculatePassingScore = viewModel::calculatePassingScore,
        isMode = isMode,
        highScore = highScore,
        checkHighScore = checkHighScore,
        isVisible = !isTimerRunning,
        restartGame = {
            inputTextState = ""
            outPutNumber = null
        },
        exitPressed = { onExitButtonPressed = true },
        dateNow = dateNow,
        dataDate = dataDate,
        increaseStreak = increaseStreak,
        colorCode = colorCode,
        launchChanging = launchChanging,
        gameModeNumber = gameModeNumber,
        currentLevel = currentLevel,
        streakLevel = streakLevel,
        stopClockOnExit = viewModel::stopClockOnExit,
        characterIsFemale = characterIsFemale,
        getFireImage = viewModel::getFireImage
    )
}

@Composable
private fun BottomButtons(
    checked: () -> Boolean,
    changeChecked: () -> Unit,
    shuffle: () -> Unit,
    score: () -> Int,
    calculatePassingScore: (Int) -> Int,
    currentLevel: Int,
    stopClock: () -> Unit,
    isMode: Boolean
) {
    // Use derivedStateOf to avoid triggering recomposition unnecessarily
    val isPassingScore by remember(score, calculatePassingScore, currentLevel) {
        derivedStateOf { score() >= calculatePassingScore(currentLevel) }
    }
    Row(
        modifier = Modifier
            .padding(3.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center

    ) {
        Spacer(modifier = Modifier.padding(2.dp))
        IconButton(
            onClick = {
                changeChecked()
            },
            modifier = Modifier
                .size(35.dp)
        ) {
            Icon(
                modifier = Modifier.fillMaxSize(),
                imageVector = ImageVector.vectorResource(if (checked()) R.drawable.keyboard_filled_24px else R.drawable.keyboard_24px),
                contentDescription = stringResource(R.string.keyboard),
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        AnimatedVisibility(
            visible = isPassingScore && !isMode,
            enter = scaleIn(
                initialScale = 0.8f,
                animationSpec = tween(
                    durationMillis = 1000,
                    easing = CubicBezierEasing(0.34f, 3f, 0.64f, 1f)
                )
            ) + fadeIn(animationSpec = tween(durationMillis = 100)),
            exit = fadeOut() + shrinkHorizontally()
        ) {
            TextButton(
                onClick = {
                    stopClock()
                },
                enabled = isPassingScore,
                modifier = Modifier
                    .height(35.dp)
            ) {
                Text(
                    text = stringResource(R.string.completeLevel),
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontSize = 17.sp,
                        color = Color(0xFF006D2F)
                    )
                )
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        IconButton(
            onClick = {
                shuffle()
            },
            modifier = Modifier
                .size(35.dp)
        ) {
            Icon(
                modifier = Modifier.fillMaxSize(),
                imageVector = ImageVector.vectorResource(R.drawable.shuffle_24px),
                contentDescription = stringResource(R.string.shuffle)
            )
        }
        Spacer(modifier = Modifier.padding(2.dp))
    }
}

@Composable
private fun OutPutMessage(
    isLoading: Boolean,
    isCorrect: () -> Int?,
    calculatePassingScore: (Int) -> Int,
    currentLevel: Int,
    score: () -> Int,
    isMode: Boolean
) {
    var lastMessage by rememberSaveable { mutableStateOf("Please enter an answer.") }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.padding(start = 10.dp)
    ) {
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
                fontSize = 17.sp,
                color = when (isCorrect()) {
                    1 -> Color(0xFF006D2F)  // Correct color
                    2 -> Color(0xFF8D0C0C) // Incorrect color
                    3 -> Color(0xFF8D310C) // repeat color
                    else -> Color.Unspecified  // Default color when isCorrect is null
                }
            )
        )
        Spacer(modifier = Modifier.padding(5.dp))
        TextScore(
            currentPassingScore = calculatePassingScore(currentLevel),
            score = score,
            isMode = isMode
        )
    }
}

@Composable
private fun MiddleLevelUi(
    checked: () -> Boolean,
    listOfLetters: List<Char>,
    colorCode: Int,
    isCorrect: () -> Int?,
    value: () -> Float,
    currentTime: () -> Long
) {
    if (!checked()) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            RowOfLetters(
                listOfLetters[0],
                listOfLetters[1],
                listOfLetters[2],
                colorCode,
                isCorrect
            )
            RowOfLetters(
                listOfLetters[3],
                listOfLetters[4],
                listOfLetters[5],
                colorCode,
                isCorrect
            )
            RowOfLetters(
                listOfLetters[6],
                listOfLetters[7],
                listOfLetters[8],
                colorCode,
                isCorrect
            )
        }
    } else {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize(1f)
                .padding(bottom = 5.dp)
        ) {
            TimerCircular(
                value = value,
                currentTime = currentTime,
                activeBarColor = DataSource().colorPairs[colorCode].darkColor,
                modifier = Modifier.size(230.dp)
            )
        }
    }
}

@Composable
fun UpperLevelUi(
    navigateUp: () -> Unit,
    isMode: Boolean,
    exitChangingMode: () -> Unit,
    onExitButtonPressed: Boolean,
    score: () -> Int,
    value: () -> Float,
    currentTime: () -> Long,
    increaseScore: (Int) -> Unit,
    generateRandomLettersForBoth: () -> Unit,
    streakLevel: Int,
    checked: () -> Boolean,
    colorCode: Int,
    gameModeNumber: Int,
    stopClockOnExit: () -> Unit,
    calculatePassingScore: (Int) -> Int,
    currentLevel: Int,
    showExitButton: Boolean,
    showText: Boolean
) {
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        if (showExitButton) {
            IconButton(
                onClick = {
                    navigateUp()
                    if (isMode) {
                        exitChangingMode()
                    }
                    if (!onExitButtonPressed && score() >= calculatePassingScore(currentLevel) && !isMode) {
                        increaseScore(score())
                        generateRandomLettersForBoth()
                        scheduleDailyNotification(context, streakLevel)
                    }
                    stopClockOnExit()
                },
                modifier = Modifier
                    .size(37.dp)
                    .padding(start = 7.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.exit),
                    modifier = Modifier.size(37.dp)
                )
            }
        }
        // most values pass as () -> type to fix the unnecessary recomposition of the ui
        if (!checked()) {
            Timer(
                value,
                colorCode,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
            if (showText) {
                TimerText(
                    modifier = Modifier.weight(0.1f),
                    currentTime = currentTime,
                    gameModeNumber = gameModeNumber
                )
            }
        } else {
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Composable
fun CustomKeyboard(
    listOfLetters: List<Char>,
    colorCode: Int,
    onClick: (String) -> Unit,
    onEnter: () -> Unit,
    onRemove: () -> Unit,
    customSize: Dp = 60.dp
) {
    Column(
        modifier = Modifier.padding(top = 5.dp, bottom = 5.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row {
            CustomLetterClick(listOfLetters[0], colorCode, onLetterClicked = onClick,customSize = customSize)
            Spacer(modifier = Modifier.size(10.dp))
            CustomLetterClick(listOfLetters[1], colorCode, onLetterClicked = onClick,customSize = customSize)
            Spacer(modifier = Modifier.size(10.dp))
            CustomLetterClick(listOfLetters[2], colorCode, onLetterClicked = onClick,customSize = customSize)
            Spacer(modifier = Modifier.size(10.dp))
            CustomLetterClick(
                thisText = stringResource(R.string.enter),
                letter = ' ',
                colorCode = colorCode,
                onLetterClicked = { onEnter() }
            )
        }
        Row {
            CustomLetterClick(listOfLetters[3], colorCode, onLetterClicked = onClick,customSize = customSize)
            Spacer(modifier = Modifier.size(10.dp))
            CustomLetterClick(listOfLetters[4], colorCode, onLetterClicked = onClick,customSize = customSize)
            Spacer(modifier = Modifier.size(10.dp))
            CustomLetterClick(listOfLetters[5], colorCode, onLetterClicked = onClick,customSize = customSize)
        }
        Row {
            CustomLetterClick(listOfLetters[6], colorCode, onLetterClicked = onClick,customSize = customSize)
            Spacer(modifier = Modifier.size(10.dp))
            CustomLetterClick(listOfLetters[7], colorCode, onLetterClicked = onClick,customSize = customSize)
            Spacer(modifier = Modifier.size(10.dp))
            CustomLetterClick(listOfLetters[8], colorCode, onLetterClicked = onClick,customSize = customSize)
            Spacer(modifier = Modifier.size(10.dp))
            CustomLetterClick(
                listOfLetters[2],
                image = R.drawable.backspace_24px,
                colorCode = colorCode,
                onLetterClicked = { onRemove() },
            )
        }
    }
}

@Composable
fun CustomLetterClick(
    letter: Char,
    colorCode: Int,
    boxColor: ColorPair = DataSource().colorPairs[colorCode],
    onLetterClicked: (String) -> Unit,
    image: Int? = null,
    thisText: String? = null,
    customSize: Dp = 60.dp
) {
    Box(modifier = Modifier.padding(top = 15.dp)) {
        KeyCards(
            thisLetter = letter,
            color = boxColor.darkColor,
            shadowColor = boxColor.darkColor.darken(),
            onClick = onLetterClicked,
            imageRes = image,
            thisText = thisText,
            customSize = customSize
        )
    }
}

@Composable
fun CustomButton(
    checkAnswer: () -> Unit,
    isTimerRunning: () -> Boolean,
    colorCode: Int,
    isDarkTheme: Boolean = isSystemInDarkTheme(),
) {
    val textColor = if (isDarkTheme) {
        DataSource().colorPairs[colorCode].darkColor.brighten()
    } else {
        DataSource().colorPairs[colorCode].darkColor
    }
    ElevatedButton(
        onClick = checkAnswer,
        colors = ButtonDefaults.elevatedButtonColors(
            containerColor = MaterialTheme.colorScheme.secondary
        ),
        contentPadding = PaddingValues(0.dp),
        enabled = isTimerRunning(),
        modifier = Modifier
            .padding(start = 10.dp, top = 5.dp, end = 5.dp)
            .width(70.dp)
            .height(50.dp)
    ) {
        Text(
            text = stringResource(R.string.check),
            maxLines = 1,
            color = textColor
        )
    }
}

@Composable
fun CustomTextField(
    textState: () -> String,
    onValueChange: (String) -> Unit,
    onDone: () -> Unit,
    isTimerRunning: () -> Boolean,
    modifier: Modifier,
    isChecked: Boolean
) {
    OutlinedTextField(
        value = textState(),
        singleLine = true,
        enabled = isTimerRunning(),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(
            onDone = { onDone() }
        ),
        modifier = modifier
            .padding(start = 20.dp, end = 5.dp),
        onValueChange = onValueChange,
        label = { Text("") },
        textStyle = TextStyle(
            textAlign = if (isChecked) TextAlign.Center else TextAlign.Start,
            fontFamily = FontFamily(Font(R.font.radiocanadabigregular)),
            fontSize = 26.sp
        ),
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
fun RowOfLetters(
    letter1: Char,
    letter2: Char,
    letter3: Char,
    colorCode: Int,
    isCorrect: () -> Int?
) {
    Row(
        modifier = Modifier.wrapContentSize()
    ) {
        LetterBox(letter1, isCorrect, colorCode)
        LetterBox(letter2, isCorrect, colorCode)
        LetterBox(letter3, isCorrect, colorCode)
    }
}


@Composable
fun TimerText(currentTime: () -> Long, modifier: Modifier, gameModeNumber: Int) {
    // Remember the score value to avoid unnecessary recompositions
    val formattedTime by remember(currentTime) {
        derivedStateOf {
            val time = currentTime()
            if (gameModeNumber != 1) { //meaning game mode with no time
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
        style = MaterialTheme.typography.titleSmall.copy(fontSize = 17.sp),
        modifier = modifier.width(25.dp)
    )
}

@Composable
fun TextScore(score: () -> Int, currentPassingScore: Int, isMode: Boolean) {
    // Remember the score value to avoid unnecessary recompositions
    val scoreValue by remember {
        derivedStateOf {
            score().toString()
        }
    }

    // Only recomposes if scoreValue changes
    Text(
        text = if (!isMode) {
            stringResource(R.string.score) + " " + scoreValue + " / " + currentPassingScore.toString()
        } else {
            stringResource(R.string.score) + " " + scoreValue
        },
        style = MaterialTheme.typography.titleSmall.copy(fontSize = 17.sp)
    )
}

@Composable
fun LetterBox(
    letter: Char,
    isCorrect: () -> Int?,
    colorCode: Int,
    boxColor: ColorPair = DataSource().colorPairs[colorCode],
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    darkCorrect: Color = Color(0xFF2AE45D),
    darkIncorrect: Color = Color(0xFFFF0000),
    lightCorrect: Color = Color(0xFF00c555),
    lightIncorrect: Color = Color(0xFFe80000),
    shadowLightDp: Dp = 13.dp,
    shadowDarkDp: Dp = 30.dp,
) {
    // Compute shadowDp based on the theme
    val shadowDp = if (isDarkTheme) shadowDarkDp else shadowLightDp
    val color = if (isDarkTheme) boxColor.darkColor else boxColor.lightColor
    // Determine shadowColor based on correctness and theme
    val shadowColor = remember(isCorrect(), isDarkTheme) {
        when {
            isCorrect() == 1 && isDarkTheme -> darkCorrect// Dark theme correct color
            isCorrect() == 1 && !isDarkTheme -> lightCorrect // Light theme correct color
            (isCorrect() == 2 || isCorrect() == 3) && isDarkTheme -> darkIncorrect// Dark theme incorrect color
            (isCorrect() == 2 || isCorrect() == 3) && !isDarkTheme -> lightIncorrect// Light theme incorrect color
            else -> boxColor.darkColor // Default color if isCorrect is null
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
            .graphicsLayer {//shadow
                shadowElevation = shadowDp.toPx()
                shape = RoundedCornerShape(15.dp)
                clip = true // Clip to the shape
                spotShadowColor = animatedColor // Apply animated color directly
            }
            .clip(MaterialTheme.shapes.medium)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = letter.toString(),
                style = MaterialTheme.typography.titleSmall.copy(fontSize = 20.sp)
            )
        }
    }
}

@Composable
fun DisplayResults(
    score: () -> Int,
    restart: (Long) -> Unit,
    time: () -> Long,
    navigateUp: () -> Unit,
    increaseScore: (Int) -> Unit,
    isMode: Boolean,
    highScore: Int,
    checkHighScore: (Int) -> Unit,
    isVisible: Boolean,
    restartGame: () -> Unit,
    exitPressed: () -> Unit,
    dateNow: String,
    dataDate: () -> String,
    increaseStreak: () -> Unit,
    colorCode: Int,
    boxColor: Color = DataSource().colorPairs[colorCode].darkColor,
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    launchChanging: () -> Unit,
    gameModeNumber: Int,
    currentLevel: Int,
    streakLevel: Int,
    characterIsFemale: Boolean,
    stopClockOnExit: () -> Unit,
    generateRandomLettersForBoth: () -> Unit,
    generateRandomLettersForMode: () -> Unit,
    calculatePassingScore: (Int) -> Int,
    generateRandomLettersForBothOnExit: () -> Unit,
    getFireImage: (Int) -> Int,
) {
    val context = LocalContext.current

    val coroutineScope = rememberCoroutineScope()
    var buttonsEnabled by remember { mutableStateOf(false) }
    val passed = score() >= calculatePassingScore(currentLevel)
    val textColor by remember {
        mutableStateOf(
            if (isDarkTheme) {
                Color.White
            } else {
                Color.Black
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
                if (passed && !isMode) {
                    increaseScore(score())
                    scheduleDailyNotification(context, streakLevel)
                    generateRandomLettersForBothOnExit()
                }
                stopClockOnExit()
            }

            Card(
                colors = CardDefaults.cardColors(
                    MaterialTheme.colorScheme.secondary
                )
            ) {
                if (score() > highScore && !isMode) {
                    ScoreContent(
                        imageId = if (characterIsFemale) R.drawable.female_half else R.drawable.male_half,
                        text = stringResource(R.string.new_high_score) + " " + score().toString(),
                        buttonText = stringResource(R.string.claim),
                        buttonAction = { checkHighScore(score()) },
                        buttonsEnabled = buttonsEnabled,
                        boxColor = if (buttonsEnabled) {
                            boxColor
                        } else {
                            Color.Gray
                        },
                        textColor = textColor,
                        size = 260.dp
                    )
                } else {
                    if (!isMode && (dateNow.substring(
                            0,
                            10
                        ) != dataDate()) && passed
                    ) {
                        ScoreContent(
                            imageId = getFireImage(streakLevel + 1),
                            text = stringResource(R.string.streak_increase),
                            buttonText = stringResource(R.string.claim),
                            buttonAction = { increaseStreak() },
                            buttonsEnabled = buttonsEnabled,
                            boxColor = if (buttonsEnabled) {
                                boxColor
                            } else {
                                Color.Gray
                            },
                            textColor = textColor,
                            size = 100.dp
                        )
                    } else {
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
                                        when {
                                            passed -> R.drawable.female_half
                                            !isMode -> R.drawable.female_half_sad_eyebags
                                            else -> R.drawable.female_half
                                        }
                                    } else {
                                        when {
                                            passed -> R.drawable.male_half
                                            !isMode -> R.drawable.male_half_sad_eyebags
                                            else -> R.drawable.male_half
                                        }
                                    }
                                ),
                                modifier = Modifier.size(260.dp),
                                contentDescription = null
                            )
                            Spacer(modifier = Modifier.padding(5.dp))
                            when {
                                passed && !isMode -> {
                                    Text(
                                        stringResource(R.string.pass) + " " + score().toString(),
                                        style = MaterialTheme.typography.titleSmall.copy(
                                            fontSize = 18.sp
                                        ),
                                        color = textColor
                                    )
                                }

                                !isMode -> {
                                    Text(
                                        stringResource(R.string.fail) + " " + score().toString(),
                                        style = MaterialTheme.typography.titleSmall.copy(
                                            fontSize = 18.sp
                                        ),
                                        color = textColor
                                    )
                                }

                                else -> {
                                    Text(
                                        stringResource(R.string.nice_try) + " " + score().toString(),
                                        style = MaterialTheme.typography.titleSmall.copy(
                                            fontSize = 18.sp
                                        ),
                                        color = textColor
                                    )
                                }
                            }

                            Row(
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                TextButton(
                                    onClick = {
                                        navigateUp()
                                        exitPressed()
                                        if (passed && !isMode) {
                                            increaseScore(score())
                                            scheduleDailyNotification(context, streakLevel)
                                            generateRandomLettersForBothOnExit()
                                        }
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
                                            boxColor
                                        } else {
                                            Color.Gray
                                        }
                                    )
                                }
                                if (isMode) {
                                    TextButton(
                                        onClick = {
                                            restartGame()
                                            coroutineScope.launch {
                                                generateRandomLettersForMode()
                                                restart(time())
                                            }
                                            if (gameModeNumber == 2) {
                                                launchChanging()
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
                                                boxColor
                                            } else {
                                                Color.Gray
                                            }
                                        )
                                    }
                                } else {
                                    TextButton(
                                        onClick = {
                                            restartGame()
                                            coroutineScope.launch {
                                                restart(time())
                                            }
                                        },
                                        enabled = buttonsEnabled
                                    ) {
                                        Text(
                                            stringResource(R.string.try_again),
                                            style = MaterialTheme.typography.titleSmall.copy(
                                                fontSize = 18.sp
                                            ),
                                            color = if (buttonsEnabled) {
                                                boxColor
                                            } else {
                                                Color.Gray
                                            }
                                        )
                                    }
                                    TextButton(
                                        onClick = {
                                            coroutineScope.launch {
                                                generateRandomLettersForBoth()
                                                restart(time())
                                            }
                                            increaseScore(score())
                                            restartGame()
                                            scheduleDailyNotification(context, streakLevel)
                                        },
                                        enabled = passed && buttonsEnabled
                                    ) {
                                        Text(
                                            stringResource(R.string.next_level),
                                            style = MaterialTheme.typography.titleSmall.copy(
                                                fontSize = 18.sp
                                            ),
                                            color = if (buttonsEnabled && passed
                                            ) {
                                                boxColor
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
        }
    }
}

@Composable
fun ScoreContent(
    imageId: Int,
    text: String,
    buttonText: String,
    buttonAction: () -> Unit,
    buttonsEnabled: Boolean,
    boxColor: Color,
    textColor: Color,
    size: Dp
) {
    Column(
        modifier = Modifier
            .padding(10.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = imageId),
            modifier = Modifier.size(size),
            contentDescription = null
        )
        Spacer(modifier = Modifier.padding(5.dp))
        Text(
            text,
            style = MaterialTheme.typography.titleSmall.copy(fontSize = 18.sp),
            color = textColor
        )
        TextButton(
            onClick = buttonAction,
            enabled = buttonsEnabled
        ) {
            Text(
                buttonText,
                style = MaterialTheme.typography.titleSmall.copy(fontSize = 18.sp),
                color = if (buttonsEnabled) boxColor else Color.Gray
            )
        }
    }
}

@Composable
fun Timer(
    value: () -> Float,
    colorCode: Int,
    modifier: Modifier = Modifier,
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    strokeWidth: Dp = 10.dp
) {
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
                color = DataSource().colorPairs[colorCode].darkColor,
                start = Offset(center.x - lineLength / 2, center.y),
                end = Offset(center.x - lineLength / 2 + lineLength * currentValue, center.y),
                strokeWidth = strokeWidth.toPx(),
                cap = StrokeCap.Round
            )
        }
    }
}

@Composable
fun TimerCircular(
    value: () -> Float,
    currentTime: () -> Long,
    activeBarColor: Color,
    modifier: Modifier = Modifier,
    strokeWidth: Dp = 10.dp,
    isDarkTheme: Boolean = isSystemInDarkTheme(),
) {
    val inactiveBarColor = if (isDarkTheme) {
        Color.Gray.darken()
    } else {
        Color.White.darken()
    }
    var size by remember {
        mutableStateOf(IntSize.Zero)
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .onSizeChanged {
                size = it
            }
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawArc(
                color = inactiveBarColor,
                startAngle = -215f,
                sweepAngle = 250f,
                useCenter = false,
                size = Size(size.width.toFloat(), size.height.toFloat()),
                style = Stroke(strokeWidth.toPx(), cap = StrokeCap.Round)
            )
            drawArc(
                color = activeBarColor,
                startAngle = -215f,
                sweepAngle = 250f * value(),
                useCenter = false,
                size = Size(size.width.toFloat(), size.height.toFloat()),
                style = Stroke(strokeWidth.toPx(), cap = StrokeCap.Round)
            )
        }
        CanvasText(currentTime)
    }
}

@Composable
fun CanvasText(currentTime: () -> Long) {
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
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.titleSmall.copy(fontSize = 45.sp)
    )
}