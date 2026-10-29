package com.ultimate.music.ui.screens
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Album
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.QueueMusic
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ultimate.music.ui.theme.*

@Composable
fun LibraryScreen() {
    val scrollState = rememberScrollState()
    Column(Modifier.fillMaxSize().background(VoidGreen).verticalScroll(scrollState).padding(16.dp)) {
        Text("Коллекция", color = CrystalWhite, fontSize = 28.sp, fontWeight = FontWeight.Black)
        Spacer(Modifier.height(16.dp))
        
        // Большая карточка (как Любимое в Яндексе)
        Box(Modifier.fillMaxWidth().height(180.dp).clip(RoundedCornerShape(24.dp)).background(Brush.linearGradient(listOf(Color(0xFF8A2BE2), Color(0xFF4B0082)))).padding(16.dp)) {
            Column(verticalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxSize()) {
                Text("Любимое", color = CrystalWhite, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Text("45 треков", color = CrystalWhite.copy(alpha = 0.7f), fontSize = 14.sp)
            }
        }
        
        Spacer(Modifier.height(24.dp))
        Text("Умные фильтры", color = CrystalWhite, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        
        var bpm by remember { mutableFloatStateOf(120f) }
        var vocal by remember { mutableFloatStateOf(0.5f) }
        Text("BPM: ${bpm.toInt()}", color = NeonTeal, fontSize = 12.sp)
        Slider(bpm, onValueChange = { bpm = it }, valueRange = 60f..200f, colors = SliderDefaults.colors(thumbColor = MoltenCopper, activeTrackColor = MoltenCopper))
        Text("Вокал: ${(vocal * 100).toInt()}%", color = NeonTeal, fontSize = 12.sp)
        Slider(vocal, onValueChange = { vocal = it }, valueRange = 0f..1f, colors = SliderDefaults.colors(thumbColor = MoltenCopper, activeTrackColor = MoltenCopper))
        
        Spacer(Modifier.height(24.dp))
        Text("Ваша музыка", color = CrystalWhite, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(12.dp))
        
        // Сетка квадратных кнопок
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            GridButton("Плейлисты", Icons.Default.QueueMusic)
            GridButton("Скачанное", Icons.Default.Download)
            GridButton("Альбомы", Icons.Default.Album)
            GridButton("Артисты", Icons.Default.Mic)
        }
    }
}

@Composable
fun GridButton(label: String, icon: ImageVector) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(Modifier.size(72.dp).clip(RoundedCornerShape(16.dp)).background(SurfaceDark), contentAlignment = Alignment.Center) {
            Icon(icon, contentDescription = null, tint = MoltenCopper, modifier = Modifier.size(32.dp))
        }
        Spacer(Modifier.height(4.dp))
        Text(label, color = CrystalWhite, fontSize = 11.sp)
    }
}
