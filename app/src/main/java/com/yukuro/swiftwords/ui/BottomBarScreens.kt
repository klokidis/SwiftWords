package com.yukuro.swiftwords.ui

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.yukuro.swiftwords.R
import com.yukuro.swiftwords.data.DataSource
import com.yukuro.swiftwords.model.BarItem
import com.yukuro.swiftwords.viewmodels.MainUiState

enum class BottomBarScreensNames {
    Levels,
    Modes,
    Profile,
}

@Composable
fun BottomBarNavGraph(
    navController: NavHostController = rememberNavController(),
    mainUiState: MainUiState,
    navigateGame: () -> Unit,
    navigateSettings: () -> Unit,
    streak: Int,
    streakDateData: String,
    color: Int,
    levelTime: Long,
    currentLevel: Int,
    starterLevel: Int,
    endingLevel: Int,
    highScore: Int,
    nickname: String,
    character: Boolean,
    profileSelected: Int,
    changeGameState: (Boolean) -> Unit,
    changeTime: (Long) -> Unit,
    changeGameMode: (Int) -> Unit,
    generateRandomLettersForMode: () -> Unit,
    changingLetters: (Boolean, () -> Unit, Long) -> Unit,
    updateUserColor: (Int) -> Unit,
    updateTime: (Long) -> Unit,
    changeProfilePic: (Int) -> Unit,
    playChangeSound: () -> Unit,
    colorCodePlayerOne: Int,
    colorCodePlayerTwo: Int,
    changeColorPlayerCombat: (Int, Int) -> Unit,
    navigateGameCombat: () -> Unit
) {
    var selectedItemIndex by rememberSaveable { mutableIntStateOf(0) }

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = BottomBarScreensNames.valueOf(
        backStackEntry?.destination?.route ?: BottomBarScreensNames.Levels.name
    )

    LaunchedEffect(currentScreen) {
        val newIndex = when (currentScreen) {
            BottomBarScreensNames.Levels -> 0
            BottomBarScreensNames.Modes -> 1
            BottomBarScreensNames.Profile -> 2
        }

        if (selectedItemIndex != newIndex) { //for navigating back with back phone arrow
            selectedItemIndex = newIndex
        }
    }

    Scaffold(
        bottomBar = {
            val barItems = listOf(
                BarItem(
                    R.string.levels,
                    R.drawable.levels,
                    R.drawable.levels
                ),
                BarItem(
                    R.string.modes,
                    R.drawable.controller_filled,
                    R.drawable.controller
                ),
                BarItem(
                    R.string.profile,
                    R.drawable.profilr_filled,
                    R.drawable.profile
                )
            )
            // Wrapper for the NavigationBar to add top line and shadow
            Column {
                Spacer(
                    modifier = Modifier
                        .background(
                            if (isSystemInDarkTheme()) {
                                Color.DarkGray.darken(0.6f)
                            } else {
                                Color(0xFFE8E8E8)
                            }
                        )
                        .fillMaxWidth()
                        .height(1.5.dp)
                )
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.background, // Override the background color
                ) {
                    barItems.forEachIndexed { index, item ->
                        val isSelected = selectedItemIndex == index
                        NavigationBarItem(
                            onClick = {
                                if (!isSelected) {
                                    selectedItemIndex = index
                                    navController.navigateToScreen(selectedItemIndex)
                                }
                            },
                            selected = isSelected,
                            label = { Text(text = stringResource(item.title)) },
                            icon = {
                                Icon(
                                    imageVector = if (index == selectedItemIndex) ImageVector.vectorResource(
                                        id = item.imageSelected
                                    ) else ImageVector.vectorResource(id = item.imageUnSelected),
                                    contentDescription = stringResource(id = item.title)
                                )
                            }
                        )
                    }
                }
            }
        },
    ) { padding ->
        NavHost(
            navController = navController,
            modifier = Modifier.padding(padding),
            startDestination = BottomBarScreensNames.Levels.name,
            enterTransition = { fadeIn(animationSpec = tween(0)) },
            exitTransition = { fadeOut(animationSpec = tween(0)) }
        ) {
            composable(route = BottomBarScreensNames.Levels.name) {
                Scaffold(topBar = {
                    TopBar(
                        //livesLeft = it.lives, maybe in the future
                        streak = streak,
                        streakDateData = streakDateData,
                        dateNow = mainUiState.todayDate,
                        color = color,
                        changeColorFun = updateUserColor,
                        colors = DataSource().colorPairs,
                        selectedTime = levelTime,
                        onTimeChange = updateTime
                    )
                }) { paddingTopBar ->
                    LevelScreen(
                        modifier = Modifier.padding(paddingTopBar),
                        currentLevel = currentLevel,
                        starterLevel = starterLevel,
                        endingLevel = endingLevel,
                        color = color
                    ) {
                        changeGameState(false)//tells the game this is not a game mode
                        changeTime(levelTime)
                        navigateGame()
                    }
                }
            }
            composable(route = BottomBarScreensNames.Modes.name) {
                ModesScreen(
                    color = color,
                    navigateFastGame = {
                        changeGameMode(0)
                        generateRandomLettersForMode()
                        changeTime(20000L)
                        changeGameState(true)//this is a game mode
                        navigateGame()
                    },
                    navigateLongGame = {
                        changeGameMode(1)
                        generateRandomLettersForMode()
                        changeTime(130000000L) //130000000L means no time
                        changeGameState(true) //this is a game mode
                        navigateGame()
                    },
                    navigateChangingGame = {
                        changeGameMode(2)
                        generateRandomLettersForMode()
                        changeTime(levelTime)
                        changingLetters(
                            true,
                            playChangeSound,
                            levelTime
                        )
                        changeGameState(true) //this is a game mode
                        navigateGame()
                    },
                    navigateConsequencesGame = {
                        changeGameMode(3)
                        generateRandomLettersForMode()
                        changeTime(levelTime)
                        changeGameState(true) //this is a game mode
                        navigateGame()
                    },
                    navigateCombatGame = { //change this navigation
                        changeGameMode(4)
                        changeGameState(true) //this is a game mode
                        generateRandomLettersForMode()
                        navigateGameCombat()
                    },
                    colorCodePlayerOne = colorCodePlayerOne,
                    colorCodePlayerTwo = colorCodePlayerTwo,
                    changeColorPlayerCombat = changeColorPlayerCombat
                )
            }
            composable(route = BottomBarScreensNames.Profile.name) {
                ProfileScreen(
                    currentLevel,
                    streak,
                    highScore,
                    nickname,
                    character,
                    profileSelected,
                    color,
                    changeProfilePic = changeProfilePic,
                    navigate = navigateSettings
                )
            }
        }
    }
}

private fun NavHostController.navigateToScreen(index: Int) {
    val screen = when (index) {
        0 -> BottomBarScreensNames.Levels.name
        1 -> BottomBarScreensNames.Modes.name
        2 -> BottomBarScreensNames.Profile.name
        else -> BottomBarScreensNames.Levels.name
    }
    navigate(screen) {
        popUpTo(graph.findStartDestination().id) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}
