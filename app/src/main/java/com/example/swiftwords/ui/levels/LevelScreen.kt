package com.example.swiftwords.ui.levels

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.Dp
import com.example.swiftwords.R
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.swiftwords.data.ColorPair
import com.example.swiftwords.ui.AppViewModelProvider
import com.example.swiftwords.ui.GetDataViewModel
import com.example.swiftwords.ui.ItemDetailsUiState

@Composable
fun LevelScreen(
    levelViewModel: LevelViewModel = viewModel(factory = AppViewModelProvider.Factory),
    dataViewModel: GetDataViewModel,
    dataUiState: ItemDetailsUiState,
    navigateToLevel: () -> Unit,
) {
    // Observe UI state from ViewModel
    val levelUiState by levelViewModel.uiState.collectAsState()

    // Scroll state for the vertical scrollable column
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopStart
    ) {
        // Main content column
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 20.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            dataUiState.userDetails?.let { userDetails ->
                LevelList(
                    currentLevel = userDetails.currentLevel,
                    startingLevel = userDetails.starterLevel,
                    endingLevel = userDetails.endingLevel,
                    calculatePaddingValues = levelUiState.padding,
                    onClick = navigateToLevel,
                    color = userDetails.color,
                    colors = levelUiState.colors
                )
            }
        }

        // TopBar for user details
        dataUiState.userDetails?.let { userDetails ->
            TopBar(
                livesLeft = userDetails.lives,
                streak = userDetails.streak,
                color = userDetails.color,
                changeColorFun = dataViewModel::updateUserColor,
                colors = levelUiState.colors
            )
        }

        // BottomLevel for the current level
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            dataUiState.userDetails?.let { userDetails ->
                BottomLevel(
                    onClick = navigateToLevel,
                    level = userDetails.currentLevel.toString(),
                    color = userDetails.color,
                    colors = levelUiState.colors
                )
            }
        }
    }
}



@Composable
fun LevelList(
    currentLevel: Int,
    calculatePaddingValues: List<Pair<Dp, Dp>>,
    onClick: () -> Unit,
    color: Int,
    startingLevel: Int,
    endingLevel: Int,
    colors: List<ColorPair>
) {
    for ((index, i) in (startingLevel..endingLevel).withIndex()) {
        if (i > 0) {
            val (leftPadding, rightPadding) = calculatePaddingValues.getOrNull(index) ?: continue

            LevelCard(i, rightPadding, leftPadding, i, currentLevel, onClick, color,colors)
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
    color: Int,
    colors: List<ColorPair>,
    isDarkTheme: Boolean = isSystemInDarkTheme(),
) {
    val upcomingLevelColor = if (isDarkTheme) Color(0xFF3B3B3D) else Color(0xFF6D6D74)

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
                    true,
                    modifier = Modifier,
                    color = colors[color].darkColor,
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
                    false,
                    modifier = Modifier,
                    color = upcomingLevelColor,
                )
            }
        }
    }
}

@Composable
fun TopBar(livesLeft: Int, streak: Int, color: Int, changeColorFun: (Int) -> Unit,colors: List<ColorPair>) {
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
            MenuColorPicker(color, changeColorFun,colors)
            Spacer(modifier = Modifier.weight(1f))
            Image(painter = painterResource(id = R.drawable.profilr_filled), contentDescription = "streak")
            Text(text = streak.toString(), modifier = Modifier.padding(start = 8.dp))
            Spacer(modifier = Modifier.weight(1f))
            Image(
                painter = painterResource(id = R.drawable.profile),
                contentDescription = "lives left"
            )
            Text(text = livesLeft.toString(), modifier = Modifier.padding(start = 8.dp))
        }
    }
}

@Composable
fun MenuColorPicker(color: Int, changeColorFun: (Int) -> Unit, colors: List<ColorPair>) {
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
            IconButton(onClick = { isExpanded = !isExpanded }) {
                Icon(
                    modifier = Modifier
                        .size(30.dp),
                    imageVector =
                    if (isExpanded) {
                        ImageVector.vectorResource(R.drawable.palette_filled)
                    } else {
                        ImageVector.vectorResource(R.drawable.palette)
                    },
                    contentDescription = stringResource(R.string.pickTheme)
                )
            }
            AnimatedVisibility(visible = isExpanded) {
                Row(
                    modifier = Modifier
                        .padding(start = 4.dp)
                        .clip(RoundedCornerShape(40.dp))
                        .border(
                            1.dp,
                            colors[color].darkColor,
                            RoundedCornerShape(40.dp)
                        )
                        .height(40.dp)
                        .wrapContentSize(),
                ) {
                    Row(modifier = Modifier.horizontalScroll(scrollState)) {
                        colors.forEach { color ->
                            Box(
                                modifier = Modifier
                                    .padding(start = 10.dp, end = 10.dp)
                                    .size(26.dp)
                                    .clip(RoundedCornerShape(26.dp))
                                    .background(color.lightColor)
                                    .clickable {
                                        changeColorFun(color.id)
                                    }
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


@Composable
fun BottomLevel(onClick: () -> Unit, level: String, color: Int, colors: List<ColorPair>) {
    val animatedColor by animateColorAsState(
        targetValue = colors[color].darkColor,
        animationSpec = tween(durationMillis = 300),
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
