package com.example.swiftwords.ui.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.swiftwords.R
import com.example.swiftwords.data.DataSource
import com.example.swiftwords.ui.elements.ProfileImagePopUp
import com.example.swiftwords.ui.elements.brighten
import kotlin.reflect.KFunction1

@Composable
fun SettingsPage(
    navigateOut: () -> Unit,
    updateTime: KFunction1<Long, Unit>,
    changeCharacter: KFunction1<Boolean, Unit>,
    introduction: KFunction1<Boolean, Unit>,
    changeName: KFunction1<String, Unit>,
    dataColor: Int,
    nickname: String,
    character: Boolean,
    levelTime: Long,
    profileSelected: Int,
    changeProfilePic: KFunction1<Int, Unit>,
) {
    val color by remember { mutableStateOf(DataSource().colorPairs[dataColor].darkColor) }
    val scrollState = rememberScrollState()
    var displayEdit by rememberSaveable { mutableStateOf(false) }
    var newName by remember { mutableStateOf(TextFieldValue(nickname)) }
    var showProfilePhotos by remember { mutableStateOf(false) }

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
                    contentDescription = stringResource(id = R.string.settings),
                    modifier = Modifier.size(30.dp)
                )
            }
            Spacer(modifier = Modifier.weight(0.65f))
            Text(
                text = "Setting",
                style = MaterialTheme.typography.titleSmall.copy(fontSize = 35.sp)
            )
            Spacer(modifier = Modifier.weight(1f))
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            Spacer(modifier = Modifier.padding(10.dp))
            OneSettingMenuLong(
                stringResource(R.string.level_time),
                listOf(35000L, 40000L, 50000L, 60000L, 70000L),
                updateTime,
                levelTime,
                color
            )
            Spacer(modifier = Modifier.padding(5.dp))
            OneSettingMenuStrings(
                stringResource(R.string.change_character),
                listOf(stringResource(R.string.female), stringResource(R.string.male)),
                changeCharacter,
                character,
                color
            )
            Spacer(modifier = Modifier.padding(5.dp))
            OneSettingSimple(stringResource(R.string.introdacton), introduction)
            Spacer(modifier = Modifier.padding(5.dp))
            OneSettingPopUp(
                stringResource(R.string.edit_name),
                onClick = { displayEdit = true }
            )
            Spacer(modifier = Modifier.padding(5.dp))
            OneSettingSimple(
                stringResource(R.string.change_profile),
                onClick = { showProfilePhotos = it }
            )
        }
    }
    if (displayEdit) {
        Dialog(onDismissRequest = { displayEdit = false }) {
            NewNamePopUp(
                newName = newName,
                onNameChange = { newName = it },
                onSave = {
                    changeName(newName.text.trim()) // Save the new name
                    displayEdit = false // Close the dialog
                },
                onCancel = { displayEdit = false },
                isError = (newName.text.isBlank() || (newName.text.trim().length) !in 2..15),
                color = color
            )
        }
    }
    ProfileImagePopUp(
        profileSelected,
        showProfilePhotos,
        { showProfilePhotos = false },
        changeProfilePic,
        character
    )
}

@Composable
fun NewNamePopUp(
    newName: TextFieldValue,
    onNameChange: (TextFieldValue) -> Unit,
    onSave: () -> Unit,
    onCancel: () -> Unit,
    isError: Boolean,
    color: Color,
    isDarkTheme: Boolean = isSystemInDarkTheme(),
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            MaterialTheme.colorScheme.secondary
        ),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = stringResource(R.string.enter_new_name),
                style = MaterialTheme.typography.titleSmall.copy(fontSize = 25.sp),
                color = if (isDarkTheme) Color.White else Color.Black
            )

            Spacer(modifier = Modifier.padding(8.dp))

            OutlinedTextField(
                value = newName,
                onValueChange = onNameChange,
                label = { Text(stringResource(R.string.new_name)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {
                        if (!isError) {
                            onSave()
                        }
                    }
                ),
                isError = isError
            )

            Spacer(modifier = Modifier.padding(8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Spacer(modifier = Modifier.weight(1f))
                TextButton(
                    onClick = onCancel,
                ) {
                    Text(stringResource(R.string.cancel), color = color)
                }
                Spacer(modifier = Modifier.weight(1f))
                TextButton(
                    onClick = onSave,
                    enabled = !isError,
                ) {
                    Text(
                        stringResource(R.string.save),
                        color = if (!isError) {
                            color
                        } else {
                            Color.DarkGray.brighten()
                        }
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}


@Composable
fun OneSettingSimple(text: String, onClick: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(true) }
            .size(60.dp)
            .padding(start = 15.dp, end = 15.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleSmall.copy(fontSize = 25.sp)
        )
    }
}

@Composable
fun OneSettingPopUp(text: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .size(60.dp)
            .padding(start = 15.dp, end = 15.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleSmall.copy(fontSize = 25.sp)
        )
    }
}


@Composable
fun OneSettingMenuStrings(
    text: String,
    options: List<String>,
    updateCharacter: (Boolean) -> Unit,
    isFemale: Boolean,
    color: Color
) {
    SettingMenu(
        text = text,
        selectedOption = if (isFemale) stringResource(R.string.female) else stringResource(R.string.male),
        options = options,
        onOptionSelected = { updateCharacter(it == "Female") },
        displayOption = { it },
        displaySelected = { it },
        color = color
    )
}

@Composable
fun OneSettingMenuLong(
    text: String,
    options: List<Long>,
    updateTime: (Long) -> Unit,
    levelTime: Long,
    color: Color
) {
    SettingMenu(
        text = text,
        selectedOption = levelTime,
        options = options,
        onOptionSelected = updateTime,
        displayOption = { time ->
            time.toString().take(2)
        }, // Only display the number in the menu
        displaySelected = { time ->
            time.toString().take(2) + " " + stringResource(R.string.seconds)
        }, // Display with "seconds" only for the selected value
        width = 50.dp,
        color = color
    )
}

@Composable
fun <T> SettingMenu(
    text: String,
    selectedOption: T,
    options: List<T>,
    onOptionSelected: (T) -> Unit,
    displayOption: (T) -> String,
    displaySelected: @Composable (T) -> String,
    modifier: Modifier = Modifier,
    width: Dp = 100.dp,
    color: Color
) {
    var expanded by remember { mutableStateOf(false) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { expanded = true }
            .size(60.dp)
            .padding(horizontal = 15.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleSmall.copy(fontSize = 25.sp)
        )
        Spacer(modifier = Modifier.weight(1f))
        Box {
            TextButton(onClick = { expanded = true }) {
                Text(
                    text = displaySelected(selectedOption), // Show the selected value with the "seconds"
                    style = MaterialTheme.typography.titleSmall.copy(fontSize = 20.sp),
                    color = color
                )
            }
            DropdownMenu(
                expanded = expanded,
                modifier = Modifier.width(width),
                onDismissRequest = { expanded = false }
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        onClick = {
                            onOptionSelected(option)
                            expanded = false
                        },
                        text = {
                            Text(
                                displayOption(option), // Show options without "seconds"
                                style = MaterialTheme.typography.titleSmall.copy(fontSize = 17.sp)
                            )
                        }
                    )
                }
            }
        }
    }
}
