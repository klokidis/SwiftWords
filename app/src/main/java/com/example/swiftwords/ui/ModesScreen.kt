package com.example.swiftwords.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.swiftwords.R
import com.example.swiftwords.data.DataSource
import com.example.swiftwords.ui.theme.SwiftWordsTheme

@Composable
fun ModesScreen(){
    LazyVerticalGrid(
        columns = GridCells.Fixed(2), //Cards per row
        modifier = Modifier
            .padding(start = 10.dp, end = 10.dp, top = 10.dp)
            .fillMaxSize()
    ) {
        items(DataSource().loadModes()) { thisMode ->
            CharacterCard(
                {  },

            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModeCard(onButtonCard: () -> Unit, modifier: Modifier = Modifier) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.inverseOnSurface,
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp,
        ),
        modifier = modifier
            .clip(MaterialTheme.shapes.medium)
            .height(150.dp)
            .fillMaxWidth()
            .padding(
                top = 30.dp,
                start = 10.dp,
                end = 10.dp,
            )
            .shadow(elevation = 2.dp, shape = RoundedCornerShape(16.dp)),

        onClick = onButtonCard,
    ) {
    }
}


@Preview(showBackground = true)
@Composable
fun ModesPreview() {
    SwiftWordsTheme {
        ModesScreen()
    }
}