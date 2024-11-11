package com.yukuro.swiftwords.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.yukuro.swiftwords.R

val Bebasneuregular = FontFamily(
    Font(R.font.bebasneuregular),
)
val Radiocanadabigregular = FontFamily(
    Font(R.font.radiocanadabigregular),
)
val pacificoregular = FontFamily(
    Font(R.font.pacificoregular),
)

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    titleLarge = TextStyle(
        fontFamily = Bebasneuregular,
        fontWeight = FontWeight.Normal,
        fontSize = 35.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    titleMedium = TextStyle(
        fontFamily = pacificoregular,
        fontSize = 30.sp,
    ),
    titleSmall = TextStyle(
        fontFamily = Radiocanadabigregular,
        fontWeight = FontWeight.Bold,
        fontSize = 15.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = Radiocanadabigregular,
        fontWeight = FontWeight.Normal,
        fontSize = 25.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.8.sp
    ),
)