package com.example.swiftwords.model

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector

data class BarItem(
    @StringRes val title: Int,
    val imageSelected: ImageVector,
    val imageUnSelected: ImageVector,
    val hasNews: Boolean,
)
