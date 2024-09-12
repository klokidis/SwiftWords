package com.example.swiftwords.ui

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
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
import com.example.swiftwords.ui.game.Game
import com.example.swiftwords.ui.levels.LevelScreen
import com.example.swiftwords.ui.levels.TopBar
import com.example.swiftwords.ui.loading.LoadingView
import com.example.swiftwords.ui.modes.ModesScreen
import com.example.swiftwords.ui.profile.ProfileScreen
import com.example.swiftwords.ui.settings.SettingsPage
import kotlinx.coroutines.launch

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
        backStackEntry?.destination?.route ?: SwiftWordsScreen.Levels.name
    )
    var selectedItemIndex by rememberSaveable { mutableIntStateOf(0) }

    LaunchedEffect(currentScreen) {
        val newIndex = when (currentScreen) {
            SwiftWordsScreen.Levels -> 0
            SwiftWordsScreen.Modes -> 1
            SwiftWordsScreen.Profile -> 2
            SwiftWordsScreen.Settings -> 3
            else -> 0 // Default value
        }

        if (selectedItemIndex != newIndex) {//for navigating back with back phone arrow
            selectedItemIndex = newIndex
        }
    }

    LaunchedEffect(mainUiState.todayDate) {
        dataViewmodel.checkAndResetStreak()
    }

    val wordListState = rememberSaveable { mutableStateOf<Set<String>?>(null) }
    val coroutineScope = rememberCoroutineScope()
    val coroutineLaunched = rememberSaveable { mutableStateOf(false) }
    val context = LocalContext.current // Get context from LocalContext
    LaunchedEffect(Unit) {
        if (!coroutineLaunched.value) {
            coroutineScope.launch {
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
                NavigationBar {
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
        },
        topBar = {
            if (currentScreen == SwiftWordsScreen.Levels) {
                dataUiState.userDetails?.let {
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
                if (dataUiState.userDetails?.initializeProfile == true) {
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
                StartingScreen(dataViewmodel = dataViewmodel)
            }
            composable(route = SwiftWordsScreen.Levels.name) {
                LevelScreen(
                    dataUiState = dataUiState,
                ) {
                    viewModel.changeGameState(false)//tells the game this is not a game mode
                    viewModel.changeTime(40000L)
                    navController.navigate(SwiftWordsScreen.Game.name)
                }
            }
            composable(route = SwiftWordsScreen.Modes.name) {
                ModesScreen(
                    color = dataUiState.userDetails?.color,
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
                        viewModel.changeTime(40000L)
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
                        viewModel.changeTime(40000L)
                        viewModel.changeGameState(true) //this is a game mode
                        navController.navigate(SwiftWordsScreen.Game.name)
                    },
                    changeTime = viewModel::changeTime,
                    changeGameMode = viewModel::changeGameMode,
                    navigateCustomGame = {
                        viewModel.changeGameState(true) //this is a game mode
                        navController.navigate(SwiftWordsScreen.Game.name)
                    },
                    sound = soundViewModel::playChangeSound,
                    startShuffle = viewModel::changingLetters,
                )
            }
            composable(route = SwiftWordsScreen.Profile.name) {
                dataUiState.userDetails?.let { data ->
                    ProfileScreen(
                        data.currentLevel,
                        data.streak,
                        data.highScore,
                        data.nickname,
                        data.character,
                        data.color,
                        navigate = { navController.navigate(SwiftWordsScreen.Settings.name) }
                    )
                }
            }
            composable(route = SwiftWordsScreen.Game.name) {
                wordListState.value?.let { wordList ->
                    dataUiState.userDetails?.let { data ->
                        Game(
                            increaseStreak = dataViewmodel::increaseStreak,
                            dateNow = mainUiState.todayDate,
                            dataDate = { data.dailyDate },
                            newTime = { mainUiState.gameTime },
                            wordList = wordList,
                            navigateUp = { navController.navigateUp() },
                            colorCode = data.color,
                            increaseScore = dataViewmodel::increaseCurrentLevel,
                            checkHighScore = dataViewmodel::checkHighScore,
                            mainViewModel = viewModel,
                            isMode = mainUiState.isMode,
                            gameModeNumber = mainUiState.gameMode,
                            setOfLetters = if (mainUiState.isMode) {
                                mainUiState.setOfLettersForMode
                            } else {
                                mainUiState.setOfLettersForLevel
                            },
                            listOfLetters = if (mainUiState.isMode) {
                                mainUiState.listOfLettersForMode
                            } else {
                                mainUiState.listOfLettersForLevel
                            },
                            shuffle = viewModel::shuffleLetters,
                            highScore = data.highScore,
                            checked = { data.checked },
                            changeChecked = dataViewmodel::updateChecked,
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
                            soundViewModel = soundViewModel
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
                    data = dataUiState,
                    navigateOut = { navController.navigateUp() })
            }
        }
    }
}