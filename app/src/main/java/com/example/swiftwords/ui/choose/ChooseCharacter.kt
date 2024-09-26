package com.example.swiftwords.ui.choose

import android.content.Context
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
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
import com.example.swiftwords.data.GetDataViewModel
import com.example.swiftwords.ui.elements.LetterByLetterText
import com.example.swiftwords.ui.elements.SoundViewModel
import kotlin.reflect.KFunction1

@Composable
fun StartingScreen(
    dataViewmodel: GetDataViewModel,
    viewModel: StartingViewmodel = viewModel(factory = AppViewModelProvider.Factory),
    soundViewModel: SoundViewModel,
    nickName: String,
) {
    val uiState by viewModel.uiState.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .clickable(
                onClick = {
                    if (uiState.dialogueState == 6) {
                        dataViewmodel.updateInitialState()
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
                CharacterChat(
                    characterIsMale = true,
                    text = "jsdnjsndjsndjs ddasd ds dasd  adsa  dsa sdas das  dsdsadas",
                    soundViewModel = soundViewModel
                )
            }

            2 -> {
                CharacterChat(
                    characterIsMale = false, text = stringResource(R.string.click),
                    soundViewModel = soundViewModel
                )
            }

            3 -> {
                CharacterChatTwo(text = stringResource(R.string.click), soundViewModel)
            }

            4 -> {
                ChooseCharacter(
                    dataViewmodel::updateCharacter,
                    uiState.character,
                    viewModel::updateCharacter
                ) { viewModel.increaseState() }
            }

            5 -> {
                SetNickName(
                    nickName = nickName,
                    chose = uiState.character,
                    onSave = dataViewmodel::updateName,
                    onCancel = { viewModel.decreaseState() },
                    nextState = { viewModel.increaseState() },
                )
            }

            6 -> {
                CharacterChat(
                    characterIsMale = uiState.character == 0,
                    text = stringResource(R.string.click),
                    soundViewModel = soundViewModel
                )
            }

            else -> {

            }
        }
    }
}


@Composable
fun CharacterChat(characterIsMale: Boolean, text: String, soundViewModel: SoundViewModel) {
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
                        R.drawable.sage
                    } else {
                        R.drawable.gekko
                    }
                ),
                modifier = Modifier
                    .size(500.dp)
                    .padding(bottom = 40.dp),
                contentDescription = null
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
                LetterByLetterText(text, soundViewModel = soundViewModel)
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
    nickName: String
) {
    var textState by rememberSaveable { mutableStateOf(nickName) }
    val painter = if (chose == 1) {
        painterResource(id = R.drawable.cypher)
    } else {
        painterResource(id = R.drawable.sage)
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
        Image(
            painter = painter,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(160.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.padding(20.dp))
        TextField(
            value = textState,
            onValueChange = { textState = it },
            label = { Text(stringResource(R.string.give_name)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp),
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    if (textState.isNotBlank() && ((textState.length) in 2..15)) {
                        onSave(textState.trim())
                        nextState()
                    }
                }
            ),
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
                    if (textState.isNotBlank() && ((textState.length) in 2..15)) {
                        onSave(textState.trim())
                        nextState()
                    }
                },
                enabled = textState.isNotBlank() && ((textState.length) in 2..15)
            ) {
                Text(stringResource(R.string.save))
            }
        }
    }
}

@Composable
fun CharacterChatTwo(text: String, soundViewModel: SoundViewModel) {
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
            Row(modifier = Modifier.fillMaxWidth()) {
                Image(
                    painter = painterResource(R.drawable.sage),
                    modifier = Modifier
                        .weight(1f)
                        .padding(bottom = 40.dp),
                    contentDescription = null
                )
                Image(
                    painter = painterResource(R.drawable.gekko),
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
                LetterByLetterText(text, soundViewModel = soundViewModel)
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
        Spacer(modifier = Modifier.padding(15.dp))
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
        Spacer(modifier = Modifier.padding(20.dp))
        CompleteCard(
            R.drawable.sage,
            stringResource(R.string.f_name),
            character == 0,
            uiStateUpdate,
            character = 0
        )
        Spacer(modifier = Modifier.padding(10.dp))
        CompleteCard(
            R.drawable.gekko,
            stringResource(R.string.m_name),
            character == 1,
            uiStateUpdate,
            character = 1
        )
        Spacer(modifier = Modifier.weight(0.5f))
        Row(
            modifier = Modifier
                .padding(25.dp)
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
fun CompleteCard(
    imageResourceId: Int,
    name: String,
    selected: Boolean,
    onClicked: KFunction1<Int, Unit>,
    character: Int
) {
    Box(
        modifier = Modifier.height(dimensionResource(id = R.dimen.artist_box))
    ) {
        CharacterCard(
            onClicked,
            name,
            selected,
            character
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
fun CharacterCard(
    onButtonCard: KFunction1<Int, Unit>,
    name: String,
    selected: Boolean,
    character: Int,
    isDarkTheme: Boolean = isSystemInDarkTheme(),
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, top = 30.dp, end = 10.dp)
            .height(150.dp)
            .shadow(2.dp, shape = RoundedCornerShape(16.dp))
            .clip(MaterialTheme.shapes.medium)
            .clickable {
            },
        onClick = { onButtonCard(character) },
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
            RadioButton(selected = selected, onClick = { onButtonCard(character) })
            Text(
                text = name,
                modifier = Modifier
                    .padding(
                        start = 10.dp,
                        end = 120.dp
                    ),
                style = MaterialTheme.typography.bodyMedium,
                color = if (isDarkTheme) {
                    Color.White
                } else {
                    Color.Black
                }
            )
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

@Composable
fun RequestNotificationPermission() {
    val context = LocalContext.current
    var permissionGranted by remember { mutableStateOf(isNotificationPermissionGranted(context)) }

    // Launcher to request the notification permission
    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        permissionGranted = isGranted
    }

    // UI part
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (permissionGranted) {
            Text(text = "Notification permission is granted!")
        } else {
            Button(onClick = {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    // Request permission on Android 13+
                    requestPermissionLauncher.launch("android.permission.POST_NOTIFICATIONS")
                }
            }) {
                Text(text = "Request Notification Permission")
            }
        }
    }
}
