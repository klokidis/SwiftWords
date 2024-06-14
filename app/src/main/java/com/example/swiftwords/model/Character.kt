package com.example.swiftwords.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class Character(
    @StringRes val name : Int,
    @DrawableRes val imageFullBody: Int,
    @DrawableRes val imageIcon: Int,
)
