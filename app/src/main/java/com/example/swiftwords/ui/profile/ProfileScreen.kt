package com.example.swiftwords.ui.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.swiftwords.R
import com.example.swiftwords.ui.theme.SwiftWordsTheme

@Composable
fun ProfileScreen(
    currentLevel: Int,
    streak: Int,
    highScore: Int,
    name: String,
    character: Boolean,
    navigate: () -> Unit
) {
    val scrollState = rememberScrollState()
    val painter = if (character) {
        painterResource(id = R.drawable.cypher)//true for f
    } else {
        painterResource(id = R.drawable.controller)//false for m
    }
    Box(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painter,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(top = 15.dp)
                    .size(160.dp)
                    .clip(CircleShape)
                //.border(2.dp, DataSource().colorPairs[color].darkColor, CircleShape)//optional
            )
            Spacer(modifier = Modifier.padding(4.dp))
            Text(
                text = name,
                style = MaterialTheme.typography.titleSmall.copy(
                    fontSize = 35.sp,
                    letterSpacing = 1.5.sp
                )
            )
            Spacer(modifier = Modifier.padding(13.dp))
            Scores(currentLevel, highScore, streak)
        }
        IconButton(
            onClick = { navigate() },
            modifier = Modifier
                .align(Alignment.TopEnd)
        ) {
            Icon(
                imageVector = Icons.Default.Settings, // Use the appropriate icon
                contentDescription = stringResource(id = R.string.settings),
                modifier = Modifier.size(28.dp)
            )
        }
    }
}

@Composable
fun Scores(currentLevel: Int, highScore: Int, streak: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 7.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        TextScores(stringResource(R.string.level_profile), currentLevel.toString())
        TextScores(stringResource(R.string.high_score), highScore.toString())
        TextScores(stringResource(R.string.streak), streak.toString())
    }
}

@Composable
fun TextScores(content: String, score: String) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = score,
            style = MaterialTheme.typography.titleSmall.copy(fontSize = 25.sp)
        )
        Text(
            text = content,
            style = MaterialTheme.typography.titleSmall.copy(fontSize = 25.sp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProfilePreview() {
    SwiftWordsTheme {
        ProfileScreen(
            20,
            1,
            2,
            "dimitris",
            true
        ) { }
    }
}