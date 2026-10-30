package com.ultimate.music.ui.components
import androidx.compose.foundation.background
import androidx.compose.foundation.border // <-- ВОТ ОН, ПРОПУЩЕННЫЙ ИМПОРТ
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
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
            .fillMaxWidth().padding(horizontal = 12.dp, vertical = 6.dp)
            .shadow(8.dp, RoundedCornerShape(20.dp))
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xCC1C1C1C)) // Матовое стекло
            .border(1.dp, Color(0x22FFFFFF), RoundedCornerShape(20.dp)) // Тонкая рамка
            .clickable { onOpenPlayer() }
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (albumArt != null) {
            AsyncImage(model = albumArt, contentDescription = null, modifier = Modifier.size(48.dp).clip(RoundedCornerShape(12.dp)), contentScale = ContentScale.Crop)
        } else {
            TrackCover(title = title, size = 48.dp)
        }
        
        Spacer(Modifier.width(12.dp))
        
        Column(Modifier.weight(1f)) {
            Text(title, color = PureWhite, fontSize = 15.sp, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(artist, color = TabGray, fontSize = 12.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }

        IconButton(onClick = { vm.playPause() }) {
            Icon(if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow, contentDescription = null, tint = PureWhite, modifier = Modifier.size(32.dp))
        }
        
        IconButton(onClick = { vm.next() }) {
            Icon(Icons.Default.SkipNext, contentDescription = null, tint = PureWhite, modifier = Modifier.size(32.dp))
        }
    }
}
