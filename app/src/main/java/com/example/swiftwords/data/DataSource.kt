package com.example.swiftwords.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.Star
import com.example.swiftwords.R
import com.example.swiftwords.model.BarItem
import com.example.swiftwords.model.Modes

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
            R.string.profile,
            Icons.Filled.AccountCircle,
            Icons.Outlined.AccountCircle,
            false
        ),
    )

    fun loadModes(): List<Modes> {
        return listOf(
            Modes(R.string.app_name, R.drawable.done),
            Modes(R.string.app_name, R.drawable.done),
            Modes(R.string.app_name, R.drawable.done),
            Modes(R.string.app_name, R.drawable.done),
        )
    }
}