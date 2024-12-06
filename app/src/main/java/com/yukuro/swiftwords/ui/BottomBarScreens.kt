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
import androidx.compose.runtime.getValue
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
import com.yukuro.swiftwords.ui.elements.darken
import com.yukuro.swiftwords.ui.levels.LevelScreen
import com.yukuro.swiftwords.ui.levels.TopBar
import com.yukuro.swiftwords.ui.modes.ModesScreen
import com.yukuro.swiftwords.ui.profile.ProfileScreen

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
    playChangeSound: () -> Unit
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = BottomBarScreensNames.valueOf(
        backStackEntry?.destination?.route ?: BottomBarScreensNames.Levels.name
    )

    Scaffold(
        bottomBar = {
            val barItems = listOf(
                BarItem(
                    R.string.levels,
                    BottomBarScreensNames.Levels,
                    R.drawable.levels,
                    R.drawable.levels
                ),
                BarItem(
                    R.string.modes,
                    BottomBarScreensNames.Modes,
                    R.drawable.controller_filled,
                    R.drawable.controller
                ),
                BarItem(
                    R.string.profile,
                    BottomBarScreensNames.Profile,
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
                    barItems.forEach { item ->
                        val isSelected = currentScreen == item.screen
                        NavigationBarItem(
                            onClick = {
                                if (!isSelected) {
                                    navController.navigate(item.screen.name) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            },
                            selected = isSelected,
                            label = { Text(text = stringResource(item.title)) },
                            icon = {
                                Icon(
                                    imageVector = if (isSelected) ImageVector.vectorResource(
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
                    changeTime = changeTime,
                    changeGameMode = changeGameMode,
                    navigateCustomGame = {
                        changeGameState(true) //this is a game mode
                        generateRandomLettersForMode()
                        navigateGame()
                    },
                    sound = playChangeSound,
                    startShuffle = changingLetters,
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