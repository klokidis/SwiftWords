package com.example.swiftwords.ui.modes

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.swiftwords.R
import com.example.swiftwords.data.DataSource
import com.example.swiftwords.ui.levels.ModesCards
import com.example.swiftwords.ui.levels.darken


@Composable
fun ModesScreen(
    navigateFastGame: () -> Unit,
    navigateLongGame: () -> Unit,
    color: Int?
) {
    val scrollState = rememberScrollState()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(modifier = Modifier.fillMaxSize()) {
                ModeCard(
                    imageRes = R.drawable.profile,
                    textRes = R.string.fast,
                    onClick = { navigateFastGame() },
                    color = color,
                    modifier = Modifier.weight(1f) // Ensures the button fills available space
                )
                ModeCard(
                    imageRes = R.drawable.profile,
                    textRes = R.string.unlimited,
                    onClick = { navigateLongGame() },
                    color = color,
                    modifier = Modifier.weight(1f) // Ensures the button fills available space
                )
            }
            Row(modifier = Modifier.fillMaxSize()) {
                ModeCard(
                    imageRes = R.drawable.profile,
                    textRes = R.string.app_name,
                    onClick = { },
                    color = color,
                    modifier = Modifier.weight(1f) // Ensures the button fills available space
                )
                ModeCard(
                    imageRes = R.drawable.profile,
                    textRes = R.string.app_name,
                    onClick = { },
                    color = color,
                    modifier = Modifier.weight(1f) // Ensures the button fills available space
                )
            }
            BottomCard({ }, color)
        }
    }
}

@Composable
fun ModeCard(
    imageRes: Int,
    textRes: Int,
    onClick: () -> Unit,
    color: Int?,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.padding(10.dp)) {
        ModesCards(
            imageRes = imageRes,
            textRes = textRes,
            color = DataSource().colorPairs[color!!].darkColor,
            shadowColor = DataSource().colorPairs[color].darkColor.darken(),
            onClick = onClick
        )
    }
}

@Composable
fun BottomCard(
    onButtonCard: () -> Unit,
    color: Int?
) {
    Box(modifier = Modifier.padding(10.dp)) {
        ModesCards(
            imageRes = R.drawable.profile,
            textRes = R.string.levels,
            color = DataSource().colorPairs[color!!].darkColor,
            shadowColor = DataSource().colorPairs[color].darkColor.darken(),
            onClick = onButtonCard
        )
    }
}