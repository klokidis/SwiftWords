package com.yukuro.swiftwords.ui.levels

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import com.yukuro.swiftwords.R
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yukuro.swiftwords.data.ColorPair
import com.yukuro.swiftwords.data.DataSource
import com.yukuro.swiftwords.ui.AppViewModelProvider
import com.yukuro.swiftwords.ui.elements.CurrentLevel
import com.yukuro.swiftwords.ui.elements.Levels
import com.yukuro.swiftwords.ui.game.getFireImage
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.math.abs

@Composable
fun LevelScreen(
    levelViewModel: LevelViewModel = viewModel(factory = AppViewModelProvider.Factory),
    currentLevel: Int,
    starterLevel: Int,
    endingLevel: Int,
    color: Int,
    navigateToLevel: () -> Unit,
) {
    val levelUiState by levelViewModel.uiState.collectAsState()

    // listState to manage the scroll state of the LazyColumn
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    // State to hold whether the current level is above or below the visible range
    var isCurrentLevelAboveVisible by remember { mutableStateOf(false) }
    var isCurrentLevelBelowVisible by remember { mutableStateOf(false) }

    // LaunchedEffect to track scroll position but avoid recomposing unnecessarily
    LaunchedEffect(listState, currentLevel) {
        snapshotFlow { listState.layoutInfo }
            .collect { layoutInfo ->
                val currentLevelIndex = currentLevel - starterLevel

                // Update the state based on the position of the current level
                val firstVisibleIndex = layoutInfo.visibleItemsInfo.firstOrNull()?.index ?: 0
                val lastVisibleIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0

                val isAbove = currentLevelIndex < firstVisibleIndex
                val isBelow = currentLevelIndex > lastVisibleIndex

                if (isCurrentLevelAboveVisible != isAbove) {
                    isCurrentLevelAboveVisible = isAbove
                }
                if (isCurrentLevelBelowVisible != isBelow) {
                    isCurrentLevelBelowVisible = isBelow
                }

            }
    }

    LaunchedEffect(Unit) {
        val indexToScroll = currentLevel - starterLevel
        listState.scrollToItem(if ((indexToScroll - 1) > 0) indexToScroll - 1 else indexToScroll)
    }

    LazyColumn(
        state = listState,
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        itemsIndexed((starterLevel..endingLevel).toList()) { index, level ->
            if (level > 0) {
                val (leftPadding, rightPadding) = levelUiState.padding.getOrNull(index)
                    ?: Pair(0.dp, 0.dp)
                LevelCard(
                    number = level,
                    rightPadding = rightPadding,
                    leftPadding = leftPadding,
                    thisLevel = level,
                    currentLevel = currentLevel,
                    onClick = navigateToLevel,
                    color = color,
                    colors = levelUiState.colors
                )
            }

        }
    }

    AnimatedVisibility(
        visible = isCurrentLevelAboveVisible || isCurrentLevelBelowVisible,
        enter = slideInVertically(
            initialOffsetY = { it }, // Slide in from the bottom
            animationSpec = tween(durationMillis = 400)
        ) + fadeIn(animationSpec = tween(400)),
        exit = slideOutVertically(
            targetOffsetY = { it }, // Slide out to the bottom
            animationSpec = tween(durationMillis = 300)
        ) + fadeOut(animationSpec = tween(300))
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 15.dp, end = 12.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            val animatedColor by animateColorAsState(
                targetValue = levelUiState.colors[color].darkColor,
                animationSpec = tween(durationMillis = 300),
                label = ""
            )
            Button(
                modifier = Modifier
                    .size(50.dp)
                    .drawBehind {
                        drawCircle(animatedColor)
                    },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent //Transparent for animation color
                ),
                shape = CircleShape,
                contentPadding = PaddingValues(10.dp),
                onClick = {
                    // Scroll to the current level when the button is clicked
                    coroutineScope.launch {
                        val indexToScroll = currentLevel - starterLevel
                        listState.scrollToItem(if ((indexToScroll - 1) > 0) indexToScroll - 1 else indexToScroll)
                    }
                }) {
                // Rotate the icon based on whether the current level is above or below the visible range
                val rotationAngle = if (isCurrentLevelAboveVisible) {
                    90f
                } else {
                    270f
                }

                Icon(
                    modifier = Modifier
                        .fillMaxSize()
                        .rotate(rotationAngle), // Rotate the icon based on the current level position
                    imageVector = ImageVector.vectorResource(R.drawable.arrow),
                    contentDescription = "",
                    tint = Color.White
                )
            }
        }
    }

    // BottomLevel for the current level
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        BottomLevel(
            onClick = navigateToLevel,
            level = currentLevel.toString(),
            color = color,
            colors = levelUiState.colors
        )

    }
}

@Composable
fun LevelCard(
    number: Int,
    rightPadding: Dp,
    leftPadding: Dp,
    thisLevel: Int,
    currentLevel: Int,
    onClick: () -> Unit,
    color: Int,
    colors: List<ColorPair>,
    isDarkTheme: Boolean = isSystemInDarkTheme(),
) {
    val upcomingLevelColor = if (isDarkTheme) Color(0xFF3B3B3D) else Color(0xFF6D6D74)

    Column(
        modifier = Modifier
            .padding(
                start = leftPadding,
                end = rightPadding,
                bottom = 30.dp,
                top = 20.dp
            ),
    ) {
        when {
            thisLevel < currentLevel -> {
                Levels(
                    true,
                    modifier = Modifier,
                    color = colors[color].darkColor,
                )
            }

            thisLevel == currentLevel -> {
                CurrentLevel(
                    modifier = Modifier,
                    text = number.toString(),
                    onClick = onClick,
                    colorCode = color,
                    color = colors,
                )
            }

            else -> {
                Levels(
                    false,
                    modifier = Modifier,
                    color = upcomingLevelColor,
                )
            }
        }
    }
}

@Composable
fun TopBar(
    //livesLeft: Int, //will be used in the future
    streak: Int,
    color: Int,
    changeColorFun: (Int) -> Unit,
    colors: List<ColorPair>,
    streakDateData: String,
    dateNow: String,
    selectedTime: Long,
    timeList: List<Long> = DataSource().timeList,
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    onTimeChange: (Long) -> Unit,
) {
    val formattedStreakDate = safeSubstring(streakDateData, 10)
    val formattedDateNow = safeSubstring(dateNow, 10)
    var isExpandedColor by remember { mutableStateOf(false) }



    Surface(
        modifier = Modifier
            .fillMaxWidth(),
        color = MaterialTheme.colorScheme.background,
        shadowElevation = 3.5.dp
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            MenuColorPicker(
                color,
                changeColorFun,
                colors,
                isExpandedColor
            ) { isExpandedColor = !isExpandedColor }

            Spacer(modifier = Modifier.weight(1f))

            Image(
                painter = when {
                    areDatesMoreThanOneDaysApart(formattedStreakDate, formattedDateNow) -> {
                        painterResource(id = R.drawable.fire_off)
                    }

                    formattedDateNow != formattedStreakDate -> {
                        painterResource(
                            id = when {
                                streak < 5 -> R.drawable.fire_ending

                                streak < 20 -> R.drawable.fire3_end

                                streak < 30 -> R.drawable.fire4_end

                                streak < 40 -> R.drawable.fire5_end

                                streak >= 50 -> R.drawable.fire6_end

                                else -> R.drawable.fire_ending
                            }
                        )

                    }

                    else -> {
                        painterResource(
                            id = getFireImage(streak)
                        )
                    }
                },
                modifier = Modifier.size(30.dp),
                contentDescription = stringResource(R.string.streak)
            )

            Text(
                text = when { // this for faster change since data takes a sec to change
                    areDatesMoreThanOneDaysApart(formattedStreakDate, formattedDateNow) -> {
                        "0"
                    }

                    else -> {
                        streak.toString()
                    }
                },
                maxLines = 1, // Restrict to a single line to fix overflow when menu opens
                modifier = Modifier
                    .padding(start = 3.dp, bottom = 3.dp),
                style = MaterialTheme.typography.titleMedium.copy(fontSize = 24.sp)
            )

            Spacer(modifier = Modifier.weight(1f))

            TimerMenu(
                isExpandedColor,
                colors,
                color,
                timeList,
                selectedTime,
                isDarkTheme,
                onTimeChange
            )

        }
    }
}

@Composable
private fun TimerMenu(
    isExpandedColor: Boolean,
    colors: List<ColorPair>,
    color: Int,
    timeList: List<Long>,
    selectedTime: Long,
    isDarkTheme: Boolean,
    onNewTime: (Long) -> Unit
) {
    var isClickedTime by remember { mutableStateOf(false) }
    val animatedColorIconButton by animateColorAsState(
        if (isClickedTime) colors[color].darkColor else MaterialTheme.colorScheme.background,
        animationSpec = tween(durationMillis = 300),
        label = ""
    )
    Box {
        IconButton(
            onClick = {
                if (!isExpandedColor) {
                    isClickedTime = !isClickedTime
                }
            },
            modifier = Modifier.drawBehind {
                if (!isExpandedColor) { // hides draw behind to not interfere
                    drawCircle(
                        color = animatedColorIconButton,
                        radius = size.minDimension / 3 + 10, // Set the radius to half of the smaller dimension to make a perfect circle
                        center = Offset(
                            size.width / 2,
                            size.height / 2
                        ) // Center the circle
                    )
                }
            }
        ) {
            Icon(
                modifier = Modifier.size(30.dp),
                imageVector = if (isClickedTime) {
                    ImageVector.vectorResource(R.drawable.timer_filled_24px)
                } else {
                    ImageVector.vectorResource(R.drawable.timer_24px)
                },
                contentDescription = stringResource(R.string.pickTheme),
                tint = if (isClickedTime) MaterialTheme.colorScheme.surface else colors[color].darkColor
            )
        }
        DropdownMenu(
            expanded = isClickedTime,
            modifier = Modifier.wrapContentSize(),
            onDismissRequest = { isClickedTime = false }
        ) {
            Row {
                Column(modifier = Modifier.width(50.dp)) {
                    timeList.forEach { time ->
                        val firstTwoDigits = time.toString().take(2)
                        DropdownMenuItem(
                            onClick = {
                                onNewTime(time)
                                isClickedTime = false
                            },
                            text = {
                                Text(
                                    firstTwoDigits,
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontSize = 18.sp
                                    ),
                                    color = when {
                                        time == selectedTime -> colors[color].darkColor
                                        isDarkTheme -> Color.White
                                        else -> Color.Black
                                    }
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}


//recalculate it here for faster change than data
fun areDatesMoreThanOneDaysApart(date1: String, date2: String): Boolean {
    // Define the date format with explicit locale
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    // Parse the dates from the strings
    val parsedDate1 = dateFormat.parse(date1)
    val parsedDate2 = dateFormat.parse(date2)

    // Create calendar instances and set the parsed dates
    val calendar1 = Calendar.getInstance().apply {
        if (parsedDate1 != null) {
            time = parsedDate1
        }
    }
    val calendar2 = Calendar.getInstance().apply {
        if (parsedDate2 != null) {
            time = parsedDate2
        }
    }

    // Get the time in milliseconds for each date
    val timeInMillis1 = calendar1.timeInMillis
    val timeInMillis2 = calendar2.timeInMillis

    // Calculate the difference in milliseconds
    val differenceInMillis = abs(timeInMillis1 - timeInMillis2)

    // Calculate the difference in days
    val differenceInDays = differenceInMillis / (24 * 60 * 60 * 1000)

    // Return whether the difference is more than 2 days
    return differenceInDays > 1
}

@Composable
fun MenuColorPicker(
    color: Int,
    changeColorFun: (Int) -> Unit,
    colors: List<ColorPair>,
    isExpanded: Boolean,
    changeExpanded: () -> Unit
) {
    val scrollState = rememberScrollState()

    val animatedColorIconButton by animateColorAsState(
        if (isExpanded) colors[color].darkColor else MaterialTheme.colorScheme.background,
        animationSpec = tween(durationMillis = 300),
        label = ""
    )

    Box(modifier = Modifier.wrapContentSize()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            IconButton(
                onClick = changeExpanded,
                modifier = Modifier.drawBehind {
                    drawCircle(
                        color = animatedColorIconButton,
                        radius = size.minDimension / 3 + 10, // Set the radius to half of the smaller dimension to make a perfect circle
                        center = Offset(size.width / 2, size.height / 2) // Center the circle
                    )
                }
            ) {
                Icon(
                    modifier = Modifier.size(30.dp),
                    imageVector = if (isExpanded) {
                        ImageVector.vectorResource(R.drawable.format_paint_24px)
                    } else {
                        ImageVector.vectorResource(R.drawable.format_paint_24px_noexp)
                    },
                    contentDescription = stringResource(R.string.pickTheme),
                    tint = if (isExpanded) MaterialTheme.colorScheme.surface else colors[color].darkColor
                )
            }

            AnimatedVisibility(visible = isExpanded) {
                Row(
                    modifier = Modifier
                        .padding(start = 5.dp)
                        .clip(RoundedCornerShape(50.dp))
                        .height(40.dp)
                        .wrapContentSize()
                        .drawBehind {
                            // Draw the border
                            drawRoundRect(
                                color = animatedColorIconButton,
                                size = Size(size.width, size.height),
                                cornerRadius = CornerRadius(40.dp.toPx(), 40.dp.toPx()),
                                style = Stroke(width = 1.5.dp.toPx())
                            )
                        }
                        .padding(top = 7.dp, bottom = 7.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .horizontalScroll(scrollState)
                            .padding(start = 2.dp, end = 2.dp)
                    ) {
                        colors.forEach { thisColor ->
                            // Animate size for the background color circle
                            val animatedSizeSelected by animateDpAsState(
                                targetValue = if (color == thisColor.id) 17.dp else 26.dp,
                                animationSpec = tween(durationMillis = 500),
                                label = ""
                            )

                            // Animate size for the foreground dark color circle
                            val animatedSize by animateDpAsState(
                                targetValue = if (color == thisColor.id) 26.dp else 12.dp,
                                animationSpec = tween(durationMillis = 700),
                                label = ""
                            )

                            Spacer(modifier = Modifier.padding(5.dp))
                            Box(
                                modifier = Modifier
                                    .size(26.dp)
                                    .clip(RoundedCornerShape(26.dp))
                            ) {
                                ColorBox(
                                    animatedSize = { animatedSize },
                                    animatedSizeSelected = { animatedSizeSelected },
                                    thisColor = thisColor,
                                    changeColorFun = changeColorFun,
                                    modifier = Modifier.align(Alignment.Center) // Center the entire box
                                )
                            }
                            Spacer(modifier = Modifier.padding(5.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ColorBox(
    animatedSize: () -> Dp,
    thisColor: ColorPair,
    changeColorFun: (Int) -> Unit,
    modifier: Modifier,
    animatedSizeSelected: () -> Dp,
    isDarkTheme: Boolean = isSystemInDarkTheme(),
) {
    Box(
        modifier = modifier
            .layout { measurable, _ ->
                val sizePx = animatedSize()
                    .roundToPx()
                    .coerceAtLeast(0)

                val constraints = Constraints.fixed(
                    width = sizePx,
                    height = sizePx,
                )

                val placeable = measurable.measure(constraints)
                layout(sizePx, sizePx) {
                    placeable.place(0, 0)
                }
            }
            .clip(RoundedCornerShape(26.dp))
            .background(
                if (isDarkTheme) Color.White else Color(0xFF0D1114)
            )
            .clickable {
                changeColorFun(thisColor.id)
            }
    )
    Box(
        modifier = modifier
            .layout { measurable, _ ->
                val sizePx = animatedSizeSelected()
                    .roundToPx()
                    .coerceAtLeast(0)

                val constraints = Constraints.fixed(
                    width = sizePx,
                    height = sizePx,
                )

                val placeable = measurable.measure(constraints)
                layout(sizePx, sizePx) {
                    placeable.place(0, 0)
                }
            }
            .clip(RoundedCornerShape(26.dp))
            .background(thisColor.darkColor)
    )
}

@Composable
fun BottomLevel(onClick: () -> Unit, level: String, color: Int, colors: List<ColorPair>) {
    val animatedColor by animateColorAsState(
        targetValue = colors[color].darkColor,
        animationSpec = tween(durationMillis = 300),
        label = ""
    )
    Card(
        modifier = Modifier
            .padding(15.dp)
            .height(50.dp)
            .fillMaxWidth(0.6f),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp,
        ),
        onClick = onClick
    ) {
        Box(
            modifier = Modifier
                .drawBehind { drawRoundRect(animatedColor) }
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.level) + " " + level,
                color = Color.White,
                style = MaterialTheme.typography.titleSmall.copy(
                    fontSize = 22.sp,
                    letterSpacing = 1.sp
                )
            )
        }
    }
}

fun safeSubstring(input: String, length: Int): String {
    return if (input.length >= length) {
        input.substring(0, length)
    } else {
        "01/01/1999"//return random date
    }
}