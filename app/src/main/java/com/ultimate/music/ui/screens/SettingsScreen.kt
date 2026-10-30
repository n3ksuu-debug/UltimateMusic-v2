package com.ultimate.music.ui.screens
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ultimate.music.player.PlayerViewModel
import com.ultimate.music.ui.theme.*

@Composable
fun SettingsScreen() {
    val vm: PlayerViewModel = hiltViewModel()
    val stats by vm.statsMinutes.collectAsStateWithLifecycle()
    
    var bass by remember { mutableStateOf(0f) }
    var mid by remember { mutableStateOf(0f) }
    var treble by remember { mutableStateOf(0f) }

    Column(Modifier.fillMaxSize().background(BlackBg).padding(16.dp)) {
        Text("Настройки", color = PureWhite, fontSize = 28.sp, fontWeight = FontWeight.Black)
        Spacer(Modifier.height(24.dp))
        
        // Статистика
        Text("Статистика", color = PureWhite, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        Box(Modifier.fillMaxWidth().clip(MaterialTheme.shapes.medium).background(SurfaceDark).padding(16.dp)) {
            Column {
                Text("Вы прослушали музыку:", color = TabGray, fontSize = 14.sp)
                Text("$stats минут", color = PureWhite, fontSize = 32.sp, fontWeight = FontWeight.Black)
            }
        }
        
        Spacer(Modifier.height(24.dp))
        Text("Эквалайзер", color = PureWhite, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(16.dp))
        
        EqSlider("Низкие (Бас)", bass) { bass = it; vm.setEqBand(0, (it * 1500).toInt().toShort()) }
        EqSlider("Средние", mid) { mid = it; vm.setEqBand(1, (it * 1500).toInt().toShort()) }
        EqSlider("Высокие", treble) { treble = it; vm.setEqBand(2, (it * 1500).toInt().toShort()) }
    }
}

@Composable
fun EqSlider(label: String, value: Float, onChange: (Float) -> Unit) {
    Column(Modifier.padding(vertical = 8.dp)) {
        Text(label, color = TabGray, fontSize = 14.sp)
        Slider(
            value = value, onValueChange = onChange, valueRange = -1f..1f,
            colors = SliderDefaults.colors(thumbColor = PureWhite, activeTrackColor = PureWhite, inactiveTrackColor = SurfaceDark)
        )
    }
}
