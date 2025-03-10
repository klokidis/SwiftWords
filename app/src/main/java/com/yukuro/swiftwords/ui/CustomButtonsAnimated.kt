package com.yukuro.swiftwords.ui

import android.view.MotionEvent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.yukuro.swiftwords.R
import com.yukuro.swiftwords.data.ColorPair
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private val shadowSize = 8.dp

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CurrentLevel(
    modifier: Modifier,
    text: String,
    colorCode: Int,
    size: Dp = 90.dp,
    color: List<ColorPair>,
    textColor: Color = Color.White,
    onClick: () -> Unit
) {
    ConstraintLayout(
        modifier = modifier
    ) {
        val (back, button) = createRefs()
        var animatedY by remember { mutableStateOf(0.dp) }
        val animTranslationY by animateDpAsState(
            targetValue = animatedY,
            animationSpec = tween(50),
            label = ""
        )
        val animatedColor by animateColorAsState(
            targetValue = color[colorCode].darkColor,
            animationSpec = tween(durationMillis = 300),
            label = ""
        )
        val animatedShadowColor by animateColorAsState(
            targetValue = color[colorCode].darkColor.darken(0.8f),
            animationSpec = tween(durationMillis = 300),
            label = ""
        )
        var buttonSize by remember { mutableStateOf(IntSize.Zero) }
        val interactionSource = remember { MutableInteractionSource() }

        Spacer(
            modifier = Modifier
                .constrainAs(back) {
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints

                    start.linkTo(button.start)
                    end.linkTo(button.end)
                    top.linkTo(button.top)
                    bottom.linkTo(button.bottom)

                    translationY = shadowSize
                }
                .clip(CircleShape)
                .drawBehind {
                    drawCircle(animatedShadowColor)
                }
        )

        Button(
            onClick = {},
            modifier = Modifier
                .constrainAs(button) {
                    width = Dimension.value(size)
                    height = Dimension.value(size)
                    top.linkTo(parent.top)

                    translationY = animTranslationY
                }
                .indication(
                    interactionSource = interactionSource,
                    indication = null
                )
                .onGloballyPositioned {
                    buttonSize = it.size
                }
                .pointerInteropFilter {
                    when (it.action) {
                        MotionEvent.ACTION_DOWN -> {
                            animatedY = shadowSize
                        }

                        MotionEvent.ACTION_UP -> {
                            animatedY = 0.dp
                            onClick()
                        }

                        MotionEvent.ACTION_CANCEL -> {
                            animatedY = 0.dp
                        }
                    }
                    true
                }
                .drawBehind {
                    drawCircle(animatedColor)
                },
            contentPadding = PaddingValues(0.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent, // Set to transparent because we are using drawBehind
                contentColor = textColor
            ),
            shape = CircleShape
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium.copy(fontSize = 35.sp),
                modifier = Modifier.padding(bottom = 6.dp)
            )
        }
    }
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Levels(
    passed: Boolean,
    modifier: Modifier,
    size: Dp = 85.dp,
    color: Color = Gray,
    textColor: Color = Color.White,
    shadowColor: Color = color.darken(0.8f),
) {
    ConstraintLayout(
        modifier = modifier
    ) {
        val (back, button) = createRefs()
        var animatedY by remember { mutableStateOf(0.dp) }
        val animTranslationY by animateDpAsState(
            targetValue = animatedY,
            animationSpec = tween(50),
            label = ""
        )
        val animatedColor by animateColorAsState(
            targetValue = color,
            animationSpec = tween(durationMillis = 300),
            label = ""
        )
        val animatedShadowColor by animateColorAsState(
            targetValue = shadowColor,
            animationSpec = tween(durationMillis = 300),
            label = ""
        )
        var buttonSize by remember { mutableStateOf(IntSize.Zero) }
        val interactionSource = remember { MutableInteractionSource() }

        Spacer(
            modifier = Modifier
                .constrainAs(back) {
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints

                    start.linkTo(button.start)
                    end.linkTo(button.end)
                    top.linkTo(button.top)
                    bottom.linkTo(button.bottom)

                    translationY = shadowSize
                }
                .clip(CircleShape)
                .drawBehind {
                    drawCircle(animatedShadowColor)
                }
        )

        Button(
            onClick = { },
            modifier = Modifier
                .constrainAs(button) {
                    width = Dimension.value(size)
                    height = Dimension.value(size)
                    top.linkTo(parent.top)

                    translationY = animTranslationY
                }
                .indication(
                    interactionSource = interactionSource,
                    indication = null
                )
                .onGloballyPositioned {
                    buttonSize = it.size
                }
                .pointerInteropFilter {
                    when (it.action) {
                        MotionEvent.ACTION_DOWN -> {
                            animatedY = shadowSize
                        }

                        MotionEvent.ACTION_UP -> {
                            animatedY = 0.dp
                        }

                        MotionEvent.ACTION_CANCEL -> {
                            animatedY = 0.dp
                        }

                    }
                    true
                }
                .drawBehind {
                    drawCircle(animatedColor)
                },
            contentPadding = PaddingValues(0.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = textColor
            ),
            shape = CircleShape
        ) {
            if (passed) {
                Icon(
                    painter = painterResource(id = R.drawable.done_icon),
                    contentDescription = null,
                    modifier = Modifier.size(40.dp)
                )
            } else {
                Icon(
                    painter = painterResource(id = R.drawable.outline_lock),
                    contentDescription = null,
                    modifier = Modifier.size(30.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ModesCards(
    imageRes: Int,
    textRes: Int,
    size: Dp = 170.dp,
    heightCustom: Dp = size,
    color: Color,
    textColor: Color = Color.White,
    shadowColor: Color,
    onClick: () -> Unit,
    isDarkTheme: Boolean = isSystemInDarkTheme(),
) {
    val cornerRadius = 16.dp // Adjust the corner radius as needed

    ConstraintLayout {
        val (back, button) = createRefs()
        var animatedY by remember { mutableStateOf(0.dp) }
        val animTranslationY by animateDpAsState(
            targetValue = animatedY,
            animationSpec = tween(50),
            label = ""
        )
        var buttonSize by remember { mutableStateOf(IntSize.Zero) }
        val interactionSource = remember { MutableInteractionSource() }

        Spacer(
            modifier = Modifier
                .constrainAs(back) {
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints

                    start.linkTo(button.start)
                    end.linkTo(button.end)
                    top.linkTo(button.top)
                    bottom.linkTo(button.bottom)

                    translationY = shadowSize
                }
                .clip(RoundedCornerShape(cornerRadius))
                .background(shadowColor)
        )

        Button(
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(button) {
                    top.linkTo(parent.top)
                    height = Dimension.value(heightCustom)

                    translationY = animTranslationY
                }
                .clip(RoundedCornerShape(cornerRadius)) // Ensure the button is clipped to the rounded rectangle shape
                .indication(
                    interactionSource = interactionSource,
                    indication = null
                )
                .onGloballyPositioned {
                    buttonSize = it.size
                }
                .pointerInteropFilter {
                    when (it.action) {
                        MotionEvent.ACTION_DOWN -> {
                            animatedY = shadowSize
                        }

                        MotionEvent.ACTION_UP -> {
                            animatedY = 0.dp
                            onClick()
                        }

                        MotionEvent.ACTION_CANCEL -> {
                            animatedY = 0.dp
                        }
                    }
                    true
                },
            contentPadding = PaddingValues(0.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = color,
                contentColor = textColor
            ),
            shape = RoundedCornerShape(cornerRadius) // Apply the rounded rectangle shape directly to the button
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Image(
                    painter = painterResource(imageRes),
                    contentDescription = null,
                    modifier = Modifier
                        .height(95.dp)
                        .padding(bottom = 10.dp),
                    contentScale = ContentScale.FillHeight
                )
                Text(
                    text = stringResource(textRes),
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontSize = 20.sp,
                        letterSpacing = 1.sp
                    ),
                    color = if (isDarkTheme) Color.Black else Color.White
                )
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun KeyCards(
    thisLetter: Char,
    imageRes: Int? = null,
    thisText: String? = null,
    color: Color,
    textColor: Color = Color.White,
    shadowColor: Color,
    onClick: (String) -> Unit,
    customSize: Dp = 60.dp
) {
    val cornerRadius = 16.dp // Adjust the corner radius as needed
    val coroutineScope = rememberCoroutineScope()
    ConstraintLayout {
        val (back, button) = createRefs()
        val customWidth = if (imageRes == null && thisText == null) {
            customSize
        } else {
            customSize + 15.dp
        }

        var animatedY by remember { mutableStateOf(0.dp) }
        val animTranslationY by animateDpAsState(
            targetValue = animatedY,
            animationSpec = tween(50),
            label = ""
        )
        var buttonSize by remember { mutableStateOf(IntSize.Zero) }
        val interactionSource = remember { MutableInteractionSource() }

        Spacer(
            modifier = Modifier
                .constrainAs(back) {
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints

                    start.linkTo(button.start)
                    end.linkTo(button.end)
                    top.linkTo(button.top)
                    bottom.linkTo(button.bottom)

                    translationY = 5.dp
                }
                .clip(RoundedCornerShape(cornerRadius))
                .background(shadowColor)
        )

        Button(
            onClick = {
                onClick(thisLetter.lowercase())
                coroutineScope.launch {
                    animatedY = 5.dp
                    delay(150L)
                    animatedY = 0.dp
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(button) {
                    top.linkTo(parent.top)
                    height = Dimension.value(customSize)
                    width = Dimension.value(customWidth)

                    translationY = animTranslationY
                }
                .clip(RoundedCornerShape(cornerRadius)) // Ensure the button is clipped to the rounded rectangle shape
                .indication(
                    interactionSource = interactionSource,
                    indication = null
                )
                .onGloballyPositioned {
                    buttonSize = it.size
                }
                .pointerInteropFilter {
                    when (it.action) {
                        MotionEvent.ACTION_DOWN -> {
                            coroutineScope.launch {
                                animatedY = 5.dp
                                onClick(
                                    thisLetter.lowercase()
                                )
                                delay(300L)
                                animatedY = 0.dp
                            }
                        }

                        MotionEvent.ACTION_UP -> {
                            animatedY = 0.dp
                        }

                        MotionEvent.ACTION_CANCEL -> {
                            animatedY = 0.dp
                        }
                    }
                    true
                },
            contentPadding = PaddingValues(0.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = color,
                contentColor = textColor
            ),
            shape = RoundedCornerShape(cornerRadius) // Apply the rounded rectangle shape directly to the button
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
            ) {
                if (imageRes == null) {
                    Text(
                        text = thisText ?: thisLetter.toString(),
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontSize = 20.sp,
                            letterSpacing = 1.sp
                        )
                    )
                } else {
                    Image(
                        painter = painterResource(imageRes),
                        contentDescription = null,
                        modifier = Modifier
                            .size(50.dp)
                            .padding(end = 3.dp)
                    )
                }
            }
        }
    }
}


fun Color.darken(factor: Float = 0.7f): Color {
    return Color(
        red = red * factor,
        green = green * factor,
        blue = blue * factor,
        alpha = alpha
    )
}

fun Color.brighten(factor: Float = 1.5f): Color {
    return copy(
        red = (red * factor).coerceAtMost(1f),
        green = (green * factor).coerceAtMost(1f),
        blue = (blue * factor).coerceAtMost(1f)
    )
}

