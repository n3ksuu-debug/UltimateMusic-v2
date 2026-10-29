package com.ultimate.music.ui.screens
import android.content.ContentUris
import android.content.Context
import android.os.Build
import android.provider.MediaStore
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.ultimate.music.player.PlayerViewModel
import com.ultimate.music.ui.components.AudioVisualizer
import com.ultimate.music.ui.components.PlayButton
import com.ultimate.music.ui.theme.*
import kotlinx.coroutines.delay
import kotlin.random.Random

@Composable
fun HomeScreen(onOpenPlayer: () -> Unit) {
    val vm: PlayerViewModel = hiltViewModel(); val ctx = LocalContext.current
    var amp by remember { mutableFloatStateOf(0.3f) }
    LaunchedEffect(Unit) { while (true) { amp = 0.2f + Random.nextFloat() * 0.8f; delay(180) } }
    Box(Modifier.fillMaxSize().background(VoidGreen)) {
        AudioVisualizer(amp, Modifier.fillMaxSize())
        Column(Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.SpaceBetween) {
            // Эффект матового стекла для заголовка
            Column(
                Modifier.clip(RoundedCornerShape(16.dp))
                    .background(Color.White.copy(alpha = 0.05f))
                    .border(1.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Text("Микс дня", color = CrystalWhite, fontSize = 32.sp, fontWeight = FontWeight.Black, letterSpacing = (-1).sp)
                Text("Нейро-поток • Персонально для вас", color = NeonTeal, fontSize = 13.sp, letterSpacing = 0.5.sp)
            }
            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                PlayButton(amp) {
                    val tracks = loadLocalTracks(ctx)
                    if (tracks.isNotEmpty()) vm.setQueue(tracks)
                    onOpenPlayer()
                }
            }
            Text("Коснитесь — начнётся поток", color = NeonTeal.copy(alpha = 0.7f), fontSize = 13.sp, modifier = Modifier.fillMaxWidth())
        }
    }
}
private fun loadLocalTracks(ctx: Context): List<MediaItem> {
    val collection = if (Build.VERSION.SDK_INT >= 29) MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL) else MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
    val projection = arrayOf(MediaStore.Audio.Media._ID, MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.ARTIST)
    val items = mutableListOf<MediaItem>()
    runCatching {
        ctx.contentResolver.query(collection, projection, null, null, null)?.use { c ->
            val id = c.getColumnIndexOrThrow(MediaStore.Audio.Media._ID); val t = c.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE); val a = c.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
            while (c.moveToNext() && items.size < 50) {
                items += MediaItem.Builder().setUri(ContentUris.withAppendedId(collection, c.getLong(id))).setMediaMetadata(MediaMetadata.Builder().setTitle(c.getString(t)).setArtist(c.getString(a)).build()).build()
            }
        }
    }
    return items
}
