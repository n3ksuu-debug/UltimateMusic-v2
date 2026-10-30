package com.ultimate.music.ui.components
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import com.ultimate.music.ui.theme.PureWhite

@Composable
fun AudioVisualizer(waveform: List<Float>, modifier: Modifier = Modifier) {
    Canvas(modifier) {
        val barWidth = size.width / waveform.size
        waveform.forEachIndexed { index, value ->
            val barHeight = value * size.height * 0.8f
            val x = index * barWidth + barWidth / 2
            val y = size.height * 0.42f // Центрируем
            drawLine(
                color = PureWhite.copy(alpha = 0.6f),
                start = Offset(x, y - barHeight / 2),
                end = Offset(x, y + barHeight / 2),
                strokeWidth = barWidth * 0.6f
            )
        }
    }
}
