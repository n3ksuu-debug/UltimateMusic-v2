package com.ultimate.music.ui.screens
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
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

    Column(Modifier.fillMaxSize().background(BlackBg)) {
        // Верхняя часть (Профиль + Коллекция)
        Column(Modifier.fillMaxWidth().padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.size(48.dp).clip(CircleShape).background(Color(0xFF333333))) // Аватарка
                Spacer(Modifier.width(12.dp))
                Text("Пользователь", color = PureWhite, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.height(20.dp))
            Text("Коллекция", color = PureWhite, fontSize = 28.sp, fontWeight = FontWeight.Black)
            Spacer(Modifier.height(12.dp))

            // Фиолетовая карточка (Любимое)
            Box(
                Modifier.fillMaxWidth().height(120.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Brush.linearGradient(listOf(Color(0xFF8A2BE2), Color(0xFF4B0082))))
                    .padding(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxSize()) {
                    Box(Modifier.size(64.dp).clip(RoundedCornerShape(12.dp)).background(Color(0x33FFFFFF)), contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.Favorite, null, tint = Color(0xFF1E0A3C), modifier = Modifier.size(36.dp))
                    }
                    Spacer(Modifier.width(16.dp))
                    Column(Modifier.weight(1f)) {
                        Text("Любимое", color = PureWhite, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                        Text("${tracks.size} треков", color = PureWhite.copy(alpha=0.8f), fontSize = 14.sp)
                    }
                    IconButton(onClick = { vm.playFlow() }, modifier = Modifier.background(PureWhite, CircleShape)) {
                        Icon(Icons.Default.PlayArrow, null, tint = BlackBg)
                    }
                }
            }
        }
        
        // Список треков
        Column(Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
            Text("Треки в устройстве", color = PureWhite, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(12.dp))
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(tracks) { track ->
                    val isPlaying = track.mediaMetadata.title == currentTitle
                    TrackItem(
                        title = track.mediaMetadata.title?.toString() ?: "Без названия",
                        artist = track.mediaMetadata.artist?.toString() ?: "Неизвестный",
                        isPlaying = isPlaying,
                        onClick = { vm.playTrack(track) }
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
            .background(if (isPlaying) Color(0xFF1C1C1C) else Color.Transparent)
            .clickable { onClick() }.padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(Modifier.size(48.dp).clip(RoundedCornerShape(8.dp)).background(Color(0xFF333333))) // Обложка (пока заглушка)
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text(title, color = if (isPlaying) PureWhite else PureWhite, fontSize = 16.sp, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(artist, color = TabGray, fontSize = 13.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
        Icon(Icons.Default.MoreVert, null, tint = TabGray)
    }
}
