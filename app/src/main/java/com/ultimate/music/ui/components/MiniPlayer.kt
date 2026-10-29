package com.ultimate.music.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.ultimate.music.player.PlayerViewModel
import com.ultimate.music.ui.theme.*

@Composable
fun MiniPlayer(onOpenPlayer: () -> Unit) {
    val vm: PlayerViewModel = hiltViewModel()
    val title by vm.title.collectAsStateWithLifecycle()
    val artist by vm.artist.collectAsStateWithLifecycle()
    val isPlaying by vm.isPlaying.collectAsStateWithLifecycle()
    val albumArt by vm.albumArt.collectAsStateWithLifecycle()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(SurfaceDark.copy(alpha = 0.95f))
            .clickable { onOpenPlayer() }
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Обложка
        Box(
            modifier = Modifier.size(48.dp).clip(RoundedCornerShape(8.dp)).background(VoidGreen)
        ) {
            if (albumArt != null) {
                AsyncImage(model = albumArt, contentDescription = null, modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
            }
        }
        
        Spacer(Modifier.width(12.dp))
        
        // Название и артист
        Column(Modifier.weight(1f)) {
            Text(title, color = CrystalWhite, fontSize = 14.sp, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(artist, color = NeonTeal, fontSize = 12.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }

        // Кнопка Play/Pause
        IconButton(onClick = { vm.playPause() }) {
            Icon(if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow, contentDescription = null, tint = MoltenCopper)
        }
        
        // Кнопка Next
        IconButton(onClick = { vm.next() }) {
            Icon(Icons.Default.SkipNext, contentDescription = null, tint = CrystalWhite)
        }
    }
}
