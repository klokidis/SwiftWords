package com.example.swiftwords.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.swiftwords.R
import com.example.swiftwords.ui.theme.SwiftWordsTheme

@Composable
fun LevelScreen(){
    val scrollState = rememberScrollState()
    val animatedColor by animateColorAsState(
        MaterialTheme.colorScheme.background,
        label = "color"
    )
    var levelCount = 0
    var rightPadding = 0.dp
    var leftPadding = 0.dp
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .drawBehind {
                drawRect(animatedColor)
            },
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var image: Int

        for (i in 0..10) {
            when (i) {
                in 1..3 -> rightPadding += 40.dp
                in 4..6 -> {
                    leftPadding += 40.dp
                    rightPadding -= 40.dp
                }
                in 7..9 -> {
                    leftPadding -= 40.dp
                    rightPadding += 40.dp
                }
                10 -> {
                    leftPadding += 40.dp
                    rightPadding -= 40.dp
                }
            }

            image = when (i) {
                0 -> R.drawable.done
                1 -> R.drawable.current
                else -> R.drawable.locked
            }

            Level(i, image, levelCount, rightPadding, leftPadding)
        }
    }
}

@Composable
fun Level(number : Int, image : Int, levelCount: Int, rightPadding: Dp, leftPadding: Dp){
    Box(
        modifier = Modifier
            .padding(
                start = leftPadding,
                end = rightPadding,
                top = 50.dp
            ),
        contentAlignment = Alignment.Center
    ){
        Image(
            painter = painterResource(image),
            contentDescription = null, //no need
            modifier =  Modifier
                .size(130.dp),
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

@Preview(showBackground = true)
@Composable
fun LevelPreview() {
    SwiftWordsTheme {
        LevelScreen()
    }
}

