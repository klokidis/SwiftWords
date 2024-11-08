package com.example.swiftwords.ui.modes

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.swiftwords.R
import com.example.swiftwords.data.DataSource
import com.example.swiftwords.ui.elements.ModesCards
import com.example.swiftwords.ui.elements.darken
import kotlin.reflect.KFunction0
import kotlin.reflect.KFunction1
import kotlin.reflect.KFunction3


@Composable
fun ModesScreen(
    navigateFastGame: () -> Unit,
    navigateLongGame: () -> Unit,
    navigateChangingGame: () -> Unit,
    color: Int?,
    navigateConsequencesGame: () -> Unit,
    navigateCustomGame: () -> Unit,
    changeTime: KFunction1<Long, Unit>,
    changeGameMode: KFunction1<Int, Unit>,
    startShuffle: KFunction3<Boolean, () -> Unit, Long, Unit>,
    sound: KFunction0<Unit>,
    isDarkTheme: Boolean = isSystemInDarkTheme(),
) {
    val scrollState = rememberScrollState()
    var visible by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(start = 7.dp, end = 7.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(modifier = Modifier.fillMaxSize()) {
                ModeCard(
                    imageRes = if (isDarkTheme) R.drawable.black_light else R.drawable.white_light,
                    textRes = R.string.fast,
                    onClick = { navigateFastGame() },
                    color = color,
                    modifier = Modifier.weight(1f) // Ensures the button fills available space
                )
                ModeCard(
                    imageRes = if (isDarkTheme) R.drawable.black_unl else R.drawable.white_unl,
                    textRes = R.string.unlimited,
                    onClick = { navigateLongGame() },
                    color = color,
                    modifier = Modifier.weight(1f) // Ensures the button fills available space
                )
            }
            Row(modifier = Modifier.fillMaxSize()) {
                ModeCard(
                    imageRes = if (isDarkTheme) R.drawable.black_shuffle else R.drawable.white_shuffle,
                    textRes = R.string.shuffle,
                    onClick = { navigateChangingGame() },
                    color = color,
                    modifier = Modifier.weight(1f) // Ensures the button fills available space
                )
                ModeCard(
                    imageRes = if (isDarkTheme) R.drawable.black_con else R.drawable.white_con,
                    textRes = R.string.consequences,
                    onClick = { navigateConsequencesGame() },
                    color = color,
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 5.dp) // Ensures the button fills available space
                )
            }
            BottomCard(color, { visible = true })
        }
    }
    PopUp(
        visible,
        DataSource().colorPairs[color!!].darkColor,
        navigateCustomGame,
        { visible = false },
        changeTime,
        changeGameMode,
        startShuffle,
        sound
    )
}

@Composable
fun ModeCard(
    imageRes: Int,
    textRes: Int,
    onClick: () -> Unit,
    color: Int?,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.padding(start = 7.dp, end = 7.dp, top = 18.dp)) {
        ModesCards(
            imageRes = imageRes,
            textRes = textRes,
            color = DataSource().colorPairs[color!!].darkColor,
            shadowColor = DataSource().colorPairs[color].darkColor.darken(),
            onClick = onClick
        )
    }
}

@Composable
fun BottomCard(
    color: Int?,
    function: () -> Unit,
    isDarkTheme: Boolean = isSystemInDarkTheme(),
) {
    Box(modifier = Modifier.padding(start = 7.dp, end = 7.dp, top = 18.dp, bottom = 10.dp)) {
        ModesCards(
            imageRes = if (isDarkTheme) R.drawable.black_custom else R.drawable.white_custom,
            textRes = R.string.custom,
            color = DataSource().colorPairs[color!!].darkColor,
            shadowColor = DataSource().colorPairs[color].darkColor.darken(),
            onClick = function,
            size = 150.dp
        )
    }
}

@Composable
fun PopUp(
    visible: Boolean,
    colorTheme: Color,
    navigate: () -> Unit,
    hide: () -> Unit,
    changeTime: (Long) -> Unit,
    changeGameMode: (Int) -> Unit,
    startShuffle: KFunction3<Boolean, () -> Unit, Long, Unit>,
    sound: () -> Unit,
    modeStringOptions: List<String> = listOf(
        stringResource(R.string.shuffle),
        stringResource(R.string.consequences)
    ),
    modeOptions: List<Int> = listOf(0, 1),
    timeOptions1: List<Long> =
        listOf(20000L, 30000L, 40000L),
    timeOptions2: List<Long> =
        listOf(50000L, 60000L, 70000L),
) {
    var expandedTime by remember { mutableStateOf(false) }
    var expandedMode by remember { mutableStateOf(false) }
    var selectedTime by remember { mutableStateOf<Long?>(null) }
    var selectedModeVisible by remember { mutableStateOf<String?>(null) }
    var selectedMode by remember { mutableStateOf<Int?>(null) }


    if (visible) {
        Dialog(onDismissRequest = hide) {
            Card(
                colors = CardDefaults.cardColors(MaterialTheme.colorScheme.secondary)
            ) {
                Column(
                    modifier = Modifier
                        .padding(30.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.black_custom),
                        modifier = Modifier.size(100.dp),
                        contentDescription = null,
                        tint = colorTheme
                    )
                    Row(
                        modifier = Modifier.padding(top = 15.dp)
                    ) {
                        // Dropdown Menu for Time Selection
                        Box {
                            TextButton(onClick = { expandedTime = true }) {
                                Text(
                                    text = selectedTime?.toString()?.take(2)
                                        ?: stringResource(R.string.select_time),
                                    style = MaterialTheme.typography.titleSmall.copy(fontSize = 18.sp),
                                    color = colorTheme
                                )
                            }
                            DropdownMenu(
                                expanded = expandedTime,
                                modifier = Modifier.width(100.dp),
                                onDismissRequest = { expandedTime = false }
                            ) {
                                Row{
                                    Column(modifier = Modifier.width(50.dp)) {
                                        timeOptions1.forEach { time ->
                                            val firstTwoDigits = time.toString().take(2)
                                            DropdownMenuItem(
                                                onClick = {
                                                    selectedTime = time
                                                    expandedTime = false
                                                },
                                                text = {
                                                    Text(
                                                        firstTwoDigits,
                                                        style = MaterialTheme.typography.titleSmall.copy(
                                                            fontSize = 18.sp
                                                        ),
                                                    )
                                                }
                                            )
                                        }
                                    }
                                    Column(modifier = Modifier.width(50.dp)) {
                                        timeOptions2.forEach { time ->
                                            val firstTwoDigits = time.toString().take(2)
                                            DropdownMenuItem(
                                                onClick = {
                                                    selectedTime = time
                                                    expandedTime = false
                                                },
                                                text = {
                                                    Text(
                                                        firstTwoDigits,
                                                        style = MaterialTheme.typography.titleSmall.copy(
                                                            fontSize = 18.sp
                                                        ),
                                                    )
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        Box {
                            TextButton(onClick = { expandedMode = true }) {
                                Text(
                                    text = selectedModeVisible
                                        ?: stringResource(R.string.select_mode),
                                    style = MaterialTheme.typography.titleSmall.copy(fontSize = 18.sp),
                                    color = colorTheme
                                )
                            }
                            DropdownMenu(
                                expanded = expandedMode,
                                onDismissRequest = { expandedMode = false }
                            ) {
                                modeOptions.forEach { mode ->
                                    DropdownMenuItem(
                                        onClick = {
                                            selectedModeVisible = modeStringOptions[mode]
                                            selectedMode = mode + 2
                                            expandedMode = false
                                        },
                                        text = {
                                            Text(
                                                modeStringOptions[mode],
                                                style = MaterialTheme.typography.titleSmall.copy(
                                                    fontSize = 18.sp
                                                ),
                                            )
                                        }
                                    )
                                }
                            }
                        }
                    }
                    Row {
                        TextButton(
                            onClick = { hide() }
                        ) {
                            Text(
                                stringResource(R.string.cancel),
                                style = MaterialTheme.typography.titleSmall.copy(fontSize = 18.sp),
                                color = colorTheme
                            )
                        }
                        TextButton(
                            onClick = {
                                changeTime(selectedTime!!)
                                selectedMode?.let { changeGameMode(it) }
                                hide()
                                if (selectedMode == 2) startShuffle(true, sound, selectedTime!!)
                                navigate()
                            },
                            enabled = selectedTime != null && selectedMode != null
                        ) {
                            Text(
                                stringResource(R.string.confirm),
                                style = MaterialTheme.typography.titleSmall.copy(fontSize = 18.sp),
                                color = if (selectedTime == null || selectedMode == null) Color.Gray else colorTheme
                            )
                        }
                    }
                }
            }
        }
    }
}
