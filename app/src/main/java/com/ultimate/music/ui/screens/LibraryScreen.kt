package com.ultimate.music.ui.screens
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
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

    Column(Modifier.fillMaxSize()) {
        // Верхняя фиолетовая часть
        Column(Modifier.fillMaxWidth().background(Brush.verticalGradient(listOf(PurpleBg, PurpleLight))).padding(16.dp)) {
            Text("Коллекция", color = PureWhite, fontSize = 28.sp, fontWeight = FontWeight.Black)
            Spacer(Modifier.height(16.dp))
            
            // Плитка "Любимое"
            Box(Modifier.fillMaxWidth().height(100.dp).clip(RoundedCornerShape(16.dp)).background(Brush.linearGradient(listOf(Color(0xFFD8BFD8), Color(0xFF8A2BE2)))).padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Favorite, contentDescription = null, tint = Color(0xFF4B0082), modifier = Modifier.size(48.dp))
                    Spacer(Modifier.width(16.dp))
                    Column {
                        Text("Любимое", color = PureWhite, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        Text("45 треков", color = PureWhite.copy(alpha = 0.8f), fontSize = 14.sp)
                    }
                }
            }
        }
        
        // Черная часть со списком треков
        Column(Modifier.fillMaxSize().background(BlackBg).padding(16.dp)) {
            Text("Треки в устройстве", color = PureWhite, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(12.dp))
            
            if (tracks.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Нет музыки в памяти.\nСкачайте MP3 файлы.", color = TabGray, fontSize = 16.sp)
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(tracks) { track ->
                        val isPlayingThis = track.mediaMetadata.title == currentTitle
                        TrackItem(
                            title = track.mediaMetadata.title?.toString() ?: "Без названия",
                            artist = track.mediaMetadata.artist?.toString() ?: "Неизвестный",
                            isPlaying = isPlayingThis,
                            onClick = { vm.playTrack(track) }
                        )
                    }
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
        Box(Modifier.size(40.dp).clip(RoundedCornerShape(8.dp)).background(if(isPlaying) PureWhite else SurfaceDark)) 
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text(title, color = if (isPlaying) PureWhite else PureWhite, fontSize = 16.sp, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(artist, color = TabGray, fontSize = 12.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
    }
}
