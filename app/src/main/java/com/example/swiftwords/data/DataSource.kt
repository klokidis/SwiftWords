package com.example.swiftwords.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.ui.graphics.Color
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
            R.string.profile,
            Icons.Filled.AccountCircle,
            Icons.Outlined.AccountCircle,
            false
        )
    )

    val colorPairs = listOf(
        ColorPair(id = 0, lightColor = Color.White, darkColor = Color(0xFF8B0000)), // Red
        ColorPair(id = 1, lightColor = Color.White, darkColor = Color(0xFF006400)), // Green
        ColorPair(id = 2, lightColor = Color.White, darkColor = Color(0xFF092E5C)), // Blue
        ColorPair(id = 3, lightColor = Color.White, darkColor = Color(0xFF0C9B86)), // Yellow
        ColorPair(id = 4, lightColor = Color.White, darkColor = Color(0xFF7E0A8A)),  // Magenta
    )
}


data class ColorPair(
    val id: Int,
    val lightColor: Color,
    val darkColor: Color
)
