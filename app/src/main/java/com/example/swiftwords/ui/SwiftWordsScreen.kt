package com.example.swiftwords.ui

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.swiftwords.R
import com.example.swiftwords.data.DataSource
import com.example.swiftwords.data.GetDataViewModel
import com.example.swiftwords.model.BarItem
import com.example.swiftwords.ui.choose.StartingScreen
import com.example.swiftwords.ui.elements.SoundViewModel
import com.example.swiftwords.ui.elements.darken
import com.example.swiftwords.ui.game.Game
import com.example.swiftwords.ui.levels.LevelScreen
import com.example.swiftwords.ui.levels.TopBar
import com.example.swiftwords.ui.loading.LoadingView
import com.example.swiftwords.ui.modes.ModesScreen
import com.example.swiftwords.ui.profile.ProfileScreen
import com.example.swiftwords.ui.settings.SettingsPage

enum class SwiftWordsScreen {
    Loading,
    Choose,
    Levels,
    Modes,
    Profile,
    Game,
    Settings,
}

@Composable
fun SwiftWordsApp(
    dataViewmodel: GetDataViewModel = viewModel(factory = AppViewModelProvider.Factory),
    viewModel: SwiftWordsMainViewModel = viewModel(factory = AppViewModelProvider.Factory),
    soundViewModel: SoundViewModel = viewModel(factory = AppViewModelProvider.Factory),
    navController: NavHostController = rememberNavController()
) {
    val dataUiState by dataViewmodel.getDataUiState.collectAsState()
    val mainUiState by viewModel.uiState.collectAsState()

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = SwiftWordsScreen.valueOf(
        backStackEntry?.destination?.route ?: SwiftWordsScreen.Loading.name
    )
    var selectedItemIndex by rememberSaveable { mutableIntStateOf(0) }

    LaunchedEffect(currentScreen) {
        val newIndex = when (currentScreen) {
            SwiftWordsScreen.Levels -> 0
            SwiftWordsScreen.Modes -> 1
            SwiftWordsScreen.Profile -> 2
            else -> 0 // Default value
        }

        if (selectedItemIndex != newIndex) { //for navigating back with back phone arrow
            selectedItemIndex = newIndex
        }
    }

    LaunchedEffect(mainUiState.todayDate) { //every 12am check the streak
        dataViewmodel.checkAndResetStreak()
    }

    val wordListState = rememberSaveable { mutableStateOf<Set<String>?>(null) }
    val coroutineLaunched = rememberSaveable { mutableStateOf(false) }
    val context = LocalContext.current // Get context from LocalContext
    LaunchedEffect(coroutineLaunched.value) {
        if (!coroutineLaunched.value) {//retrieve the word list from file on launch
            try {
                val wordList = viewModel.loadWordsFromAssets(context)
                wordListState.value = wordList
                coroutineLaunched.value = true
            } catch (e: Exception) {
                // Handle the exception appropriately, update UI to show an error message
                e.printStackTrace()
            }
        }
    }

    Scaffold(
        bottomBar = {
            if (
                currentScreen != SwiftWordsScreen.Game
                && currentScreen != SwiftWordsScreen.Settings
                && currentScreen != SwiftWordsScreen.Choose
                && currentScreen != SwiftWordsScreen.Loading
            ) {
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
                            NavigationBarItem(
                                onClick = {
                                    if (selectedItemIndex != index) {
                                        when (index) {
                                            0 -> navController.navigate(SwiftWordsScreen.Levels.name) {
                                                popUpTo(navController.graph.findStartDestination().id) {
                                                    saveState = true
                                                }
                                                launchSingleTop = true
                                                restoreState = true
                                            }

                                            1 -> navController.navigate(SwiftWordsScreen.Modes.name) {
                                                popUpTo(navController.graph.findStartDestination().id) {
                                                    saveState = true
                                                }
                                                launchSingleTop = true
                                                restoreState = true
                                            }

                                            2 -> navController.navigate(SwiftWordsScreen.Profile.name) {
                                                popUpTo(navController.graph.findStartDestination().id) {
                                                    saveState = true
                                                }
                                                launchSingleTop = true
                                                restoreState = true
                                            }
                                        }
                                        selectedItemIndex = index
                                    }
                                },
                                selected = selectedItemIndex == index,
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
            }
        },
        modifier = Modifier.safeDrawingPadding(),
        topBar = {
            if (currentScreen == SwiftWordsScreen.Levels) {
                dataUiState.userDetails.let {
                    TopBar(
                        livesLeft = it.lives,
                        streak = it.streak,
                        streakDateData = it.dailyDate,
                        dateNow = mainUiState.todayDate,
                        color = it.color,
                        changeColorFun = dataViewmodel::updateUserColor,
                        colors = DataSource().colorPairs
                    )
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = if (dataUiState.isLoading) {
                SwiftWordsScreen.Loading.name
            } else {
                if (dataUiState.userDetails.initializeProfile) {
                    SwiftWordsScreen.Choose.name
                } else {
                    SwiftWordsScreen.Levels.name
                }
            },
            modifier = Modifier.padding(paddingValues),
            enterTransition = { fadeIn(animationSpec = tween(0)) },
            exitTransition = { fadeOut(animationSpec = tween(0)) },
        ) {
            composable(route = SwiftWordsScreen.Loading.name) {
                LoadingView()
            }
            composable(route = SwiftWordsScreen.Choose.name) {
                StartingScreen(
                    updateInitialState = dataViewmodel::updateInitialState,
                    updateName = dataViewmodel::updateName,
                    updateCharacter = dataViewmodel::updateCharacter,
                    playLetterSound = soundViewModel::playLetterSound,
                    nickName = dataUiState.userDetails.nickname
                )
            }
            composable(route = SwiftWordsScreen.Levels.name) {
                LevelScreen(
                    dataUiState = dataUiState,
                ) {
                    viewModel.changeGameState(false)//tells the game this is not a game mode
                    viewModel.changeTime(dataUiState.userDetails.levelTime)
                    navController.navigate(SwiftWordsScreen.Game.name)
                }
            }
            composable(route = SwiftWordsScreen.Modes.name) {
                ModesScreen(
                    color = dataUiState.userDetails.color,
                    navigateFastGame = {
                        viewModel.changeGameMode(0)
                        viewModel.generateRandomLettersForMode()
                        viewModel.changeTime(20000L)
                        viewModel.changeGameState(true)//this is a game mode
                        navController.navigate(SwiftWordsScreen.Game.name)
                    },
                    navigateLongGame = {
                        viewModel.changeGameMode(1)
                        viewModel.generateRandomLettersForMode()
                        viewModel.changeTime(130000000L) //130000000L means no time
                        viewModel.changeGameState(true) //this is a game mode
                        navController.navigate(SwiftWordsScreen.Game.name)
                    },
                    navigateChangingGame = {
                        viewModel.changeGameMode(2)
                        viewModel.generateRandomLettersForMode()
                        viewModel.changeTime(dataUiState.userDetails.levelTime)
                        viewModel.changingLetters(
                            true,
                            soundViewModel::playChangeSound,
                            40000L
                        )
                        viewModel.changeGameState(true) //this is a game mode
                        navController.navigate(SwiftWordsScreen.Game.name)
                    },
                    navigateConsequencesGame = {
                        viewModel.changeGameMode(3)
                        viewModel.generateRandomLettersForMode()
                        viewModel.changeTime(dataUiState.userDetails.levelTime)
                        viewModel.changeGameState(true) //this is a game mode
                        navController.navigate(SwiftWordsScreen.Game.name)
                    },
                    changeTime = viewModel::changeTime,
                    changeGameMode = viewModel::changeGameMode,
                    navigateCustomGame = {
                        viewModel.changeGameState(true) //this is a game mode
                        viewModel.generateRandomLettersForMode()
                        navController.navigate(SwiftWordsScreen.Game.name)
                    },
                    sound = soundViewModel::playChangeSound,
                    characterIsFemale = dataUiState.userDetails.character,
                    startShuffle = viewModel::changingLetters,
                )
            }
            composable(route = SwiftWordsScreen.Profile.name) {
                dataUiState.userDetails.let { data ->
                    ProfileScreen(
                        data.currentLevel,
                        data.streak,
                        data.highScore,
                        data.nickname,
                        data.character,
                        navigate = { navController.navigate(SwiftWordsScreen.Settings.name) }
                    )
                }
            }
            composable(route = SwiftWordsScreen.Game.name) {
                wordListState.value?.let { wordList ->
                    dataUiState.userDetails.let { data ->
                        Game(
                            dateNow = mainUiState.todayDate,
                            dataDate = { data.dailyDate },
                            newTime = { mainUiState.gameTime },
                            wordList = wordList,
                            colorCode = data.color,
                            isMode = mainUiState.isMode,
                            gameModeNumber = mainUiState.gameMode,
                            increaseScore = dataViewmodel::increaseCurrentLevel,
                            navigateUp = { navController.navigateUp() },
                            checkHighScore = dataViewmodel::checkHighScore,
                            setOfLetters = if (mainUiState.isMode) {
                                mainUiState.setOfLettersForMode
                            } else {
                                mainUiState.setOfLettersForLevel
                            },
                            highScore = data.highScore,
                            checked = { data.checked },
                            changeChecked = dataViewmodel::updateChecked,
                            increaseStreak = dataViewmodel::increaseStreak,
                            listOfLetters = if (mainUiState.isMode) {
                                mainUiState.listOfLettersForMode
                            } else {
                                mainUiState.listOfLettersForLevel
                            },
                            shuffle = viewModel::shuffleLetters,
                            exitChangingMode = {
                                viewModel.changingLetters(
                                    false,
                                    soundViewModel::playChangeSound,
                                    mainUiState.gameTime
                                )
                            },
                            launchChanging = {
                                viewModel.changingLetters(
                                    true,
                                    soundViewModel::playChangeSound,
                                    mainUiState.gameTime
                                )
                            },
                            currentLevel = data.currentLevel,
                            streakLevel = data.streak,
                            characterIsFemale = data.character,
                            playCorrectSound = soundViewModel::playCorrectSound,
                            playIncorrectSound = soundViewModel::playIncorrectSound,
                            generateRandomLettersForMode = viewModel::generateRandomLettersForMode,
                            generateRandomLettersForBoth = viewModel::generateRandomLettersForBoth,
                            generateRandomLettersForBothOnExit = viewModel::generateRandomLettersForBothOnExit
                        )
                    }
                } ?: run {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Loading...")
                    }
                }
            }
            composable(route = SwiftWordsScreen.Settings.name) {
                SettingsPage(
                    updateTime = dataViewmodel::updateTime,
                    changeCharacter = dataViewmodel::updateCharacter,
                    changeName = dataViewmodel::updateName,
                    dataColor = dataUiState.userDetails.color,
                    nickname = dataUiState.userDetails.nickname,
                    character = dataUiState.userDetails.character,
                    levelTime = dataUiState.userDetails.levelTime,
                    navigateOut = { navController.navigateUp() },
                    introduction = dataViewmodel::updateInitialState
                )
            }
        }
    }
}