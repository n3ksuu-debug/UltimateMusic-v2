package com.ultimate.music.ui.screens
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Nightlight
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ultimate.music.player.PlayerViewModel
import com.ultimate.music.ui.components.AudioVisualizer
import com.ultimate.music.ui.components.PlayButton
import com.ultimate.music.ui.theme.*

@Composable
fun HomeScreen(onOpenPlayer: () -> Unit) {
    val vm: PlayerViewModel = hiltViewModel()
    val waveform by vm.waveform.collectAsStateWithLifecycle()
    
    Box(Modifier.fillMaxSize().background(
        Brush.radialGradient(
            colors = listOf(BgGreenGlow.copy(alpha = 0.4f), BgGreenTop, BgGreenBottom),
            center = Offset(0.5f, 0.4f),
            radius = 1500f
        )
    )) {
        AudioVisualizer(waveform, Modifier.fillMaxSize())
        Column(Modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.SpaceBetween) {
            Column {
                Text("Моя волна", color = PureWhite, fontSize = 32.sp, fontWeight = FontWeight.Black, letterSpacing = (-1).sp)
                Text("Нейро-поток • Персонально", color = PureWhite.copy(alpha = 0.7f), fontSize = 14.sp)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                PlayButton(waveform.maxOrNull() ?: 0f) { vm.playFlow(); onOpenPlayer() }
                Spacer(Modifier.height(16.dp))
                Button(onClick = { }, shape = RoundedCornerShape(24.dp), colors = ButtonDefaults.buttonColors(containerColor = Color(0x33FFFFFF))) {
                    Text("Настроить", color = PureWhite, fontWeight = FontWeight.Bold)
                }
            }
            LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                item { WaveCard("Любимое", "Лучшее", Icons.Default.Favorite, Brush.linearGradient(listOf(Color(0xFF9B59B6), Color(0xFF5E2A84)))) { vm.playFlow(); onOpenPlayer() } }
                item { WaveCard("Мой поток", "Все треки", Icons.Default.PlayArrow, Brush.linearGradient(listOf(Color(0xFF1E9A62), Color(0xFF094D2F)))) { vm.playFlow(); onOpenPlayer() } }
                item { WaveCard("Для отдыха", "Случайный", Icons.Default.Nightlight, Brush.linearGradient(listOf(Color(0xFF3498DB), Color(0xFF154360)))) { vm.playFlow(); onOpenPlayer() } }
            }
        }
    }
}

@Composable
fun WaveCard(title: String, subtitle: String, icon: ImageVector, brush: Brush, onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(if (isPressed) 0.92f else 1f, label = "scale")
    
    Box(
        Modifier.size(150.dp, 170.dp)
            .scale(scale)
            .shadow(16.dp, RoundedCornerShape(24.dp), spotColor = LightGreen)
            .clip(RoundedCornerShape(24.dp))
            .background(Brush.radialGradient(colors = listOf(Color(0x44FFFFFF), Color.Transparent), center = Offset(0f, 0f)))
            .background(brush)
            .clickable(interactionSource = interactionSource, indication = null) { onClick() }
            .padding(16.dp)
    ) {
        Column(verticalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxSize()) {
            Box(Modifier.size(48.dp).clip(CircleShape).background(Color(0x33FFFFFF)), contentAlignment = Alignment.Center) {
                Icon(icon, null, tint = PureWhite, modifier = Modifier.size(28.dp))
            }
            Column {
                Text(title, color = PureWhite, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text(subtitle, color = PureWhite.copy(alpha = 0.7f), fontSize = 12.sp)
            }
        }
    }
}
