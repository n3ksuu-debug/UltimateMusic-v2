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
    val particles = remember { List(120) { Triple(Random.nextFloat(), Random.nextFloat(), Random.nextFloat()) } }
    val phase = rememberInfiniteTransition(label = "ph").animateFloat(0f, (2 * PI).toFloat(), infiniteRepeatable(tween(8000, easing = LinearEasing), RepeatMode.Restart), label = "pv")
    Canvas(modifier) {
        val cx = size.width / 2f; val cy = size.height * 0.42f
        particles.forEachIndexed { i, (a, r, s) ->
            val angle = a * (2 * PI).toFloat() + phase.value * (0.5f + s)
            val radius = size.minDimension * 0.12f + r * size.minDimension * 0.35f * (1f + amplitude * 0.7f)
            drawCircle(color = if (i % 3 == 0) MoltenCopper else NeonTeal.copy(alpha = 0.7f), radius = (2f + 6f * amplitude * s) * 2f, center = Offset(cx + cos(angle) * radius, cy + sin(angle) * radius * 0.85f))
        }
    }
}