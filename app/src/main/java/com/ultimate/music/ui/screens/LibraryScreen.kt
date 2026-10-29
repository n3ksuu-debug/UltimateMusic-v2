package com.ultimate.music.ui.screens
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ultimate.music.player.PlayerViewModel
import com.ultimate.music.ui.theme.*

@Composable
fun LibraryScreen() {
    val vm: PlayerViewModel = hiltViewModel()
    val tracks by vm.allTracks.collectAsStateWithLifecycle()
    val currentTitle by vm.title.collectAsStateWithLifecycle()

    Column(Modifier.fillMaxSize().background(VoidGreen).padding(16.dp)) {
        Text("Коллекция", color = CrystalWhite, fontSize = 28.sp, fontWeight = FontWeight.Black)
        Text("Треки в памяти устройства", color = NeonTeal, fontSize = 13.sp)
        Spacer(Modifier.height(16.dp))
        
        if (tracks.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Нет музыки в памяти телефона.\nСкачайте MP3 файлы.", color = CrystalWhite.copy(alpha=0.5f), fontSize = 16.sp)
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(tracks) { track ->
                    val isPlayingThis = track.mediaMetadata.title == currentTitle
                    TrackItem(
                        title = track.mediaMetadata.title?.toString() ?: "Без названия",
                        artist = track.mediaMetadata.artist?.toString() ?: "Неизвестный исполнитель",
                        isPlaying = isPlayingThis,
                        onClick = {
                            vm.playTrack(track)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun TrackItem(title: String, artist: String, isPlaying: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp))
            .background(if (isPlaying) SurfaceDark else Color.Transparent)
            .clickable { onClick() }.padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(Modifier.size(40.dp).clip(RoundedCornerShape(8.dp)).background(MoltenCopper.copy(alpha = if(isPlaying) 1f else 0.3f))) 
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text(title, color = if (isPlaying) MoltenCopper else CrystalWhite, fontSize = 16.sp, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(artist, color = NeonTeal, fontSize = 12.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
    }
}
