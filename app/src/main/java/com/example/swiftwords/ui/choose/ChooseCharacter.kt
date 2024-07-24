package com.example.swiftwords.ui.choose

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.swiftwords.R
import com.example.swiftwords.ui.theme.SwiftWordsTheme

@Composable
fun ChooseCharacter(){
    val scrollState = rememberScrollState()
    Column{
        Spacer(modifier = Modifier.padding(20.dp))
        Text(
            text = "CHOOSE",
            modifier = Modifier.padding(start = 10.dp)
        )
        Spacer(modifier = Modifier.padding(3.dp))
        Text(
            text = "YOUR CHARACTER",
            modifier = Modifier.padding(start = 10.dp)
        )
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ){
        Spacer(modifier = Modifier.weight(1f))
        CompleteCard(R.drawable.omen)
        Spacer(modifier = Modifier.padding(20.dp))
        CompleteCard(R.drawable.sage)
        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
fun CompleteCard(
    imageResourceId: Int
){
    Box(
        modifier = Modifier.height(200.dp)//dimensionResource(id = R.dimen.artist_box)
    ) {
        CharacterCard(
            {  },
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
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterCard(onButtonCard: () -> Unit){
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
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .fillMaxHeight()
                .padding(top = 20.dp, start = 10.dp)
        ) {
            Text(
                text = "dsads",//LocalContext.current.getString(artist.stringResourceId),
                modifier = Modifier
                    .padding(
                        start = 10.dp,
                        end = 120.dp
                    ),
                style = MaterialTheme.typography.titleLarge,
            )
            Text(
                text = "dsads",//LocalContext.current.getString(artist.stringResourceId),
                modifier = Modifier
                    .padding(
                        start = 10.dp,
                        end = 120.dp
                    ),
                style = MaterialTheme.typography.titleLarge,
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