package com.example.swiftwords.ui

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
fun Scores(){
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

@Preview(showBackground = true)
@Composable
fun ProfilePreview() {
    SwiftWordsTheme {
        ProfileScreen()
    }
}