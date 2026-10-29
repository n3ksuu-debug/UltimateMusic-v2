package com.ultimate.music.ui.components
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import com.ultimate.music.ui.theme.*

@Composable
fun PlayButton(amplitude: Float, onClick: () -> Unit) {
    val haptic = LocalHapticFeedback.current
    val pulse = rememberInfiniteTransition(label = "p").animateFloat(1f, 1.08f, infiniteRepeatable(tween(1200), RepeatMode.Reverse), label = "pv")
    Box(contentAlignment = Alignment.Center) {
        repeat(3) { i -> Box(Modifier.size((180 + i * 40 + (amplitude * 40)).dp).border(BorderStroke(1.dp, PureWhite.copy(alpha = 0.3f - i * 0.1f)), CircleShape)) }
        Button(onClick = { haptic.performHapticFeedback(HapticFeedbackType.LongPress); onClick() }, modifier = Modifier.size(160.dp).scale(pulse.value), shape = CircleShape, colors = ButtonDefaults.buttonColors(containerColor = PureWhite), contentPadding = PaddingValues(0.dp)) {
            Icon(Icons.Default.PlayArrow, null, tint = BgGreen, modifier = Modifier.size(72.dp))
        }
    }
}
