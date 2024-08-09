package com.example.swiftwords.ui.levels

import android.view.MotionEvent
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension

private val shadowSize = 4.dp

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CurrentLevel(
    modifier: Modifier,
    text: String,
    color: Color = Green,
    textColor: Color = Color.White,
    shadowColor: Color = Color(0xFF4e9a00),
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
        var buttonSize by remember { mutableStateOf(IntSize.Zero) }
        val interactionSource = remember { MutableInteractionSource() }

        Box(
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
                .background(shadowColor)
        )

        Button(
            onClick = onClick,
            modifier = Modifier
                .constrainAs(button) {
                    width = Dimension.value(80.dp)
                    height = Dimension.value(80.dp)
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

                        MotionEvent.ACTION_MOVE -> {
                            val isOutside = it.x.toInt() !in 0..buttonSize.width
                                    || it.y.toInt() !in 0..buttonSize.height

                            if (isOutside) animatedY = 0.dp
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
            shape = CircleShape
        ) {
            Text(text = text)
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Levels(
    modifier: Modifier,
    text: String,
    color: Color = Gray,
    textColor: Color = Color.White,
    shadowColor: Color = Color(0xFF4e9a00),
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
        var buttonSize by remember { mutableStateOf(IntSize.Zero) }
        val interactionSource = remember { MutableInteractionSource() }

        Box(
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
                .background(shadowColor)
        )

        Button(
            onClick = { },
            modifier = Modifier
                .constrainAs(button) {
                    width = Dimension.value(80.dp)
                    height = Dimension.value(80.dp)
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

                        MotionEvent.ACTION_MOVE -> {
                            val isOutside = it.x.toInt() !in 0..buttonSize.width
                                    || it.y.toInt() !in 0..buttonSize.height

                            if (isOutside) animatedY = 0.dp
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
            shape = CircleShape
        ) {
            Text(text = text)
        }
    }
}
@Preview(showBackground = true)
@Composable
private fun PreviewDuolingoButton() {
    Column(modifier = Modifier.fillMaxSize()) {
        CurrentLevel(
            modifier = Modifier,
            text = "Preview"
        ) {}
        Levels(modifier = Modifier,
            text = "Preview")
    }
}
