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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import com.ultimate.music.ui.theme.*

@Composable
fun PlayButton(amplitude: Float, onClick: () -> Unit) {
    val haptic = LocalHapticFeedback.current
    val pulse = rememberInfiniteTransition(label = "p").animateFloat(1f, 1.05f, infiniteRepeatable(tween(1500, easing = FastOutSlowInEasing), RepeatMode.Reverse), label = "pv")
    Box(contentAlignment = Alignment.Center) {
        repeat(3) { i -> 
            Box(Modifier.size((180 + i * 40 + (amplitude * 40)).dp).border(BorderStroke(1.dp, PureWhite.copy(alpha = 0.15f - i * 0.05f)), CircleShape)) 
        }
        Button(
            onClick = { haptic.performHapticFeedback(HapticFeedbackType.LongPress); onClick() }, 
            modifier = Modifier
                .size(150.dp)
                .scale(pulse.value)
                .shadow(25.dp, CircleShape, spotColor = LightGreen),
            shape = CircleShape, 
            colors = ButtonDefaults.buttonColors(containerColor = PureWhite), 
            contentPadding = PaddingValues(0.dp)
        ) {
            Icon(Icons.Default.PlayArrow, null, tint = BgGreenTop, modifier = Modifier.size(80.dp))
        }
    }
}
