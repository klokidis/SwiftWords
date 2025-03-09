package com.yukuro.swiftwords.ui

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.yukuro.swiftwords.R
import com.yukuro.swiftwords.data.ColorPair
import com.yukuro.swiftwords.data.DataSource


@Composable
fun ModesScreen(
    navigateFastGame: () -> Unit,
    navigateLongGame: () -> Unit,
    navigateChangingGame: () -> Unit,
    color: Int?,
    navigateConsequencesGame: () -> Unit,
    navigateCombatGame: () -> Unit,
    colorCodePlayerOne: Int,
    colorCodePlayerTwo: Int,
    changeColorPlayerCombat: (Int, Int) -> Unit,
    isDarkTheme: Boolean = isSystemInDarkTheme(),
) {
    val scrollState = rememberScrollState()
    var visible by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .padding(start = 7.dp, end = 7.dp),
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
                    textRes = R.string.effect,
                    onClick = { navigateConsequencesGame() },
                    color = color,
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 5.dp) // Ensures the button fills available space
                )
            }
            BottomCard(color,{ visible = true })
        }
    }
    PopUp(
        visible,
        DataSource().colorPairs[color!!].darkColor,
        navigateCombatGame,
        { visible = false },
        colorThemeCode = color,
        colorCodePlayerOne = colorCodePlayerOne,
        colorCodePlayerTwo = colorCodePlayerTwo,
        changeColorPlayerCombat = changeColorPlayerCombat,
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
            imageRes = if (isDarkTheme) R.drawable.black_sword else R.drawable.white_sword,
            textRes = R.string.combat,
            color = DataSource().colorPairs[color!!].darkColor,
            shadowColor = DataSource().colorPairs[color].darkColor.darken(),
            onClick = function,
            size = 130.dp
        )
    }
}

@Composable
fun PopUp(
    visible: Boolean,
    colorTheme: Color,
    navigate: () -> Unit,
    hide: () -> Unit,
    listOfColorsOne: List<ColorPair> = DataSource().colorPairs.subList(0, 7),
    listOfColorsTwo: List<ColorPair> = DataSource().colorPairs.subList(7, 13),
    colorCodePlayerOne: Int,
    colorCodePlayerTwo: Int,
    changeColorPlayerCombat: (Int, Int) -> Unit,
    colorThemeCode: Int,
) {
    var selectedOneVisible by remember { mutableStateOf(false) }
    var selectedTwoVisible by remember { mutableStateOf(false) }
    var selectedColorOne by remember { mutableStateOf<Int?>(null) }
    var selectedColorTwo by remember { mutableStateOf<Int?>(null) }

    val filteredColorsOne by remember(listOfColorsOne, colorThemeCode, selectedColorTwo) {
        derivedStateOf {
            listOfColorsOne.filter { it.id != colorThemeCode && it.id != selectedColorTwo && it.id != selectedColorOne }
        }
    }

    val filteredColorsTwo by remember(listOfColorsTwo, colorThemeCode, selectedColorOne) {
        derivedStateOf {
            listOfColorsTwo.filter { it.id != colorThemeCode && it.id != selectedColorOne && it.id != selectedColorTwo }
        }
    }

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
                        imageVector = ImageVector.vectorResource(id = R.drawable.swords_24px),
                        modifier = Modifier.size(100.dp),
                        contentDescription = null,
                        tint = colorTheme
                    )
                    Row(
                        modifier = Modifier.padding(top = 15.dp)
                    ) {
                        // Dropdown Menu for Time Selection
                        Box {
                            TextButton(onClick = { selectedOneVisible = true }) {
                                if (selectedColorOne != null) {
                                    ColorRound(colorCodePlayerOne)
                                } else {
                                    Text(
                                        text = stringResource(R.string.player_one),
                                        style = MaterialTheme.typography.titleSmall.copy(fontSize = 18.sp),
                                        color = colorTheme
                                    )
                                }
                            }
                            DropdownMenu(
                                expanded = selectedOneVisible,
                                modifier = Modifier.width(100.dp),
                                onDismissRequest = { selectedOneVisible = false }
                            ) {
                                Row {
                                    Column(modifier = Modifier.width(50.dp)) {
                                        filteredColorsOne.forEach { colorPair ->
                                            DropdownMenuItem(
                                                onClick = {
                                                    changeColorPlayerCombat(colorPair.id, 1)
                                                    selectedColorOne = colorPair.id
                                                    selectedOneVisible = false
                                                },
                                                text = {
                                                    ColorRound(colorPair.id)
                                                },
                                            )
                                        }
                                    }
                                    Column(modifier = Modifier.width(50.dp)) {
                                        filteredColorsTwo.forEach { colorPair ->
                                            DropdownMenuItem(
                                                onClick = {
                                                    changeColorPlayerCombat(colorPair.id, 1)
                                                    selectedColorOne = colorPair.id
                                                    selectedOneVisible = false
                                                },
                                                text = {
                                                    ColorRound(colorPair.id)
                                                },
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        Box {
                            TextButton(onClick = { selectedTwoVisible = true }) {
                                if (selectedColorTwo != null) {
                                    ColorRound(colorCodePlayerTwo)
                                } else {
                                    Text(
                                        text = stringResource(R.string.player_two),
                                        style = MaterialTheme.typography.titleSmall.copy(fontSize = 18.sp),
                                        color = colorTheme
                                    )
                                }
                            }
                            DropdownMenu(
                                expanded = selectedTwoVisible,
                                modifier = Modifier.width(100.dp),
                                onDismissRequest = { selectedTwoVisible = false }
                            ) {
                                Row {
                                    Column(modifier = Modifier.width(50.dp)) {
                                        filteredColorsOne.forEach { colorPair ->
                                            DropdownMenuItem(
                                                onClick = {
                                                    changeColorPlayerCombat(colorPair.id, 2)
                                                    selectedColorTwo = colorPair.id
                                                    selectedTwoVisible = false
                                                },
                                                text = {
                                                    ColorRound(colorPair.id)
                                                },
                                            )
                                        }
                                    }
                                    Column(modifier = Modifier.width(50.dp)) {
                                        filteredColorsTwo.forEach { colorPair ->
                                            DropdownMenuItem(
                                                onClick = {
                                                    changeColorPlayerCombat(colorPair.id, 2)
                                                    selectedColorTwo = colorPair.id
                                                    selectedTwoVisible = false
                                                },
                                                text = {
                                                    ColorRound(colorPair.id)
                                                },
                                            )
                                        }
                                    }
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
                                hide()
                                navigate()
                            },
                            enabled = selectedColorOne != null && selectedColorTwo != null
                        ) {
                            Text(
                                stringResource(R.string.confirm),
                                style = MaterialTheme.typography.titleSmall.copy(fontSize = 18.sp),
                                color = if (selectedColorOne == null || selectedColorTwo == null) Color.Gray else colorTheme
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ColorRound(
    colorCodePlayerOne: Int,
    listOfColors: List<ColorPair> = DataSource().colorPairs,
) {
    Box(
        modifier = Modifier
            .size(26.dp)
            .clip(RoundedCornerShape(26.dp))
            .background(listOfColors[colorCodePlayerOne].darkColor)
    )
}