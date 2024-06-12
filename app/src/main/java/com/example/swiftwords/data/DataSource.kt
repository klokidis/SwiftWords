package com.example.swiftwords.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.PlayArrow
import com.example.swiftwords.R
import com.example.swiftwords.model.BarItem

class DataSource {

    val barItems = listOf(
        BarItem(
            R.string.home,
            Icons.Filled.Home,
            Icons.Outlined.Home,
            false
        ),
        BarItem(
            R.string.modes,
            Icons.Filled.PlayArrow,
            Icons.Outlined.PlayArrow,
            true
        ),
        BarItem(
            R.string.home,
            Icons.Filled.Build,
            Icons.Outlined.Build ,
            false
        ),
    )
}