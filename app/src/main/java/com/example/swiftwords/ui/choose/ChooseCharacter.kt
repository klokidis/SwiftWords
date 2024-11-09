package com.example.swiftwords.ui.choose

import android.content.Context
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import com.example.swiftwords.R
import com.example.swiftwords.ui.AppViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.swiftwords.data.DataSource
import com.example.swiftwords.ui.elements.LetterByLetterText
import com.example.swiftwords.ui.elements.ProfileImagePopUp
import kotlin.reflect.KFunction1
import kotlin.reflect.KFunction2

@Composable
fun StartingScreen(
    viewModel: StartingViewmodel = viewModel(factory = AppViewModelProvider.Factory),
    nickName: String,
    updateInitialState: KFunction1<Boolean, Unit>,
    updateName: KFunction1<String, Unit>,
    updateCharacter: KFunction1<Boolean, Unit>,
    playLetterSound: KFunction2<Char, Float, Unit>,
    profileSelected: Int,
    changeProfilePic: KFunction1<Int, Unit>,
) {
    val uiState by viewModel.uiState.collectAsState()

    val context = LocalContext.current
    var permissionGranted by remember { mutableStateOf(isNotificationPermissionGranted(context)) }
    // Launcher to request the notification permission
    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        permissionGranted = isGranted
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .clickable(
                onClick = {
                    if (uiState.dialogueState == 6) {
                        if (!permissionGranted) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                // Request permission on Android 13+
                                requestPermissionLauncher.launch("android.permission.POST_NOTIFICATIONS")
                            }
                        }
                        updateInitialState(false)
                    } else {
                        if (uiState.dialogueState < 4) {
                            viewModel.increaseState()
                        }
                    }
                },
                indication = null, // Removes the click effect
                interactionSource = remember { MutableInteractionSource() } // Required for the clickable modifier
            )
    ) {
        when (uiState.dialogueState) {
            1 -> {
                CharacterChatTwo(
                    text = stringResource(R.string.dialog1),
                    characterIsMale = false,
                    playLetterSound,
                    characterOne = R.drawable.male_full_body,
                    characterTwo = R.drawable.female_full_body
                )
            }

            2 -> {
                CharacterChat(
                    characterIsMale = true,
                    text = stringResource(R.string.dialog2),
                    playLetterSound = playLetterSound
                )
            }

            3 -> {
                CharacterChatTwo(
                    text = stringResource(R.string.dialog3),
                    characterIsMale = false,
                    playLetterSound,
                    characterOne = R.drawable.fire_on,
                    characterTwo = R.drawable.female_full_body
                )
            }

            4 -> {
                ChooseCharacter(
                    updateCharacter,
                    uiState.character,
                    viewModel::updateCharacter
                ) { viewModel.increaseState() }
            }

            5 -> {
                SetNickName(
                    nickName = nickName,
                    chose = uiState.character,
                    onSave = updateName,
                    onCancel = { viewModel.decreaseState() },
                    nextState = { viewModel.increaseState() },
                    profileSelected = profileSelected,
                    changeProfilePic = changeProfilePic,
                )
            }

            6 -> {
                CharacterChat(
                    characterIsMale = uiState.character != 0,
                    text = stringResource(R.string.dialog4),
                    playLetterSound = playLetterSound
                )
            }

            else -> {

            }
        }
    }
}


@Composable
fun CharacterChat(
    characterIsMale: Boolean,
    text: String,
    playLetterSound: KFunction2<Char, Float, Unit>
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Box(
            contentAlignment = Alignment.BottomStart
        ) {
            Image(
                painter = painterResource(
                    id = if (characterIsMale) {
                        R.drawable.male_full_body
                    } else {
                        R.drawable.female_full_body
                    }
                ),
                modifier = Modifier
                    .size(600.dp)
                    .padding(bottom = 40.dp),
                contentDescription = null,
                contentScale = ContentScale.FillHeight
            )
            Card(
                modifier = Modifier
                    .clip(MaterialTheme.shapes.medium)
                    .padding(10.dp)
                    .fillMaxWidth()
                    .shadow(
                        elevation = 2.dp,
                        shape = RoundedCornerShape(16.dp)
                    ),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                ),
            ) {
                LetterByLetterText(
                    text,
                    characterIsMale = characterIsMale,
                    playLetterSound = playLetterSound
                )
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        Text(text = stringResource(id = R.string.click))
    }
}

@Composable
fun SetNickName(
    chose: Int,
    onSave: KFunction1<String, Unit>,
    onCancel: () -> Unit,
    nextState: () -> Unit,
    nickName: String,
    profileSelected: Int,
    changeProfilePic: KFunction1<Int, Unit>,
) {
    var textState by rememberSaveable { mutableStateOf(nickName) }
    var showProfilePhotos by rememberSaveable { mutableStateOf(false) }
    var isError by remember { mutableStateOf(textState.isBlank() || textState.trim().length !in 2..15) }
    val painter = if (chose == 0) {
        painterResource(id = DataSource().profileImagesFemale[profileSelected])
    } else {
        painterResource(id = DataSource().profileImagesMale[profileSelected])
    }
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(15.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(0.3f))
        Box(
            modifier = Modifier
                .wrapContentSize()
        ) {
            Image(
                painter = painter,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(top = 15.dp)
                    .size(200.dp)
                    .clip(CircleShape)
                    .clickable { showProfilePhotos = true  },
            )
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = stringResource(R.string.edit),
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(27.dp)
                    .clip(CircleShape)
                    .clickable { showProfilePhotos = true },
            )
        }
        Spacer(modifier = Modifier.padding(20.dp))
        OutlinedTextField(
            value = textState,
            onValueChange = {
                textState = it
                isError = textState.isBlank() || textState.trim().length !in 2..15
            },
            label = { Text(stringResource(R.string.give_name)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp),
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    if (!isError) {
                        onSave(textState.trim())
                        nextState()
                    }
                }
            ),
            isError = textState.trim().length > 15,
            shape = RoundedCornerShape(20.dp)
        )
        Spacer(modifier = Modifier.weight(1f))
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            OutlinedButton(onClick = {
                onCancel()
            }) {
                Text(stringResource(R.string.cancel))
            }
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = {
                    if (!isError) {
                        onSave(textState.trim())
                        nextState()
                    }
                },
                enabled = !isError
            ) {
                Text(stringResource(R.string.save))
            }
        }
    }
    ProfileImagePopUp(
        profileSelected,
        showProfilePhotos,
        { showProfilePhotos = false },
        changeProfilePic,
        chose == 0
    )
}

@Composable
fun CharacterChatTwo(
    text: String,
    characterIsMale: Boolean,
    playLetterSound: KFunction2<Char, Float, Unit>,
    characterOne: Int,
    characterTwo: Int
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Box(
            contentAlignment = Alignment.BottomStart
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 25.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                Image(
                    painter = painterResource(characterOne),
                    modifier = Modifier
                        .weight(1f)
                        .padding(bottom = 40.dp),
                    contentDescription = null
                )
                Image(
                    painter = painterResource(characterTwo),
                    modifier = Modifier
                        .weight(1f)
                        .padding(bottom = 40.dp),
                    contentDescription = null
                )
            }
            Card(
                modifier = Modifier
                    .clip(MaterialTheme.shapes.medium)
                    .padding(10.dp)
                    .fillMaxWidth()
                    .shadow(
                        elevation = 2.dp,
                        shape = RoundedCornerShape(16.dp)
                    ),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                ),
            ) {
                LetterByLetterText(
                    text,
                    playLetterSound = playLetterSound,
                    characterIsMale = characterIsMale
                )
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        Text(text = stringResource(id = R.string.click))
    }
}


@Composable
fun ChooseCharacter(
    dataUpdate: (Boolean) -> Unit,
    character: Int,
    uiStateUpdate: KFunction1<Int, Unit>,
    increaseState: () -> Unit,
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
    ) {
        Spacer(modifier = Modifier.padding(5.dp))
        Text(
            text = stringResource(id = R.string.choose),
            style = MaterialTheme.typography.titleLarge.copy(fontSize = 45.sp),
            modifier = Modifier
                .padding(start = 10.dp)
        )
        Text(
            text = stringResource(id = R.string.choose2),
            style = MaterialTheme.typography.titleLarge.copy(fontSize = 45.sp),
            modifier = Modifier.padding(start = 10.dp)
        )
        Spacer(modifier = Modifier.padding(top = 10.dp))
        CharacterCard(
            R.drawable.female_full_body,
            character == 0,
            uiStateUpdate,
            character = 0,
            Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.padding(10.dp))
        CharacterCard(
            R.drawable.male_full_body,
            character == 1,
            uiStateUpdate,
            character = 1,
            Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.padding(top = 10.dp))
        Row(
            modifier = Modifier
                .padding(end = 25.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = {
                    if (character != 2) {
                        dataUpdate(character == 0) //true means f false mean m
                        increaseState()
                    }
                },
                enabled = character != 2
            ) {
                Text(stringResource(R.string.next))
            }
        }
    }
}

@Composable
fun CharacterCard(
    imageResourceId: Int,
    selected: Boolean,
    onClicked: KFunction1<Int, Unit>,
    character: Int,
    modifier: Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp, bottom = 5.dp)
            .clip(MaterialTheme.shapes.medium)
            .clickable(
                onClick = {
                    onClicked(character)
                },
                indication = null, // Removes the click effect
                interactionSource = remember { MutableInteractionSource() }, // Required for the clickable modifier
            ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background,
        ),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .padding(start = 10.dp)
        ) {
            RadioButton(selected = selected, onClick = { onClicked(character) })
            Spacer(modifier = Modifier.weight(1f))
            Image(
                painter = painterResource(imageResourceId),
                contentDescription = null, //no need
                modifier = Modifier
                    .fillMaxHeight(),
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

// Function to check if the notification permission is granted
fun isNotificationPermissionGranted(context: Context): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        // Check POST_NOTIFICATIONS permission on Android 13+
        ContextCompat.checkSelfPermission(
            context,
            "android.permission.POST_NOTIFICATIONS"
        ) == PermissionChecker.PERMISSION_GRANTED
    } else {
        // No permission needed on versions below Android 13
        true
    }
}
