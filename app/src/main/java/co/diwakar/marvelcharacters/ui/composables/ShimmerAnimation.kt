package co.diwakar.marvelcharacters.ui.composables

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import co.diwakar.marvelcharacters.ui.theme.ShimmerColorShades

@Composable
fun ShimmerAnimation(modifier: Modifier = Modifier) {
    val transition = rememberInfiniteTransition()
    val translateAnim by transition.animateFloat(
        initialValue = 0f, targetValue = 1000f, animationSpec = infiniteRepeatable(
            tween(durationMillis = 2000, easing = FastOutSlowInEasing), RepeatMode.Reverse
        )
    )

    val brush = Brush.linearGradient(
        colors = ShimmerColorShades,
        start = Offset(10f, 10f),
        end = Offset(translateAnim, translateAnim)
    )

    ShimmerItem(brush = brush, modifier = modifier)
}

@Composable
fun ShimmerItem(brush: Brush, modifier: Modifier) {
    Column {
        Spacer(
            modifier = modifier
                .fillMaxSize()
                .background(brush = brush)
        )
    }
}