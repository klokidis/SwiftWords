package com.example.swiftwords.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.swiftwords.ui.theme.SwiftWordsTheme

@Composable
fun Game(listOfLetters : List<String>) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            RowOfLetters(listOfLetters[0],listOfLetters[1],listOfLetters[2])
            RowOfLetters(listOfLetters[3],listOfLetters[4],listOfLetters[5])
            RowOfLetters(listOfLetters[6],listOfLetters[7],listOfLetters[8])
        }
    }
}

@Composable
fun RowOfLetters(letter1 : String, letter2 : String, letter3 : String) {
    Row(
        modifier = Modifier
            .wrapContentSize()
    ){
        LetterBox(letter1)
        LetterBox(letter2)
        LetterBox(letter3)
    }
}

@Composable
fun LetterBox(letter : String){
    Card(
        modifier = Modifier
            .padding(5.dp)
            .size(50.dp)
            .shadow(2.dp, shape = RoundedCornerShape(15.dp))
            .clip(MaterialTheme.shapes.medium)
            .clickable {
            },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.inverseOnSurface,
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp,
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,){
            Text(text = letter)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GamePreview() {
    SwiftWordsTheme {
        Game(listOf("A","B","C","D","E","F","G","H","O"))
    }
}