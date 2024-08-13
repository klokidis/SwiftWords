package com.example.swiftwords.model

import androidx.annotation.StringRes

data class BarItem(
    @StringRes val title: Int,
    val imageSelected: Int,
    val imageUnSelected: Int,
    val hasNews: Boolean,
)
