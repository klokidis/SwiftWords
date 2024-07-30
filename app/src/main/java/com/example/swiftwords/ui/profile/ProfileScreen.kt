package com.example.swiftwords.ui.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.swiftwords.R
import com.example.swiftwords.ui.theme.SwiftWordsTheme

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
/*
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
                    id = R.drawable.done
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
    modifier: Modifier = Modifier,
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
    Column(modifier = modifier.padding(10.dp)) {
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
*/

@Preview(showBackground = true)
@Composable
fun ProfilePreview() {
    SwiftWordsTheme {
        ProfileScreen()
    }
}