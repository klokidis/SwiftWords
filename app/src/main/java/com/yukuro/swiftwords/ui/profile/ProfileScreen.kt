package com.yukuro.swiftwords.ui.profile

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yukuro.swiftwords.R
import com.yukuro.swiftwords.data.DataSource
import com.yukuro.swiftwords.ui.AppViewModelProvider
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
    profileViewModel: ProfileViewmodel = viewModel(factory = AppViewModelProvider.Factory),
) {
    val uiState by profileViewModel.uiState.collectAsState()
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
            if (streak > 0) {
                WalkingFiresRow(
                    loopNumber = profileViewModel.streakCalculation(streak),
                    firesWalk = { uiState.firesWalk },
                    visible = { uiState.visible },
                    changeVisible = profileViewModel::changeVisible,
                    boublePadding = profileViewModel::boublePadding,
                    getText = profileViewModel::getTextBouble,
                    fires = { uiState.fires }
                )
            }
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
private fun WalkingFiresRow(
    loopNumber: Int,
    firesWalk: () -> List<Int>,
    visible: () -> Boolean,
    changeVisible: () -> Unit,
    boublePadding: (Int) -> Dp,
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    getText: (Int) -> Int,
    fires: () -> List<Int>,
) {
    var boubleClicked by remember { mutableIntStateOf(0) }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.End
    ) {
        AnimatedVisibility(
            visible = visible(),
            enter = fadeIn(animationSpec = tween(durationMillis = 200)),
            exit = fadeOut(animationSpec = tween(durationMillis = 2500))
        ) {
            Card(
                modifier = Modifier
                    .wrapContentSize()
                    .clip(MaterialTheme.shapes.medium)
                    .padding(bottom = 10.dp, end = boublePadding(boubleClicked))
                    .shadow(
                        elevation = 2.dp,
                        shape = RoundedCornerShape(16.dp)
                    ),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                ),
            ) {
                Text(
                    text = stringResource(getText(boubleClicked)),
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 18.sp),
                    color = if (isDarkTheme) {
                        Color.White
                    } else {
                        Color.Black
                    },
                    modifier = Modifier
                        .padding(6.dp),
                    textAlign = TextAlign.Center
                )
            }
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
                        .clickable(
                            onClick = {
                                boubleClicked = i
                                changeVisible()
                            },
                            indication = null, // Removes the click effect
                            interactionSource = remember { MutableInteractionSource() } // Required for the clickable modifier
                        ),
                ) {
                    Image(
                        painter = painterResource(
                            id = if (visible() && i == boubleClicked) fires()[i] else firesWalk()[i]
                        ),
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
                changeProfilePic = changeProfilePic, // Pass the function here
            )
        }
    }
}