package com.example.swiftwords

import androidx.annotation.StringRes
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.swiftwords.ui.ViewModel

enum class SwiftWordsScreen(@StringRes var title: Int) {
    Start(title = R.string.app_name),
    Levels(title = R.string.home),
    Modes(title = R.string.modes)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomAppBar() {
    var selectedItemIndex by rememberSaveable {
        mutableIntStateOf(0)
    }
    NavigationBar {
        DataSource().barItems.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = selectedItemIndex == index,
                onClick = {
                    selectedItemIndex = index
                },
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

@Composable
fun SwiftWordsApp(
    viewModel: ViewModel = ViewModel(),
    navController: NavHostController = rememberNavController()
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = SwiftWordsScreen.valueOf(
        backStackEntry?.destination?.route ?: SwiftWordsScreen.Start.name
    )

    Scaffold(bottomBar = { BottomAppBar() }) { it
        val uiState by viewModel.uiState.collectAsState()


        NavHost(
            navController = navController,
            startDestination = SwiftWordsScreen.Start.name,
        ) {
            composable(route = SwiftWordsScreen.Start.name) {
                LevelMap(1)
            }
            composable(route = SwiftWordsScreen.Modes.name) {
                LevelMap(10)
            }
        }
    }
}

@Composable
fun LevelMap(level: Int) {
    Box{
        Text(text = level.toString())
    }
}