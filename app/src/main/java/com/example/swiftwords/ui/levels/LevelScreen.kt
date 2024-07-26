package com.example.swiftwords.ui.levels

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.swiftwords.R
import com.example.swiftwords.SwiftWordsScreen
import com.example.swiftwords.ui.theme.SwiftWordsTheme

@Composable
fun LevelScreen(navigateToLevel: () -> Unit) {
    val scrollState = rememberScrollState()

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

        for (i in 0..50) {
            if (i != 0){
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

            val image = when (i) {
                0 -> R.drawable.done
                1 -> R.drawable.current
                else -> R.drawable.locked
            }

            Level(navigateToLevel,i, image, rightPadding, leftPadding)
        }
    }
}

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


