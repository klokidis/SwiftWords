package com.example.swiftwords.ui.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.swiftwords.R
import com.example.swiftwords.ui.theme.SwiftWordsTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen() {

    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(15.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.done),
            contentDescription = null,
        )
        Spacer(modifier = Modifier.padding(7.dp))
        Text(
            text = "Name"
        )
        Spacer(modifier = Modifier.padding(20.dp))
        Scores()
    }
    CharacterChat()
}

@Composable
fun Scores() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "Current level"
        )
        Spacer(modifier = Modifier.padding(5.dp))
        Text(
            text = "High Score"
        )
        Spacer(modifier = Modifier.padding(5.dp))
        Text(
            text = "daily streak"
        )
        Spacer(modifier = Modifier.padding(5.dp))
        Text(
            text = "Name"
        )
    }
}

@Composable
fun CharacterChat() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.4f)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            contentAlignment = Alignment.BottomStart
        ) {
            Image(
                painter = painterResource(
                    id = R.drawable.omen
                ),
                modifier = Modifier
                    .size(400.dp),
                contentDescription = null
            )
            Card(
                modifier = Modifier
                    .clip(MaterialTheme.shapes.medium)
                    .padding(10.dp)
                    .fillMaxWidth()
                    .shadow(
                        elevation = 2.dp,
                        shape = RoundedCornerShape(16.dp)
                    ),
            ) {
                LetterByLetterText("dsndjsan sadjsad sadjisajdia iasjdiajaids ijdsjadiasj asidjsadja doijsad djasd dsad jndjdsandjandkasjnd adsnjdnsjdnjas dasjndnasj  njdanasj  njdsanjd djand jdna")
            }
        }
    }

}

@Composable
fun LetterByLetterText(
    text: String,
    modifier: Modifier = Modifier.padding(10.dp),
    textStyle: TextStyle = TextStyle.Default
) {
    var visibleText by rememberSaveable { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            animateText(text) { newText ->
                visibleText = newText
            }
        }
    }

    // Display the text with wrapping
    Column(modifier = modifier) {
        var lineWidth by remember{ mutableStateOf(0.dp) } //CHANGE TO VIEWMODEL

        Text(
            text = visibleText,
            style = textStyle,
            modifier = Modifier
                .onGloballyPositioned {
                    lineWidth = it.size.width.dp
                }
                .padding(end = 4.dp) // Extra padding to ensure text doesn't wrap too tightly
        )
    }
}

private suspend fun animateText(text: String, callback: (String) -> Unit) {
    for (i in text.indices) {
        delay(50) // Delay between each letter
        callback(text.substring(0, i + 1))
    }
}


@Preview(showBackground = true)
@Composable
fun ProfilePreview() {
    SwiftWordsTheme {
        ProfileScreen()
    }
}