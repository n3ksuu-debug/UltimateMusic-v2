package com.ultimate.music.ui.screens
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ultimate.music.ui.theme.*
@Composable
fun LibraryScreen() {
    var bpm by remember { mutableFloatStateOf(120f) }; var vocal by remember { mutableFloatStateOf(0.5f) }
    Column(Modifier.fillMaxSize().background(VoidGreen).padding(16.dp)) {
        Text("Библиотека", color = CrystalWhite, fontSize = 24.sp, fontWeight = FontWeight.Bold); Spacer(Modifier.height(24.dp))
        Text("BPM: ${bpm.toInt()}", color = NeonTeal, fontSize = 12.sp)
        Slider(bpm, onValueChange = { bpm = it }, valueRange = 60f..200f, colors = SliderDefaults.colors(thumbColor = MoltenCopper, activeTrackColor = MoltenCopper))
        Text("Вокал: ${(vocal * 100).toInt()}%", color = NeonTeal, fontSize = 12.sp)
        Slider(vocal, onValueChange = { vocal = it }, valueRange = 0f..1f, colors = SliderDefaults.colors(thumbColor = MoltenCopper, activeTrackColor = MoltenCopper))
    }
}