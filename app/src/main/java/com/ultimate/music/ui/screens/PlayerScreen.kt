package com.ultimate.music.ui.screens
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ultimate.music.player.PlayerViewModel
import com.ultimate.music.ui.components.BottomMediator
import com.ultimate.music.ui.theme.*
@Composable
fun PlayerScreen(onBack: () -> Unit) {
    val vm: PlayerViewModel = hiltViewModel()
    val isPlaying by vm.isPlaying.collectAsStateWithLifecycle(); val title by vm.title.collectAsStateWithLifecycle()
    val artist by vm.artist.collectAsStateWithLifecycle(); val pos by vm.position.collectAsStateWithLifecycle(); val dur by vm.duration.collectAsStateWithLifecycle()
    Column(Modifier.fillMaxSize().background(VoidGreen).padding(16.dp).systemBarsPadding(), horizontalAlignment = Alignment.CenterHorizontally) {
        Box(Modifier.fillMaxWidth()) { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = NeonTeal) } }
        Box(Modifier.fillMaxWidth().weight(1f).clip(RoundedCornerShape(24.dp)).background(Brush.radialGradient(listOf(NeonTeal.copy(alpha = 0.25f), MoltenCopper.copy(alpha = 0.3f), SurfaceDark))).border(1.dp, NeonTeal.copy(alpha = 0.4f), RoundedCornerShape(24.dp)))
        Spacer(Modifier.height(24.dp)); Text(title, color = CrystalWhite, fontSize = 22.sp, fontWeight = FontWeight.SemiBold); Text(artist, color = NeonTeal, fontSize = 14.sp)
        Slider(value = pos.toFloat(), valueRange = 0f..(dur.coerceAtLeast(1L)).toFloat(), onValueChange = { vm.seekTo(it.toLong()) }, colors = SliderDefaults.colors(thumbColor = MoltenCopper, activeTrackColor = MoltenCopper, inactiveTrackColor = Color.White.copy(alpha = 0.15f)), modifier = Modifier.padding(vertical = 16.dp))
        BottomMediator(isPlaying, vm::playPause, vm::next, vm::prev)
    }
}