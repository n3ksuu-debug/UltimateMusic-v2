package com.ultimate.music.ui.screens
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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

    val pagerState = rememberPagerState(pageCount = { 3 })

    Box(Modifier.fillMaxSize().background(VoidGreen)) {
        // 1. Размытый фон из обложки
        if (albumArt != null) {
            AsyncImage(
                model = albumArt,
                contentDescription = null,
                modifier = Modifier.fillMaxSize().blur(60.dp),
                contentScale = ContentScale.Crop,
                alpha = 0.4f
            )
        }

        Column(Modifier.fillMaxSize().padding(16.dp).systemBarsPadding(), horizontalAlignment = Alignment.CenterHorizontally) {
            Box(Modifier.fillMaxWidth()) { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = NeonTeal) } }

            // 2. Свайпы (Обложка / Текст / Очередь)
            HorizontalPager(state = pagerState, modifier = Modifier.weight(1f).fillMaxWidth()) { page ->
                when (page) {
                    0 -> {
                        // Обложка
                        Box(Modifier.fillMaxSize().padding(16.dp).clip(RoundedCornerShape(32.dp)).background(SurfaceDark).border(1.dp, NeonTeal.copy(alpha = 0.3f), RoundedCornerShape(32.dp))) {
                            if (albumArt != null) {
                                AsyncImage(model = albumArt, contentDescription = "Cover", modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
                            }
                        }
                    }
                    1 -> {
                        // Текст песни (Заглушка)
                        Box(Modifier.fillMaxSize().padding(16.dp), contentAlignment = Alignment.Center) {
                            Text("Текст песни загружается...", color = CrystalWhite.copy(alpha = 0.5f), fontSize = 18.sp)
                        }
                    }
                    2 -> {
                        // Очередь (Заглушка)
                        Box(Modifier.fillMaxSize().padding(16.dp), contentAlignment = Alignment.Center) {
                            Text("Очередь пуста", color = CrystalWhite.copy(alpha = 0.5f), fontSize = 18.sp)
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))
            Text(title, color = CrystalWhite, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Text(artist, color = NeonTeal, fontSize = 14.sp)
            
            Slider(
                value = pos.toFloat(), valueRange = 0f..(dur.coerceAtLeast(1L)).toFloat(),
                onValueChange = { vm.seekTo(it.toLong()) },
                colors = SliderDefaults.colors(thumbColor = MoltenCopper, activeTrackColor = MoltenCopper, inactiveTrackColor = Color.White.copy(alpha = 0.15f)),
                modifier = Modifier.padding(vertical = 8.dp)
            )

            // 3. Матовое стекло (Glassmorphism) для нижней панели
            Box(
                Modifier.fillMaxWidth().padding(bottom = 8.dp)
                    .clip(RoundedCornerShape(32.dp))
                    .background(Color.White.copy(alpha = 0.08f))
                    .border(1.dp, Color.White.copy(alpha = 0.15f), RoundedCornerShape(32.dp))
                    .padding(horizontal = 16.dp, vertical = 4.dp)
            ) {
                BottomMediator(isPlaying, vm::playPause, vm::next, vm::prev)
            }
        }
    }
}
