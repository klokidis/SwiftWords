package com.example.swiftwords.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.swiftwords.R
import com.example.swiftwords.data.DataSource
import com.example.swiftwords.data.ItemDetailsUiState

@Composable
fun SettingsPage(
    navigateOut: () -> Unit,
    data: ItemDetailsUiState,
) {
    Box {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            Spacer(modifier = Modifier.padding(18.dp))
            DataSource().settingsList.forEach { setting ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 25.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(
                        text = setting,
                        style = MaterialTheme.typography.titleSmall.copy(fontSize = 22.sp)
                    )
                }
            }
        }
        IconButton(
            onClick = { navigateOut() },
            modifier = Modifier
                .align(Alignment.TopStart).padding(start = 5.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack, // Use the appropriate icon
                contentDescription = stringResource(id = R.string.settings),
                modifier = Modifier.size(30.dp)
            )
        }
    }
}