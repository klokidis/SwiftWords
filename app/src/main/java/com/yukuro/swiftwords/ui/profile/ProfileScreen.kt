package com.yukuro.swiftwords.ui.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yukuro.swiftwords.R
import com.yukuro.swiftwords.data.DataSource
import com.yukuro.swiftwords.ui.elements.ProfileImagePopUp
import com.yukuro.swiftwords.ui.theme.SwiftWordsTheme

@Composable
fun ProfileScreen(
    currentLevel: Int,
    streak: Int,
    highScore: Int,
    name: String,
    character: Boolean,
    pictureId: Int,
    colorBoarder: Int,
    navigate: () -> Unit,
    changeProfilePic: (Int) -> Unit,
) {
    val scrollState = rememberScrollState()
    var showProfilePhotos by remember { mutableStateOf(false) }
    val painter = if (character) {
        painterResource(id = DataSource().profileImagesFemale[pictureId])//true for f
    } else {
        painterResource(id = DataSource().profileImagesMale[pictureId])//false for m
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
                    .size(200.dp)
                    .clip(CircleShape)
                    .clickable { showProfilePhotos = true },
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
            Spacer(modifier = Modifier.weight(1f))
            WalkingFiresRow(
                loopNumber = DataSource().streakCalculation(streak),
                fires = DataSource().firesWalk
            )
        }
        IconButton(
            onClick = { navigate() },
            modifier = Modifier
                .align(Alignment.TopEnd)
        ) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = stringResource(id = R.string.settings),
                modifier = Modifier.size(28.dp)
            )
        }
    }
    ProfileImagePopUp(
        pictureId,
        showProfilePhotos,
        { showProfilePhotos = false },
        changeProfilePic,
        character,
        DataSource().colorPairs[colorBoarder].darkColor
    )
}

@Composable
private fun WalkingFiresRow(loopNumber: Int, fires: List<Int>) {
    var boubleClicked by remember { mutableIntStateOf(0) }
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment =Alignment.End
    ) {
        if (boubleClicked == 1 || boubleClicked ==0) {
            Text(
                text = "Hello! I'm on fire!", // Funny message
                color = Color.White,
                modifier = Modifier
                    .padding(10.dp)
                    .background(
                        color = Color.DarkGray,
                        shape = RoundedCornerShape(8.dp)
                    )
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 3.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {

            for (i in loopNumber downTo 0) { // Loop in reverse order
                Box(
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(end = 5.dp)
                        .clickable {
                            boubleClicked = i
                        }
                ) {
                    Image(
                        painter = painterResource(id = fires[i]),
                        contentDescription = null,
                        modifier = Modifier.size(40.dp)
                    )
                }
            }
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
            style = MaterialTheme.typography.titleSmall.copy(fontSize = 30.sp)
        )
        Text(
            text = content,
            style = MaterialTheme.typography.titleSmall.copy(fontSize = 20.sp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProfilePreview() {
    SwiftWordsTheme {
        val changeProfilePic: (Int) -> Unit = { newPicId ->
            // For demonstration purposes, printing the new picture ID
            println("Profile picture changed to ID: $newPicId")
        }

        SwiftWordsTheme {
            ProfileScreen(
                currentLevel = 20,
                streak = 1,
                highScore = 2,
                name = "dimitris",
                character = true,
                pictureId = 1,
                colorBoarder = 2,
                navigate = { },
                changeProfilePic = changeProfilePic // Pass the function here
            )
        }
    }
}