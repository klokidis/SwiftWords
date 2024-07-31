package com.example.swiftwords

import android.content.Context
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import com.example.swiftwords.data.DataSource
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.swiftwords.ui.AppViewModelProvider
import com.example.swiftwords.ui.game.Game
import com.example.swiftwords.ui.levels.LevelScreen
import com.example.swiftwords.ui.modes.ModesScreen
import com.example.swiftwords.ui.profile.ProfileScreen
import kotlinx.coroutines.launch

enum class SwiftWordsScreen {
    Levels,
    Modes,
    Profile,
    Game
}

@Composable
fun SwiftWordsApp(
    context: Context,
    viewModel: MainViewModel = viewModel(factory = AppViewModelProvider.Factory),
    navController: NavHostController = rememberNavController()
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val mainUiState by viewModel.uiState.collectAsState()
    val currentScreen = SwiftWordsScreen.valueOf(
        backStackEntry?.destination?.route ?: SwiftWordsScreen.Levels.name
    )
    var selectedItemIndex by rememberSaveable {
        mutableIntStateOf(0)
    }

    val wordListState = remember { mutableStateOf<Set<String>?>(null) }
    val coroutineScope = rememberCoroutineScope()

    // Launch a coroutine to load the word list
    LaunchedEffect(context) {
        coroutineScope.launch {
            val words = viewModel.loadWordsFromAssets(context)
            wordListState.value = words
        }
    }

    Scaffold(
        bottomBar = {
            if (currentScreen != SwiftWordsScreen.Game) {
                NavigationBar {
                    DataSource().barItems.forEachIndexed { index, item ->
                        NavigationBarItem(
                            onClick = {
                                if (selectedItemIndex != index) {
                                    when (index) {
                                        0 -> navController.navigate(SwiftWordsScreen.Levels.name) {
                                            popUpTo(navController.graph.findStartDestination().id)
                                            launchSingleTop = true
                                        }

                                        1 -> navController.navigate(SwiftWordsScreen.Modes.name) {
                                            popUpTo(navController.graph.findStartDestination().id)
                                            launchSingleTop = true
                                        }

                                        2 -> navController.navigate(SwiftWordsScreen.Profile.name) {
                                            popUpTo(navController.graph.findStartDestination().id)
                                            launchSingleTop = true
                                        }
                                    }
                                }
                            },
                            selected = selectedItemIndex == index,
                            label = {
                                Text(text = stringResource(item.title))
                            },
                            icon = {
                                BadgedBox(badge = {//this is for red notification
                                    if (item.hasNews) {
                                        Badge()
                                    }
                                }) {
                                    Icon(
                                        imageVector = if (index == selectedItemIndex) {
                                            item.imageSelected
                                        } else {
                                            item.imageUnSelected
                                        },
                                        contentDescription = stringResource(id = item.title)
                                    )
                                }
                            }
                        )
                    }
                }
            }
        }) {
        NavHost(
            navController = navController,
            startDestination = SwiftWordsScreen.Levels.name,
            modifier = Modifier.padding(it)
        ) {
            composable(route = SwiftWordsScreen.Levels.name) {
                LevelScreen { navController.navigate(SwiftWordsScreen.Game.name) }
                selectedItemIndex = 0
            }
            composable(route = SwiftWordsScreen.Modes.name) {
                ModesScreen()
                selectedItemIndex = 1
            }
            composable(route = SwiftWordsScreen.Profile.name) {
                ProfileScreen()
                selectedItemIndex = 2
            }
            composable(route = SwiftWordsScreen.Game.name) {
                wordListState.value?.let { it1 ->
                    Game(
                        listOfLetters = mainUiState.listOfLettersForLevel,
                        wordList = it1,
                        navigateUp = {navController.navigateUp()})
                }
            }
        }
    }
}

