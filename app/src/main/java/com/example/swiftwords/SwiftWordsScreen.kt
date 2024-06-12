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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import com.example.swiftwords.data.DataSource

enum class SwiftWordsScreen(@StringRes var title: Int) {
    Start(title = R.string.app_name),
    Level(title = R.string.home),
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomAppBar(

) {
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
fun SwiftWordsApp(){
    Scaffold(bottomBar = { BottomAppBar() }) { it

    }
}