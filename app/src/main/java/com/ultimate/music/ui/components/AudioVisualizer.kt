package com.ultimate.music.ui.components
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import com.ultimate.music.ui.theme.MoltenCopper
import com.ultimate.music.ui.theme.NeonTeal
import kotlin.math.*
import kotlin.random.Random

@Composable
fun AudioVisualizer(amplitude: Float, modifier: Modifier = Modifier) {
    val particles = remember { List(150) { Triple(Random.nextFloat(), Random.nextFloat(), Random.nextFloat()) } }
    val phase = rememberInfiniteTransition(label = "ph").animateFloat(
        0f, (2 * PI).toFloat(), infiniteRepeatable(tween(6000, easing = LinearEasing), RepeatMode.Restart), label = "pv"
    )
    Canvas(modifier) {
        val cx = size.width / 2f; val cy = size.height * 0.42f
        particles.forEachIndexed { i, (a, r, s) ->
            val angle = a * (2 * PI).toFloat() + phase.value * (0.5f + s)
            val radius = size.minDimension * 0.1f + r * size.minDimension * 0.4f * (1f + amplitude * 0.8f)
            val x = cx + cos(angle) * radius
            val y = cy + sin(angle) * radius * 0.85f
            val alpha = (0.4f + amplitude * 0.6f).coerceIn(0f, 1f)
            val color = if (i % 4 == 0) MoltenCopper.copy(alpha = alpha) else NeonTeal.copy(alpha = alpha)
            drawCircle(color = color, radius = (1.5f + 5f * amplitude * s) * 2.5f, center = Offset(x, y))
        }
    }
}
