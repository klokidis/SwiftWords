package com.example.swiftwords.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.swiftwords.R
import com.example.swiftwords.ui.theme.SwiftWordsTheme


@Composable
fun ModesScreen() {
    val scrollState = rememberScrollState()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
            .verticalScroll(scrollState),
        contentAlignment = Alignment.Center // Center content within the Box
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row {
                ModeCard(
                    imageRes = R.drawable.done,
                    textRes = R.string.app_name,
                    onClick = { },
                    modifier = Modifier.weight(1f)
                )
                ModeCard(
                    imageRes = R.drawable.done,
                    textRes = R.string.app_name,
                    onClick = { },
                    modifier = Modifier.weight(1f)
                )
            }
            Row {
                ModeCard(
                    imageRes = R.drawable.done,
                    textRes = R.string.app_name,
                    onClick = { },
                    modifier = Modifier.weight(1f)
                )
                ModeCard(
                    imageRes = R.drawable.done,
                    textRes = R.string.app_name,
                    onClick = { },
                    modifier = Modifier.weight(1f)
                )
            }
            DailyCard({ })
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModeCard(
    imageRes: Int,
    textRes: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.inverseOnSurface,
        ),
        modifier = modifier
            .padding(10.dp)
            .height(230.dp)
            .shadow(
                elevation = 2.dp,
                shape = RoundedCornerShape(16.dp)
            ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = dimensionResource(id = R.dimen.elevation),
        ),
        onClick = onClick
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(10.dp)
                .fillMaxSize()
        ) {
            Image(
                painter = painterResource(imageRes),
                contentDescription = null,
                modifier = Modifier.height(110.dp)
            )
            Text(text = stringResource(textRes))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DailyCard(
    onButtonCard: () -> Unit,
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.inverseOnSurface,
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp,
        ),
        modifier = Modifier
            .clip(MaterialTheme.shapes.medium)
            .height(180.dp)
            .fillMaxWidth()
            .padding(
                top = 15.dp,
                start = 10.dp,
                end = 10.dp,
                bottom = 10.dp
            )
            .shadow(
                elevation = 2.dp,
                shape = RoundedCornerShape(16.dp)
            ),
        onClick = { },
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
        ) {
            Image(
                painter = painterResource(R.drawable.done),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .height(110.dp)
            )
            Text(text = stringResource(R.string.app_name))
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ModesPreview() {
    SwiftWordsTheme {
        ModesScreen()
    }
}