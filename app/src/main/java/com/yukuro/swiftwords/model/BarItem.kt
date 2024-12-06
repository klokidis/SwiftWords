package com.yukuro.swiftwords.model

import androidx.annotation.StringRes
import com.yukuro.swiftwords.ui.BottomBarScreensNames

data class BarItem(
    @StringRes val title: Int,
    val screen: BottomBarScreensNames,
    val imageSelected: Int,
    val imageUnSelected: Int
)
