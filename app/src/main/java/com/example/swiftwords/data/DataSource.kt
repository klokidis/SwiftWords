package com.example.swiftwords.data

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.swiftwords.R
import com.example.swiftwords.model.BarItem

class DataSource {
    val barItems = listOf(
        BarItem(
            R.string.levels,
            R.drawable.levels,
            R.drawable.levels,
            false
        ),
        BarItem(
            R.string.modes,
            R.drawable.controller_filled,
            R.drawable.controller,
            true
        ),
        BarItem(
            R.string.profile,
            R.drawable.profilr_filled,
            R.drawable.profile,
            false
        )
    )

    val colorPairs = listOf(
        ColorPair(id = 0, lightColor = Color.White, darkColor = Color(0xFF092E5C)),
        ColorPair(id = 1, lightColor = Color.White, darkColor = Color(0xFF007FA5)),
        ColorPair(id = 2, lightColor = Color.White, darkColor = Color(0xFF0C9B86)),
        ColorPair(id = 3, lightColor = Color.White, darkColor = Color(0xFF7E0A8A)),
        ColorPair(id = 4, lightColor = Color.White, darkColor = Color(0xFFC713A9)),
        ColorPair(id = 5, lightColor = Color.White, darkColor = Color(0xFFA7105E)),
        ColorPair(id = 6, lightColor = Color.White, darkColor = Color(0xFF006400)),
        ColorPair(id = 7, lightColor = Color.White, darkColor = Color(0xFF049C2F)),
        ColorPair(id = 8, lightColor = Color.White, darkColor = Color(0xFF7AA710)),
        ColorPair(id = 9, lightColor = Color.White, darkColor = Color(0xFF5E0000)),
        ColorPair(id = 10, lightColor = Color.White, darkColor = Color(0xFFC40000)),
        ColorPair(id = 11, lightColor = Color.White, darkColor = Color(0xFFE24615)),
    )

    val paddingList: List<Pair<Dp, Dp>> = listOf(
        // Right padding increases, left stays at 0
        Pair(0.dp, 0.dp),
        Pair(0.dp, 70.dp),
        Pair(0.dp, 140.dp),
        Pair(70.dp, 100.dp),
        Pair(170.dp, 70.dp),
        Pair(190.dp, 0.dp),
        Pair(170.dp, 70.dp),
        Pair(70.dp, 100.dp),
        Pair(0.dp, 140.dp),
        Pair(70.dp, 100.dp),
        Pair(170.dp, 70.dp),
        Pair(190.dp, 0.dp),
        Pair(170.dp, 70.dp),
        Pair(70.dp, 100.dp),
        Pair(0.dp, 140.dp),
        Pair(70.dp, 100.dp),
        Pair(170.dp, 70.dp),
        Pair(190.dp, 0.dp),
        Pair(170.dp, 70.dp),
        Pair(70.dp, 100.dp),
        Pair(0.dp, 140.dp),
        Pair(70.dp, 100.dp),
        Pair(170.dp, 70.dp),
        Pair(190.dp, 0.dp),
        Pair(170.dp, 70.dp),
        Pair(70.dp, 100.dp),
        Pair(0.dp, 140.dp),
        Pair(70.dp, 100.dp),
        Pair(170.dp, 70.dp),
        Pair(190.dp, 0.dp),
        Pair(170.dp, 70.dp),
        Pair(70.dp, 100.dp),
        Pair(0.dp, 140.dp),
        Pair(70.dp, 100.dp),
        Pair(170.dp, 70.dp),
        Pair(190.dp, 0.dp),
        Pair(170.dp, 70.dp),
    )
}

data class ColorPair(
    val id: Int,
    val lightColor: Color,
    val darkColor: Color
)
