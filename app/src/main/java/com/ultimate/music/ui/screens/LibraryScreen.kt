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
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.ultimate.music.ui.components.TrackCover // <-- ДОБАВЛЕН ИМПОРТ ДЛЯ НОВОЙ ОБЛОЖКИ
import com.ultimate.music.ui.theme.*

@Composable
fun LibraryScreen() {
    val vm: PlayerViewModel = hiltViewModel()
    val tracks by vm.allTracks.collectAsStateWithLifecycle()
    val likedIds by vm.likedTracks.collectAsStateWithLifecycle()
    val currentTrackId by vm.currentTrackId.collectAsStateWithLifecycle()
    
    var showOnlyLiked by remember { mutableStateOf(false) }
    val displayTracks = if (showOnlyLiked) tracks.filter { likedIds.contains(it.mediaId) } else tracks

    Column(Modifier.fillMaxSize().background(BlackBg)) {
        Column(Modifier.fillMaxWidth().padding(16.dp)) {
            Text("Коллекция", color = PureWhite, fontSize = 28.sp, fontWeight = FontWeight.Black)
            Spacer(Modifier.height(12.dp))

            // Карточка Любимое
            Box(
                Modifier.fillMaxWidth().height(120.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(if(showOnlyLiked) Brush.linearGradient(listOf(Color(0xFF4B0082), Color(0xFF8A2BE2))) else Brush.linearGradient(listOf(Color(0xFF8A2BE2), Color(0xFF4B0082))))
                    .clickable { showOnlyLiked = !showOnlyLiked }
                    .padding(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxSize()) {
                    Box(Modifier.size(64.dp).clip(RoundedCornerShape(12.dp)).background(Color(0x33FFFFFF)), contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.Favorite, null, tint = PureWhite, modifier = Modifier.size(36.dp))
                    }
                    Spacer(Modifier.width(16.dp))
                    Column(Modifier.weight(1f)) {
                        Text(if(showOnlyLiked) "Показать все" else "Любимое", color = PureWhite, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        Text("${likedIds.size} треков", color = PureWhite.copy(alpha=0.8f), fontSize = 14.sp)
                    }
                    IconButton(onClick = { vm.playFlow() }, modifier = Modifier.background(PureWhite, CircleShape)) {
                        Icon(Icons.Default.PlayArrow, null, tint = BlackBg)
                    }
                }
            }
        }
        
        Column(Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
            Text(if(showOnlyLiked) "Ваши любимые треки" else "Все треки в устройстве", color = PureWhite, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(12.dp))
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(displayTracks) { track ->
                    val isPlaying = track.mediaId == currentTrackId
                    TrackItem(
                        title = track.mediaMetadata.title?.toString() ?: "Без названия",
                        artist = track.mediaMetadata.artist?.toString() ?: "Неизвестный",
                        isPlaying = isPlaying,
                        isLiked = likedIds.contains(track.mediaId),
                        onClick = { vm.playTrack(track) },
                        onLike = { vm.toggleLike(track.mediaId) }
                    )
                }
            }
        }
    }
}

@Composable
fun TrackItem(title: String, artist: String, isPlaying: Boolean, isLiked: Boolean, onClick: () -> Unit, onLike: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp))
            .background(if (isPlaying) SurfaceDark else Color.Transparent)
            .clickable { onClick() }.padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // ВОТ ТУТ МЫ ЗАМЕНИЛИ СЕРЫЙ КВАДРАТ НА НОВУЮ ОБЛОЖКУ
        TrackCover(title = title, size = 48.dp) 
        
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text(title, color = PureWhite, fontSize = 16.sp, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(artist, color = TabGray, fontSize = 13.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
        IconButton(onClick = onLike) {
            Icon(
                Icons.Default.Favorite, null, 
                tint = if (isLiked) Color(0xFFFF4081) else TabGray
            )
        }
    }
}
