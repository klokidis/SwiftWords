package com.example.swiftwords.ui.game

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextButton
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onSizeChanged
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.swiftwords.R
import com.example.swiftwords.data.ColorPair
import com.example.swiftwords.data.DataSource
import com.example.swiftwords.ui.AppViewModelProvider
import com.example.swiftwords.ui.SwiftWordsMainViewModel
import com.example.swiftwords.ui.elements.KeyCards
import com.example.swiftwords.ui.elements.SoundViewModel
import com.example.swiftwords.ui.elements.brighten
import com.example.swiftwords.ui.elements.darken
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.reflect.KFunction0
import kotlin.reflect.KFunction1
import kotlin.reflect.KSuspendFunction1

@Composable
fun Game(
    dateNow: String,
    dataDate: () -> String,
    newTime: () -> Long,
    wordList: Set<String>,
    colorCode: Int,
    isMode: Boolean,
    increaseScore: KFunction1<Int, Unit>,
    viewModel: GameViewModel = viewModel(factory = GameViewModelFactory(newTime)),
    navigateUp: () -> Unit,
    mainViewModel: SwiftWordsMainViewModel,
    checkHighScore: KSuspendFunction1<Int, Unit>,
    setOfLetters: Set<Char>,
    highScore: Int,
    checked: () -> Boolean,
    changeChecked: KFunction0<Unit>,
    increaseStreak: KFunction0<Unit>,
) {
    val gameUiState by viewModel.uiState.collectAsState()
    val isTimerRunning by remember { derivedStateOf { gameUiState.isTimerRunning } }
    val coroutineScope = rememberCoroutineScope()
    var textState by rememberSaveable { mutableStateOf("") }
    var isCorrect by rememberSaveable { mutableStateOf<Boolean?>(null) }
    var isLoading by rememberSaveable { mutableStateOf(false) }
    var onExitButtonPressed by rememberSaveable { mutableStateOf(false) }
    var lastMessage by rememberSaveable { mutableStateOf("Please enter an answer.") }
    val scroll = rememberScrollState()
    val soundViewModel: SoundViewModel = viewModel(factory = AppViewModelProvider.Factory)
    var listOfLetters by rememberSaveable { mutableStateOf(setOfLetters.toList()) } // Mutable state for shuffled list
    val checkAnswer: () -> Unit = remember(setOfLetters) { // remember so it doesn't composition
        {
            isLoading = true
            coroutineScope.launch {
                isCorrect = viewModel.checkAnswer(
                    { textState },
                    wordList,
                    setOfLetters
                )
                isLoading = false
                textState = ""  // Reset textState after isCorrect is updated
                when (isCorrect) {
                    true -> soundViewModel.playCorrectSound()
                    else -> soundViewModel.playIncorrectSound()
                }
            }
        }
    }
    DisposableEffect(Unit) { //update level even if user exist since he passed
        onDispose {
            if (!onExitButtonPressed && gameUiState.score >= 1 && !isMode) {
                increaseScore(gameUiState.score)
                mainViewModel.generateRandomLettersForBoth()
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
                    modifier = Modifier
                        .size(33.dp)
                        .padding(start = 7.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.exit),
                        modifier = Modifier.size(33.dp)
                    )
                }
                // most values pass as () -> type to fix the unnecessary recomposition of the ui
                if (!checked()) {
                    Timer(
                        { gameUiState.value },
                        colorCode,
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                    )
                    TimerText(
                        modifier = Modifier.weight(1f),
                        currentTime = { gameUiState.currentTime })
                } else {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
            if (!checked()) {
                Spacer(modifier = Modifier.padding(10.dp))
            } else {
                Spacer(modifier = Modifier.weight(1f))
            }
            if (!checked()) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    RowOfLetters(
                        listOfLetters[0],
                        listOfLetters[1],
                        listOfLetters[2],
                        colorCode
                    ) { isCorrect }
                    RowOfLetters(
                        listOfLetters[3],
                        listOfLetters[4],
                        listOfLetters[5],
                        colorCode
                    ) { isCorrect }
                    RowOfLetters(
                        listOfLetters[6],
                        listOfLetters[7],
                        listOfLetters[8],
                        colorCode
                    ) { isCorrect }
                }
            } else {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxSize(1f)
                        .padding(bottom = 5.dp)
                ) {
                    TimerCircular(
                        value = { gameUiState.value },
                        currentTime = { gameUiState.currentTime },
                        activeBarColor = DataSource().colorPairs[colorCode].darkColor,
                        modifier = Modifier.size(230.dp)
                    )
                }
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
                            lastMessage = "Correct"
                            lastMessage
                        }

                        isCorrect == false -> {
                            lastMessage = "Incorrect"
                            lastMessage
                        }

                        else -> {
                            lastMessage = "Enter an answer."
                            lastMessage
                        }
                    },
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontSize = 16.sp,
                        color = when (isCorrect) {
                            true -> Color(0xFF006D2F)  // Correct color
                            false -> Color(0xFF8D0C0C) // Incorrect color
                            else -> Color.Unspecified  // Default color when isCorrect is null
                        }
                    )
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
            if (checked() && isTimerRunning) {
                CustomKeyboard(
                    listOfLetters = listOfLetters,
                    colorCode,
                    onClick = { newText ->
                        textState =
                            newText.toString()
                    },
                    word = textState,
                    onEnter = checkAnswer,
                    onRemove = { textState = textState.dropLast(1) }
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Row(
                modifier = Modifier
                    .padding(start = 3.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center

            ) {
                Checkbox(
                    checked = checked(),
                    onCheckedChange = {
                        changeChecked()
                    },
                    colors = CheckboxDefaults.colors(
                        checkedColor = DataSource().colorPairs[colorCode].darkColor,
                    )
                )
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.keyboard_24px),
                    contentDescription = "keyboard",
                )
                Spacer(modifier = Modifier.weight(1f))
                IconButton(
                    onClick = {
                        listOfLetters = listOfLetters.shuffled()
                    },
                    modifier = Modifier
                        .size(33.dp)
                        .padding(end = 7.dp)
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.shuffle_24px),
                        contentDescription = stringResource(R.string.shuffle)
                    )

                }
            }
        }
    }
    DisplayResults(
        { gameUiState.score },
        viewModel::restartGame,
        newTime,
        navigateUp,
        increaseScore,
        mainViewModel,
        isMode,
        highScore,
        checkHighScore,
        isVisible = !isTimerRunning,
        restartGame = {
            textState = ""
            isCorrect = null
        },
        exitPressed = { onExitButtonPressed = true },
        dateNow = dateNow,
        dataDate = dataDate,
        increaseStreak = increaseStreak
    )
}

@Composable
fun CustomKeyboard(
    listOfLetters: List<Char>,
    colorCode: Int,
    onClick: (Any?) -> Unit,
    word: String,
    onEnter: () -> Unit,
    onRemove: () -> Unit
) {
    Column(
        modifier = Modifier.padding(top = 5.dp, bottom = 50.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row {
            CustomLetterClick(listOfLetters[0], colorCode, onLetterClicked = onClick, word = word)
            Spacer(modifier = Modifier.size(10.dp))
            CustomLetterClick(listOfLetters[1], colorCode, onLetterClicked = onClick, word = word)
            Spacer(modifier = Modifier.size(10.dp))
            CustomLetterClick(listOfLetters[2], colorCode, onLetterClicked = onClick, word = word)
            Spacer(modifier = Modifier.size(10.dp))
            CustomLetterClick(
                thisText = "ENTER",
                letter = ' ',
                colorCode = colorCode,
                onLetterClicked = { onEnter() },
                word = word
            )
        }
        Row {
            CustomLetterClick(listOfLetters[3], colorCode, onLetterClicked = onClick, word = word)
            Spacer(modifier = Modifier.size(10.dp))
            CustomLetterClick(listOfLetters[4], colorCode, onLetterClicked = onClick, word = word)
            Spacer(modifier = Modifier.size(10.dp))
            CustomLetterClick(listOfLetters[5], colorCode, onLetterClicked = onClick, word = word)
        }
        Row {
            CustomLetterClick(listOfLetters[6], colorCode, onLetterClicked = onClick, word = word)
            Spacer(modifier = Modifier.size(10.dp))
            CustomLetterClick(listOfLetters[7], colorCode, onLetterClicked = onClick, word = word)
            Spacer(modifier = Modifier.size(10.dp))
            CustomLetterClick(listOfLetters[8], colorCode, onLetterClicked = onClick, word = word)
            Spacer(modifier = Modifier.size(10.dp))
            CustomLetterClick(
                listOfLetters[2],
                image = R.drawable.backspace_24px,
                colorCode = colorCode,
                onLetterClicked = { onRemove() },
                word = word
            )
        }
    }
}

@Composable
fun CustomLetterClick(
    letter: Char,
    colorCode: Int,
    boxColor: ColorPair = DataSource().colorPairs[colorCode],
    onLetterClicked: (Any?) -> Unit,
    image: Int? = null,
    thisText: String? = null,
    word: String
) {
    Box(modifier = Modifier.padding(top = 15.dp)) {
        KeyCards(
            thisLetter = letter,
            color = boxColor.darkColor,
            shadowColor = boxColor.darkColor.darken(),
            onClick = onLetterClicked,
            imageRes = image,
            thisText = thisText,
            thisWord = word
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
            text = "check",
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
            fontSize = 23.sp
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
    isCorrect: () -> Boolean?
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
fun TimerText(currentTime: () -> Long, modifier: Modifier) {
    // Remember the score value to avoid unnecessary recompositions
    val formattedTime by remember(currentTime) {
        derivedStateOf {
            val time = currentTime()
            if (time != 130000000L) { //meaning game mode with no time
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
        style = MaterialTheme.typography.titleSmall.copy(fontSize = 17.sp)
    )
}

@Composable
fun LetterBox(
    letter: Char,
    isCorrect: () -> Boolean?,
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
            isCorrect() == true && isDarkTheme -> darkCorrect// Dark theme correct color
            isCorrect() == true && !isDarkTheme -> lightCorrect // Light theme correct color
            isCorrect() == false && isDarkTheme -> darkIncorrect// Dark theme incorrect color
            isCorrect() == false && !isDarkTheme -> lightIncorrect// Light theme incorrect color
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
                .background(color),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = letter.toString(),
                style = MaterialTheme.typography.titleSmall.copy(fontSize = 18.sp)
            )
        }
    }
}

@Composable
fun DisplayResults(
    score: () -> Int,
    restart: KSuspendFunction1<Long, Unit>,
    time: () -> Long,
    navigateUp: () -> Unit,
    increaseScore: KFunction1<Int, Unit>,
    viewModel: SwiftWordsMainViewModel,
    isMode: Boolean,
    highScore: Int,
    checkHighScore: suspend (Int) -> Unit,
    isVisible: Boolean,
    restartGame: () -> Unit,
    exitPressed: () -> Unit,
    dateNow: String,
    dataDate: () -> String,
    increaseStreak: KFunction0<Unit>,
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(
            // Overwrites the initial value of alpha to 0.2f for fade in, 0 by default
            initialAlpha = 0.2f
        ),
        exit = fadeOut(
            // Overwrites the default animation with tween
            animationSpec = tween(durationMillis = 200)
        )
    ) {
        val coroutineScope = rememberCoroutineScope()
        var buttonsEnabled by remember { mutableStateOf(false) }

        // Launch a coroutine to enable the buttons after a delay
        LaunchedEffect(Unit) {
            delay(800L)
            buttonsEnabled = true
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.4f)),
        ) {
            Card(
                modifier = Modifier
                    .align(Alignment.Center),
            ) {
                if (score() > highScore && !isMode) {
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
                        Text(
                            "New high score: " + score().toString(),
                            style = MaterialTheme.typography.titleSmall
                        )
                        TextButton(
                            onClick = {
                                coroutineScope.launch {
                                    checkHighScore(score())
                                }
                            },
                            enabled = buttonsEnabled
                        ) {
                            Text("Claim")
                        }
                    }
                } else {
                    if (!isMode && (dateNow.substring(0, 10) != dataDate()) && score() >= 1) {
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
                            Text(
                                "good job streak increased",
                                style = MaterialTheme.typography.titleSmall
                            )
                            TextButton(
                                onClick = {
                                    increaseStreak()
                                },
                                enabled = buttonsEnabled
                            ) {
                                Text("Claim")
                            }
                        }
                    } else {
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
                            when {
                                score() >= 10 && !isMode -> {
                                    Text(
                                        "Congrats!! you passed with score: " + score().toString(),
                                        style = MaterialTheme.typography.titleSmall
                                    )
                                }

                                !isMode -> {
                                    Text(
                                        "you failed :( with score: " + score().toString(),
                                        style = MaterialTheme.typography.titleSmall
                                    )
                                }

                                else -> {
                                    Text(
                                        "nice try!! with score: " + score().toString(),
                                        style = MaterialTheme.typography.titleSmall
                                    )
                                }
                            }

                            Row(
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                TextButton(
                                    onClick = {
                                        exitPressed()
                                        if (score() >= 1 && !isMode) {
                                            increaseScore(score())
                                            viewModel.generateRandomLettersForBoth()
                                        }
                                        navigateUp()
                                    },
                                    enabled = buttonsEnabled
                                ) {
                                    Text("Exit")
                                }
                                if (isMode) {
                                    TextButton(
                                        onClick = {
                                            restartGame()
                                            coroutineScope.launch {
                                                viewModel.generateRandomLettersForBoth()
                                                restart(time())
                                            }
                                        },
                                        enabled = buttonsEnabled
                                    ) {
                                        Text("Play Again")
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
                                        Text("Try Again (-1 life)")
                                    }
                                    TextButton(
                                        onClick = {
                                            increaseScore(score())
                                            restartGame()
                                            coroutineScope.launch {
                                                viewModel.generateRandomLettersForBoth()
                                                restart(time())
                                            }
                                        },
                                        enabled = score() >= 0 && buttonsEnabled
                                    ) {
                                        Text("Next Level")
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
        style = MaterialTheme.typography.titleSmall.copy(fontSize = 43.sp)
    )
}