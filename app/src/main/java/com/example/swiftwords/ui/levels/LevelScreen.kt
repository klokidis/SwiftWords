package com.example.swiftwords.ui.levels

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.example.swiftwords.R
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.swiftwords.ui.AppViewModelProvider
import com.example.swiftwords.ui.theme.SwiftWordsTheme

@Composable
fun LevelScreen(
    viewModel: LevelViewModel = androidx.lifecycle.viewmodel.compose.viewModel(factory = AppViewModelProvider.Factory),
    navigateToLevel: () -> Unit,
) {
    val levelUiState by viewModel.uiState.collectAsState()

    val scrollState = rememberScrollState()

    Box {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            val paddingChange = 50.dp
            var leftPadding = 0.dp
            var rightPadding = 0.dp
            var step = 0

            for (i in levelUiState.currentLevel - 25..levelUiState.currentLevel + 25) {
                if (i > 0) {
                    when (step) {
                        in 0..2 -> {
                            leftPadding += paddingChange
                            if (rightPadding != 0.dp) {
                                rightPadding -= paddingChange
                            }
                        }

                        3 -> {
                            step = -4
                        }

                        in -4..-1 -> {
                            rightPadding += paddingChange
                            if (leftPadding != 0.dp) {
                                leftPadding -= paddingChange
                            }
                        }
                    }
                    step += 1

                }
                if (i > 0) {
                    val modifierLevel = if (i < levelUiState.currentLevel) {
                        Modifier
                            .clip(RoundedCornerShape(50))
                            .background(Color.Yellow)
                    } else {
                        if (i == levelUiState.currentLevel) {
                            Modifier
                                .clip(RoundedCornerShape(50))
                                .background(Color.Green)
                        } else {
                            Modifier
                                .clip(RoundedCornerShape(50))
                                .background(Color.Gray)
                        }
                    }
                    LevelCard(navigateToLevel, i, modifierLevel, rightPadding, leftPadding)
                }
            }
        }
        TopBar()
    }
}

@Composable
fun LevelCard(
    navigateToLevel: () -> Unit,
    number: Int,
    modifier: Modifier,
    rightPadding: Dp,
    leftPadding: Dp
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
        Card(
            modifier = modifier
                .size(90.dp),
            onClick = navigateToLevel,
            shape = RoundedCornerShape(50),
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = modifier.fillMaxSize(),
            ) {
                Text(
                    text = number.toString(),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = Color.White
                    ),
                    fontSize = 24.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun TopBar() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        color = MaterialTheme.colorScheme.background,
        shadowElevation = 4.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                Image(painter = painterResource(id = R.drawable.done), contentDescription = null)
                Text(text = "theme", modifier = Modifier.padding(start = 8.dp))
                Spacer(modifier = Modifier.weight(1f))
                Image(painter = painterResource(id = R.drawable.done), contentDescription = null)
                Text(text = "10", modifier = Modifier.padding(start = 8.dp))
                Spacer(modifier = Modifier.weight(1f))
                Image(painter = painterResource(id = R.drawable.done), contentDescription = null)
                Text(text = "5", modifier = Modifier.padding(start = 8.dp))
            }
            HorizontalDivider(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .height(1.dp),
                color = Color.LightGray
            )
        }
    }
}



@Composable
fun BottomLevel(onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(10.dp)
            .height(50.dp)
            .fillMaxWidth(0.8f)
            .shadow(
                elevation = 2.dp,
                shape = RoundedCornerShape(16.dp)
            ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = dimensionResource(id = R.dimen.elevation),
        ),
        onClick = onClick
    ) {

    }
}

/*
@Composable
fun Level(navigateToLevel: () -> Unit,number: Int, image: Int, rightPadding: Dp, leftPadding: Dp) {
    Box(
        modifier = Modifier
            .padding(
                start = leftPadding,
                end = rightPadding,
                top = 50.dp
            ),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(image),
            contentDescription = null, //no need
            modifier = Modifier
                .size(130.dp)
                .clickable {
                    navigateToLevel.invoke()
                },
            contentScale = ContentScale.Crop
        )
        Text(
            text = number.toString(),
            style = MaterialTheme.typography.displaySmall.copy(
                color = Color.White,
                shadow = Shadow(
                    color = Color.Black,
                    offset = Offset(2f, 2f),
                    blurRadius = 4f
                )
            ),
        )
    }
}
*/
@Preview(showBackground = true)
@Composable
fun CardPreview() {
    SwiftWordsTheme {
        LevelCard({}, 1, Modifier, 19.dp, 19.dp)
    }
}

@Preview(showBackground = true)
@Composable
fun LevelPreview() {
    SwiftWordsTheme {
    }
}


