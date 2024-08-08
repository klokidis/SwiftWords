package com.example.swiftwords.ui.choose

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.swiftwords.R
import com.example.swiftwords.data.DataViewmodel
import com.example.swiftwords.ui.AppViewModelProvider

import com.example.swiftwords.ui.theme.SwiftWordsTheme

@Composable
fun ChooseCharacter(viewmodel: DataViewmodel = viewModel(factory = AppViewModelProvider.Factory)) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
    ) {
        Spacer(modifier = Modifier.padding(20.dp))
        Text(
            text = stringResource(id = R.string.choose),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .padding(start = 10.dp)
        )
        Spacer(modifier = Modifier.padding(3.dp))
        Text(
            text = stringResource(id = R.string.choose2),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(start = 10.dp)
        )

        Spacer(modifier = Modifier.padding(40.dp))

    }
}

@Composable
fun CompleteCard(
    imageResourceId: Int,
    name: String,
    selected: Boolean
) {
    Box(
        modifier = Modifier.height(dimensionResource(id = R.dimen.artist_box))
    ) {
        CharacterCard(
            { },
            name,
            selected
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(end = 30.dp, bottom = 19.dp),
            contentAlignment = Alignment.TopEnd
        ) {
            Image(
                painter = painterResource(imageResourceId),
                contentDescription = null, //no need
                modifier = Modifier
                    .fillMaxHeight()
                    .width(150.dp),
                contentScale = ContentScale.Fit
            )
        }
    }
}

@Composable
fun CharacterCard(onButtonCard: () -> Unit, name: String, selected: Boolean) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .height(170.dp)
            .shadow(2.dp, shape = RoundedCornerShape(16.dp))
            .clip(MaterialTheme.shapes.medium)
            .clickable {
            },
        onClick = onButtonCard,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.inverseOnSurface,
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp,
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxHeight()
                .padding(start = 10.dp)
        ) {
            RadioButton(selected = selected, onClick = {  })
            Text(
                text = name,
                modifier = Modifier
                    .padding(
                        start = 10.dp,
                        end = 120.dp
                    ),
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SwiftWordsTheme {
        ChooseCharacter()
    }
}