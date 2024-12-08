package com.yukuro.swiftwords.ui

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.yukuro.swiftwords.data.GetDataViewModel
import com.yukuro.swiftwords.ui.choose.StartingScreen
import com.yukuro.swiftwords.ui.credits.CreditsScreen
import com.yukuro.swiftwords.ui.elements.SoundViewModel
import com.yukuro.swiftwords.ui.game.Game
import com.yukuro.swiftwords.ui.game.GameCombat
import com.yukuro.swiftwords.ui.loading.LoadingView
import com.yukuro.swiftwords.ui.settings.SettingsPage

enum class SwiftWordsScreen {
    Loading,
    Choose,
    BottomBarScreens,
    Game,
    Settings,
    Credits
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
        modifier = Modifier.safeDrawingPadding()
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = if (dataUiState.isLoading) {
                SwiftWordsScreen.Loading.name
            } else {
                if (dataUiState.userDetails.initializeProfile) {
                    SwiftWordsScreen.Choose.name
                } else {
                    SwiftWordsScreen.BottomBarScreens.name
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
                    nickName = dataUiState.userDetails.nickname,
                    profileSelected = dataUiState.userDetails.profileSelected,
                    changeProfilePic = dataViewmodel::changeProfilePic,
                    loadLettersSound = soundViewModel::loadLettersSound,
                    releaseAllAlphabetSounds = soundViewModel::releaseAllAlphabetSounds,
                    level = dataUiState.userDetails.currentLevel
                )
            }
            composable(route = SwiftWordsScreen.BottomBarScreens.name) { //separated screens to fix the bottom navigation junk. Since having it appear with an if condition it doesn't run smoothly
                dataUiState.userDetails.let {
                    BottomBarNavGraph(
                        streak = it.streak,
                        streakDateData = it.dailyDate,
                        color = it.color,
                        levelTime = it.levelTime,
                        currentLevel = it.currentLevel,
                        starterLevel = it.starterLevel,
                        endingLevel = it.endingLevel,
                        highScore = it.highScore,
                        nickname = it.nickname,
                        character = it.character,
                        profileSelected = it.profileSelected,
                        changeGameState = viewModel::changeGameState,
                        changeTime = viewModel::changeTime,
                        changeGameMode = viewModel::changeGameMode,
                        generateRandomLettersForMode = viewModel::generateRandomLettersForMode,
                        changingLetters = viewModel::changingLetters,
                        mainUiState = mainUiState,
                        updateUserColor = dataViewmodel::updateUserColor,
                        changeProfilePic = dataViewmodel::changeProfilePic,
                        updateTime = dataViewmodel::updateTime,
                        playChangeSound = soundViewModel::playChangeSound,
                        navigateGame = { navController.navigate(SwiftWordsScreen.Game.name) },
                        navigateSettings =
                        { navController.navigate(SwiftWordsScreen.Settings.name) }
                    )
                }
            }
            composable(
                route = SwiftWordsScreen.Game.name,
                enterTransition = {
                    scaleIn(
                        initialScale = 0.8f,
                        animationSpec = tween(
                            durationMillis = 500,
                            easing = CubicBezierEasing(0.34f, 1.56f, 0.64f, 1f)
                        )
                    ) + fadeIn(animationSpec = tween(durationMillis = 500))
                },
            ) {
                wordListState.value?.let { wordList ->
                    dataUiState.userDetails.let { data ->
                        if (false) {
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
                        } else {// make a new nav composable
                            GameCombat(
                                newTime = { mainUiState.gameTime },
                                wordList = wordList,
                                colorCodePlayerOne = data.color,
                                colorCodePlayerTwo = 7,
                                navigateUp = { navController.navigateUp() },
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
                                characterIsFemale = data.character,
                                playCorrectSound = soundViewModel::playCorrectSound,
                                playIncorrectSound = soundViewModel::playIncorrectSound,
                                exitChangingMode = {
                                    viewModel.changingLetters(
                                        false,
                                        soundViewModel::playChangeSound,
                                        mainUiState.gameTime
                                    )
                                },
                                generateRandomLettersForMode = viewModel::generateRandomLettersForMode,
                                colorTheme = dataUiState.userDetails.color
                            )
                        }
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
                    introduction = dataViewmodel::updateInitialState,
                    profileSelected = dataUiState.userDetails.profileSelected,
                    changeProfilePic = dataViewmodel::changeProfilePic,
                    navigateCredit = { navController.navigate(SwiftWordsScreen.Credits.name) }
                )
            }
            composable(route = SwiftWordsScreen.Credits.name) {
                CreditsScreen(
                    navigateOut = { navController.navigateUp() }
                )
            }
        }
    }
}