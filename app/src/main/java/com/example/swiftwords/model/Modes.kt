package com.example.swiftwords.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class Modes (
    @StringRes val stringResourceId: Int,
    @DrawableRes val imageResourceId: Int,
)