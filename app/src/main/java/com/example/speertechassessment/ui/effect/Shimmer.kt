package com.example.speertechassessment.ui.effect

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

@Composable
fun Modifier.shimmerLoading(
    durationMillis: Int = 1000,
): Modifier {
    val transition = rememberInfiniteTransition(label = "shimmer")

    val translateAnimation = transition.animateFloat(
        initialValue = 0f,
        targetValue = 500f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = durationMillis,
                easing = LinearEasing,
            ),
            repeatMode = RepeatMode.Restart,
        ),
        label = "shimmerTranslate",
    )

    // OPT: read the animated value in the draw phase only. Reading it during composition (as before)
    // recomposed every caller on every frame; now only this layer redraws.
    return drawBehind {
        val offset = translateAnimation.value
        drawRect(
            brush = Brush.linearGradient(
                colors = ShimmerColors,
                start = Offset(x = offset, y = offset),
                end = Offset(x = offset + 100f, y = offset + 100f),
            )
        )
    }
}

private val ShimmerColors = listOf(
    Color.LightGray.copy(alpha = 0.2f),
    Color.LightGray.copy(alpha = 1.0f),
    Color.LightGray.copy(alpha = 0.2f),
)
