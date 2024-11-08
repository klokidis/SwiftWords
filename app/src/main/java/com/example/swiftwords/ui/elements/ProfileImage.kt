package com.example.swiftwords.ui.elements

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.swiftwords.R
import com.example.swiftwords.data.DataSource

@Composable
fun ProfileImage(
    selectedImageId: Int,
    isVisible: Boolean,
    onCancel: () -> Unit,
    onSelect: () -> Unit,
    isCharacterFemale: Boolean
) {
    val listOfCharacter = if (isCharacterFemale) DataSource().profileImagesFemale else DataSource().profileImagesMale
    if (isVisible) {
        Dialog(
            onDismissRequest = onCancel
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2), // 2 columns
                modifier = Modifier
                    .wrapContentSize(),
                contentPadding = PaddingValues(8.dp),
                horizontalArrangement = Arrangement.Center,
                verticalArrangement = Arrangement.Center,
            ) {
                items(listOfCharacter.size) { index ->
                    Image(
                        painter = painterResource(id = listOfCharacter[index]),
                        contentDescription = stringResource(R.string.profile_drawings),
                        modifier = Modifier
                            .padding(8.dp)
                            .clip(CircleShape)
                            .clickable { onSelect() }
                            .border(
                                width = if (selectedImageId == index) 2.dp else 0.dp,
                                color = if (selectedImageId == index) MaterialTheme.colorScheme.primary else Color.Transparent,
                                shape = CircleShape
                            )
                    )
                }
            }
        }
    }
}

