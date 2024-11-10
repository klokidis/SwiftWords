package com.example.swiftwords.ui.credits

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.swiftwords.R
import com.example.swiftwords.data.DataSource

@Composable
fun CreditsScreen(
    navigateOut: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            IconButton(
                onClick = { navigateOut() },
                modifier = Modifier
                    .padding(start = 5.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack, // Use the appropriate icon
                    contentDescription = stringResource(id = R.string.back),
                    modifier = Modifier.size(30.dp)
                )
            }
            Spacer(modifier = Modifier.weight(0.65f))
            Text(
                text = stringResource(R.string.credits),
                style = MaterialTheme.typography.titleSmall.copy(fontSize = 35.sp)
            )
            Spacer(modifier = Modifier.weight(1f))
        }
        Row {
            Text(
                modifier = Modifier.padding(10.dp),
                text = stringResource(R.string.artist_credit),
                style = MaterialTheme.typography.titleSmall.copy(fontSize = 20.sp)
            )
            Spacer(modifier = Modifier.weight(1f))
        }
        ImagesCreditList(DataSource().allImagesCredit)
    }
}

@Composable
fun ImageItem(imageRes: Int) {
    Image(
        painter = painterResource(id = imageRes),
        contentDescription = null,
        modifier = Modifier
            .size(400.dp)
    )
}

@Composable
fun ImagesCreditList(allImagesCredit: List<Int>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(allImagesCredit) { imageRes ->
            ImageItem(imageRes = imageRes)
        }
    }
}
