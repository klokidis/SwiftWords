package com.example.swiftwords.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import com.example.swiftwords.data.DataSource
import com.example.swiftwords.ui.theme.SwiftWordsTheme


@Composable
fun ModesScreen(){
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .verticalScroll(scrollState)
    ){
        FourModesScreen()
        DailyCard({ })
    }
}

@Composable
fun FourModesScreen() {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2), //Cards per row
        modifier = Modifier
            .padding(
                start = 10.dp,
                end = 10.dp,
                top = 10.dp
            )
            .height(420.dp)
            .wrapContentSize()
    ) {
        items(DataSource().loadModes()) { thisMode ->
            ModeCard(
                {  },
                thisMode.imageResourceId,
                thisMode.stringResourceId
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModeCard(
    onButtonCard: () -> Unit,
    image: Int,
    name: Int,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.inverseOnSurface,
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = dimensionResource(id = R.dimen.elevation),
        ),
        modifier = modifier
            .clip(MaterialTheme.shapes.medium)
            .height(210.dp)
            .fillMaxWidth()
            .padding(
                top = 22.dp,
                start = 10.dp,
                end = 10.dp,
                bottom = 7.dp
            )
            .shadow(
                elevation = 2.dp,
                shape = RoundedCornerShape(16.dp)
            ),
        onClick = onButtonCard,
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                painter = painterResource(image),
                contentDescription = null
            )
            Text(text = stringResource(name))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DailyCard(
    onButtonCard: () -> Unit,
){
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
                top = 22.dp,
                start = 20.dp,
                end = 20.dp,
                bottom = 10.dp
            )
            .shadow(
                elevation = 2.dp,
                shape = RoundedCornerShape(16.dp)
            ),
        onClick = { },
    ){
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