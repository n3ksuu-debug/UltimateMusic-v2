package com.ultimate.music.ui.screens
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Nightlight
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ultimate.music.player.PlayerViewModel
import com.ultimate.music.ui.components.AudioVisualizer
import com.ultimate.music.ui.components.PlayButton
import com.ultimate.music.ui.theme.*
import kotlinx.coroutines.delay
import kotlin.random.Random

@Composable
fun HomeScreen(onOpenPlayer: () -> Unit) {
    val vm: PlayerViewModel = hiltViewModel()
    var amp by remember { mutableFloatStateOf(0.3f) }
    LaunchedEffect(Unit) { while (true) { amp = 0.2f + Random.nextFloat() * 0.8f; delay(180) } }
    
    // Легкий градиент на фоне (делает его "живым")
    Box(Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(BgGreen, Color(0xFF083D25))))) {
        AudioVisualizer(amp, Modifier.fillMaxSize())
        Column(Modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.SpaceBetween) {
            Column {
                Text("Микс дня", color = PureWhite, fontSize = 32.sp, fontWeight = FontWeight.Black)
                Text("Нейро-поток • Персонально", color = PureWhite.copy(alpha = 0.8f), fontSize = 14.sp)
            }

            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                PlayButton(amp) {
                    vm.playFlow()
                    onOpenPlayer()
                }
            }

            Column {
                Text("Ваши потоки", color = PureWhite, fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 12.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    item {
                        FlowCard("Мой поток", "Все треки", Icons.Default.PlayArrow, Brush.linearGradient(listOf(Color(0xFF11995E), Color(0xFF0A5C38)))) {
                            vm.playFlow(); onOpenPlayer()
                        }
                    }
                    item {
                        FlowCard("Для отдыха", "Случайный трек", Icons.Default.Nightlight, Brush.linearGradient(listOf(Color(0xFF2ECC71), Color(0xFF1ABC9C)))) {
                            vm.playFlow(); onOpenPlayer()
                        }
                    }
                    item {
                        FlowCard("Любимое", "Лучшее", Icons.Default.Favorite, Brush.linearGradient(listOf(Color(0xFF9B59B6), Color(0xFF5E2A84)))) {
                            vm.playFlow(); onOpenPlayer()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FlowCard(title: String, subtitle: String, icon: ImageVector, brush: Brush, onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(if (isPressed) 0.95f else 1f, label = "scale")

    Box(
        Modifier.size(150.dp, 170.dp)
            .scale(scale) // Анимация нажатия
            .shadow(10.dp, RoundedCornerShape(24.dp)) // Объемная тень
            .clip(RoundedCornerShape(24.dp))
            .background(brush)
            .clickable(interactionSource = interactionSource, indication = null) { onClick() }
            .padding(16.dp)
    ) {
        Column(verticalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxSize()) {
            Icon(icon, contentDescription = null, tint = PureWhite, modifier = Modifier.size(32.dp))
            Column {
                Text(title, color = PureWhite, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text(subtitle, color = PureWhite.copy(alpha = 0.7f), fontSize = 12.sp)
            }
        }
    }
}
