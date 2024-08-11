package com.example.swiftwords.ui.levels

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.example.swiftwords.R
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.swiftwords.data.DataSource
import com.example.swiftwords.ui.AppViewModelProvider
import com.example.swiftwords.ui.theme.SwiftWordsTheme

@Composable
fun LevelScreen(
    levelViewModel: LevelViewModel = viewModel(factory = AppViewModelProvider.Factory),
    level: Int,
    livesLeft: Int,
    streak: Int,
    color: Int,
    navigateToLevel: () -> Unit,
) {
    val levelUiState by levelViewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopStart
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 20.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LevelList(
                currentLevel = level,
                levelViewModel.calculatePaddingValues(level),
                navigateToLevel,
                color
            )
        }

        TopBar(livesLeft, streak, color)

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            BottomLevel(navigateToLevel, level.toString(), color)
        }
    }
}

@Composable
fun LevelList(
    currentLevel: Int,
    calculatePaddingValues: List<Pair<Dp, Dp>>,
    onClick: () -> Unit,
    color: Int
) {
    for ((index, i) in (currentLevel - 3..currentLevel + 20).withIndex()) {
        if (i > 0) {
            val (leftPadding, rightPadding) = calculatePaddingValues.getOrNull(index) ?: continue

            LevelCard(i, rightPadding, leftPadding, i, currentLevel, onClick, color)
        }
    }
}

@Composable
fun LevelCard(
    number: Int,
    rightPadding: Dp,
    leftPadding: Dp,
    thisLevel: Int,
    currentLevel: Int,
    onClick: () -> Unit,
    color: Int
) {
    Box(
        modifier = Modifier
            .padding(
                start = leftPadding,
                end = rightPadding,
                top = 50.dp
            ),
        contentAlignment = Alignment.Center
    ) {
        when {
            thisLevel < currentLevel -> {
                Levels(
                    modifier = Modifier,
                    text = number.toString(),
                    color = Color.Yellow,
                    shadowColor = Color.DarkGray
                )
            }

            thisLevel == currentLevel -> {
                CurrentLevel(
                    modifier = Modifier,
                    text = number.toString(),
                    onClick = onClick,
                    colorCode = color,
                )
            }

            else -> {
                Levels(
                    modifier = Modifier,
                    text = number.toString(),
                    color = Color.Gray,
                    shadowColor = Color.Black
                )
            }
        }
    }
}

@Composable
fun TopBar(livesLeft: Int, streak: Int, color: Int) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        color = MaterialTheme.colorScheme.background,
        shadowElevation = 3.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            MenuColorPicker(color)
            Spacer(modifier = Modifier.weight(1f))
            Image(painter = painterResource(id = R.drawable.done), contentDescription = "streak")
            Text(text = streak.toString(), modifier = Modifier.padding(start = 8.dp))
            Spacer(modifier = Modifier.weight(1f))
            Image(
                painter = painterResource(id = R.drawable.done),
                contentDescription = "lives left"
            )
            Text(text = livesLeft.toString(), modifier = Modifier.padding(start = 8.dp))
        }
    }
}

@Composable
fun MenuColorPicker(color: Int) {
    var isExpanded by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()
    Box(
        modifier = Modifier
            .wrapContentSize()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                modifier = Modifier
                    .size(30.dp)
                    .clickable { isExpanded = !isExpanded },
                imageVector = Icons.Filled.Face,
                contentDescription = "",
            )
            AnimatedVisibility(visible = isExpanded) {
                Row(
                    modifier = Modifier
                        .padding(start = 4.dp)
                        .clip(RoundedCornerShape(40.dp))
                        .border(1.dp, DataSource().colorPairs[color].darkColor, RoundedCornerShape(40.dp))
                        .height(40.dp)
                        .wrapContentSize(),
                ) {
                    Row(modifier = Modifier.horizontalScroll(scrollState)) {
                        DataSource().colorPairs.forEach { color ->
                            Box(
                                modifier = Modifier
                                    .padding(start = 10.dp, end = 10.dp)
                                    .size(26.dp)
                                    .clip(RoundedCornerShape(26.dp))
                                    .background(color.lightColor)
                                    .clickable { }
                            ) {
                                Box(
                                    modifier = Modifier
                                        .align(Alignment.Center)
                                        .size(16.dp)
                                        .clip(RoundedCornerShape(20.dp))
                                        .background(color.darkColor)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewMenuColorPicker() {
    MenuColorPicker(2)
}


@Composable
fun BottomLevel(onClick: () -> Unit, level: String, color: Int) {
    val animatedColor by animateColorAsState(
        targetValue = DataSource().colorPairs[color].darkColor,
        animationSpec = tween(durationMillis = 400),
        label = ""
    )
    Card(
        modifier = Modifier
            .padding(15.dp)
            .height(50.dp)
            .fillMaxWidth(0.6f),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp,
        ),
        onClick = onClick
    ) {
        Box(
            modifier = Modifier
                .background(animatedColor)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Level: $level", color = Color.White)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CardPreview() {
    SwiftWordsTheme {
        MenuColorPicker(2)
    }
}
