package com.ultimate.music.ui.components
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ultimate.music.ui.theme.*
@Composable
fun BottomMediator(isPlaying: Boolean, onPlayPause: () -> Unit, onNext: () -> Unit, onPrev: () -> Unit) {
    Row(Modifier.fillMaxWidth().height(96.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
        IconButton(onClick = {}) { Icon(Icons.Default.Cast, null, tint = NeonTeal) }
        Row(horizontalArrangement = Arrangement.spacedBy(24.dp), verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onPrev) { Icon(Icons.Default.SkipPrevious, null, tint = CrystalWhite) }
            IconButton(onClick = onPlayPause, modifier = Modifier.size(72.dp).background(MoltenCopper, CircleShape)) { Icon(if(isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow, null, tint = VoidGreen, modifier = Modifier.size(36.dp)) }
            IconButton(onClick = onNext) { Icon(Icons.Default.SkipNext, null, tint = CrystalWhite) }
        }
        IconButton(onClick = {}) { Icon(Icons.Default.Timer, null, tint = NeonTeal) }
    }
}