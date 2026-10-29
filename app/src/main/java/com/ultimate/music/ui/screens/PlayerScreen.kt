package com.ultimate.music.ui.screens
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.ultimate.music.player.PlayerViewModel
import com.ultimate.music.ui.components.BottomMediator
import com.ultimate.music.ui.theme.*

@Composable
fun PlayerScreen(onBack: () -> Unit) {
    val vm: PlayerViewModel = hiltViewModel()
    val isPlaying by vm.isPlaying.collectAsStateWithLifecycle()
    val title by vm.title.collectAsStateWithLifecycle()
    val artist by vm.artist.collectAsStateWithLifecycle()
    val pos by vm.position.collectAsStateWithLifecycle()
    val dur by vm.duration.collectAsStateWithLifecycle()
    val albumArt by vm.albumArt.collectAsStateWithLifecycle()
    val currentTrackId by vm.currentTrackId.collectAsStateWithLifecycle()
    val likedIds by vm.likedTracks.collectAsStateWithLifecycle()

    var showTimerDialog by remember { mutableStateOf(false) }
    val pagerState = rememberPagerState(pageCount = { 3 })

    Box(Modifier.fillMaxSize().background(BlackBg)) {
        if (albumArt != null) {
            AsyncImage(model = albumArt, contentDescription = null, modifier = Modifier.fillMaxSize().blur(60.dp), contentScale = ContentScale.Crop, alpha = 0.4f)
        }

        Column(Modifier.fillMaxSize().padding(16.dp).systemBarsPadding(), horizontalAlignment = Alignment.CenterHorizontally) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = PureWhite) }
                IconButton(onClick = { showTimerDialog = true }) { Icon(Icons.Default.Timer, null, tint = PureWhite) }
            }

            // Свайпы для смены трека (влево/вправо)
            HorizontalPager(state = pagerState, modifier = Modifier.weight(1f).fillMaxWidth()) { page ->
                if (page == 0) {
                    Box(
                        Modifier.fillMaxSize().padding(16.dp).clip(RoundedCornerShape(32.dp)).background(SurfaceDark)
                            .pointerInput(Unit) {
                                detectHorizontalDragGestures { _, dragAmount ->
                                    if (dragAmount > 100) vm.prev()
                                    else if (dragAmount < -100) vm.next()
                                }
                            }
                    ) {
                        if (albumArt != null) {
                            AsyncImage(model = albumArt, contentDescription = "Cover", modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
                        }
                        // Кнопка лайка на обложке
                        IconButton(
                            onClick = { currentTrackId?.let { vm.toggleLike(it) } },
                            modifier = Modifier.align(Alignment.TopEnd).padding(16.dp).background(Color(0x80000000), RoundedCornerShape(50))
                        ) {
                            Icon(Icons.Default.Favorite, null, tint = if (likedIds.contains(currentTrackId)) Color(0xFFFF4081) else PureWhite)
                        }
                    }
                } else if (page == 1) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("Текст песни", color = PureWhite.copy(alpha=0.5f)) }
                } else {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("Очередь", color = PureWhite.copy(alpha=0.5f)) }
                }
            }

            Spacer(Modifier.height(16.dp))
            Text(title, color = PureWhite, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Text(artist, color = TabGray, fontSize = 14.sp)
            
            Slider(
                value = pos.toFloat(), valueRange = 0f..(dur.coerceAtLeast(1L)).toFloat(),
                onValueChange = { vm.seekTo(it.toLong()) },
                colors = SliderDefaults.colors(thumbColor = PureWhite, activeTrackColor = PureWhite, inactiveTrackColor = Color.White.copy(alpha = 0.2f)),
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Box(Modifier.fillMaxWidth().clip(RoundedCornerShape(32.dp)).background(Color.White.copy(alpha = 0.1f)).padding(8.dp)) {
                BottomMediator(isPlaying, vm::playPause, vm::next, vm::prev)
            }
        }
    }

    // Диалог таймера сна
    if (showTimerDialog) {
        AlertDialog(
            onDismissRequest = { showTimerDialog = false },
            title = { Text("Таймер сна", color = PureWhite) },
            text = { Text("Выберите время, через которое музыка остановится", color = TabGray) },
            containerColor = SurfaceDark,
            confirmButton = {
                TextButton(onClick = { vm.setSleepTimer(15); showTimerDialog = false }) { Text("15 минут", color = PureWhite) }
            },
            dismissButton = {
                TextButton(onClick = { vm.setSleepTimer(30); showTimerDialog = false }) { Text("30 минут", color = PureWhite) }
            }
        )
    }
}
