package com.keak.kanjininja.common;

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.dp
import kotlin.math.sin

@Composable
fun LiquidLoadingBar(
    modifier: Modifier = Modifier,
    waveColor: Color = Color.Cyan,
    backgroundColor: Color = Color.LightGray.copy(alpha = 0.2f),
    waveAmplitude: Float = 10f,
    waveFrequency: Float = 40f,
    waveSpeed: Int = 2000,
    progress: Float = 0.5f, // 0.0 - 1.0 arasında bir değer
) {
    val infiniteTransition = rememberInfiniteTransition()
    val waveOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = (2 * Math.PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(waveSpeed, easing = LinearEasing)
        )
    )

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(20.dp)
            .clip(RoundedCornerShape(25.dp))
            .background(backgroundColor)
    ) {
        val width = size.width
        val height = size.height

        // Sıvı dolum oranına göre bar uzunluğunu hesaplıyoruz
        val progressWidth = width * progress

        // Dalga çizimi için path
        val path = Path().apply {
            moveTo(0f, height / 3)
            for (x in 0..progressWidth.toInt() step 10) {
                val y = (sin(x / waveFrequency + waveOffset) * waveAmplitude + height / 3)
                lineTo(x.toFloat(), y)
            }
            lineTo(progressWidth, height)
            lineTo(0f, height)
            close()
        }

        drawPath(path = path, color = waveColor)
    }
}