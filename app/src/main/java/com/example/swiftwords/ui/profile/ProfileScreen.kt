package com.example.swiftwords.ui.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
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
    color: Int
) {
    val scrollState = rememberScrollState()
    val painter = if(character){
        painterResource(id = R.drawable.cypher)
    }else{
        painterResource(id = R.drawable.cypher)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(15.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painter,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(160.dp)
                .clip(CircleShape)
                //.border(2.dp, DataSource().colorPairs[color].darkColor, CircleShape)//optional
        )
        Spacer(modifier = Modifier.padding(4.dp))
        Text(
            text = name,
            style = MaterialTheme.typography.titleSmall.copy(fontSize = 35.sp, letterSpacing = 1.sp)
        )
        Spacer(modifier = Modifier.padding(20.dp))
        Scores(currentLevel, highScore, streak)
    }
}

@Composable
fun Scores(currentLevel: Int, highScore: Int, streak: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),

        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        TextScores("Current level", currentLevel.toString())
        Spacer(modifier = Modifier.padding(15.dp))
        TextScores("High Score", highScore.toString())
        Spacer(modifier = Modifier.padding(15.dp))
        TextScores("daily streak", streak.toString())
    }
}

@Composable
fun TextScores(content: String,score: String){
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = content,
            style = MaterialTheme.typography.titleSmall.copy(fontSize = 16.sp)
        )
        Text(
            text = score,
            style = MaterialTheme.typography.titleMedium.copy(fontSize = 20.sp)
        )
    }
}
/*


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
        ProfileScreen(20, 1, 2, "dimitris", true, 1)
    }
}