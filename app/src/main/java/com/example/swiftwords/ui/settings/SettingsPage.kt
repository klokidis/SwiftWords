import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.swiftwords.R
import com.example.swiftwords.data.ItemDetailsUiState
import kotlin.reflect.KFunction1

@Composable
fun SettingsPage(
    navigateOut: () -> Unit,
    data: ItemDetailsUiState,
    updateTime: KFunction1<Long, Unit>,
    changeCharacter: KFunction1<Boolean, Unit>,
    introduction: KFunction1<Boolean, Unit>,
) {
    val scrollState = rememberScrollState()
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
            data.userDetails?.let {
                OneSettingMenuLong(
                    stringResource(R.string.level_time),
                    listOf(35000L, 40000L, 50000L, 60000L, 70000L),
                    updateTime,
                    it.levelTime
                )
            }
            Spacer(modifier = Modifier.padding(5.dp))
            data.userDetails?.let {
                OneSettingMenuStrings(
                    stringResource(R.string.change_character),
                    listOf(stringResource(R.string.female), stringResource(R.string.male)),
                    changeCharacter,
                    it.character
                )
            }
            Spacer(modifier = Modifier.padding(5.dp))
            OneSettingSimple(stringResource(R.string.introdacton), introduction)
        }

    }
}

@Composable
fun OneSettingSimple(text: String, onClick: KFunction1<Boolean, Unit>) {
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
fun OneSettingMenuStrings(
    text: String,
    options: List<String>,
    updateCharacter: (Boolean) -> Unit,
    isFemale: Boolean
) {
    SettingMenu(
        text = text,
        selectedOption = if (isFemale) stringResource(R.string.female) else stringResource(R.string.male),
        options = options,
        onOptionSelected = { updateCharacter(it == "Female") },
        displayOption = { it },
        displaySelected = { it }

    )
}

@Composable
fun OneSettingMenuLong(
    text: String,
    options: List<Long>,
    updateTime: (Long) -> Unit,
    levelTime: Long
) {
    SettingMenu(
        text = text,
        selectedOption = levelTime,
        options = options,
        onOptionSelected = updateTime,
        displayOption = { time -> time.toString().take(2) }, // Only display the number in the menu
        displaySelected = { time ->
            time.toString().take(2) + " " + stringResource(R.string.seconds)
        }, // Display with "seconds" only for the selected value
        width = 50.dp
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
    width: Dp = 100.dp
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
                    style = MaterialTheme.typography.titleSmall.copy(fontSize = 20.sp)
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
