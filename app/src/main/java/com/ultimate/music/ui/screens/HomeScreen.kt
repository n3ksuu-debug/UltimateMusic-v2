package com.ultimate.music.ui.screens
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
    
    Box(Modifier.fillMaxSize().background(BgGreen)) {
        AudioVisualizer(amp, Modifier.fillMaxSize())
        Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.SpaceBetween) {
            Column {
                Text("Микс дня", color = PureWhite, fontSize = 28.sp, fontWeight = FontWeight.Black)
                Text("Нейро-поток • Персонально", color = PureWhite.copy(alpha = 0.8f), fontSize = 13.sp)
            }

            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                PlayButton(amp) {
                    vm.playFlow()
                    onOpenPlayer()
                }
            }

            Column {
                Text("Ваши потоки", color = PureWhite, fontSize = 18.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 8.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    item {
                        FlowCard("Мой поток", "Все треки", Brush.linearGradient(listOf(DarkGreen, LightGreen))) {
                            vm.playFlow(); onOpenPlayer()
                        }
                    }
                    item {
                        FlowCard("Для отдыха", "Случайный трек", Brush.linearGradient(listOf(DarkGreen, Color(0xFF2ECC71)))) {
                            vm.playFlow(); onOpenPlayer()
                        }
                    }
                    item {
                        FlowCard("Для спорта", "Энергичный бит", Brush.linearGradient(listOf(DarkGreen, Color(0xFF1ABC9C)))) {
                            vm.playFlow(); onOpenPlayer()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FlowCard(title: String, subtitle: String, brush: Brush, onClick: () -> Unit) {
    Box(Modifier.size(140.dp, 160.dp).clip(RoundedCornerShape(16.dp)).background(brush).clickable { onClick() }.padding(12.dp)) {
        Column(verticalArrangement = Arrangement.Bottom, modifier = Modifier.fillMaxSize()) {
            Text(title, color = PureWhite, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Text(subtitle, color = PureWhite.copy(alpha = 0.8f), fontSize = 11.sp)
        }
    }
}
