package com.ultimate.music.player
import android.content.ComponentName
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class PlayerViewModel @Inject constructor(@ApplicationContext private val ctx: Context) : ViewModel() {
    private val controllerFuture: ListenableFuture<MediaController>
    private var controller: MediaController? = null
    private val _isPlaying = MutableStateFlow(false); val isPlaying = _isPlaying.asStateFlow()
    private val _title = MutableStateFlow("Микс дня"); val title = _title.asStateFlow()
    private val _artist = MutableStateFlow("Нейро-поток"); val artist = _artist.asStateFlow()
    private val _position = MutableStateFlow(0L); val position = _position.asStateFlow()
    private val _duration = MutableStateFlow(0L); val duration = _duration.asStateFlow()

    init {
        val token = SessionToken(ctx, ComponentName(ctx, PlaybackService::class.java))
        controllerFuture = MediaController.Builder(ctx, token).buildAsync()
        controllerFuture.addListener({
            val c = controllerFuture.get(); controller = c
            c.addListener(object : Player.Listener {
                override fun onIsPlayingChanged(isPlaying: Boolean) { _isPlaying.value = isPlaying }
                override fun onMediaMetadataChanged(md: MediaMetadata) { _title.value = md.title?.toString() ?: "Микс дня"; _artist.value = md.artist?.toString() ?: "Нейро-поток" }
                override fun onPlaybackStateChanged(state: Int) { _duration.value = c.duration.coerceAtLeast(0) }
                override fun onMediaItemTransition(item: MediaItem?, reason: Int) { _duration.value = c.duration.coerceAtLeast(0) }
            })
        }, MoreExecutors.directExecutor())
        viewModelScope.launch { while (true) { controller?.let { _position.value = it.currentPosition }; delay(500) } }
    }
    fun playPause() { controller?.let { if (it.isPlaying) it.pause() else it.play() } }
    fun next() { controller?.seekToNextMediaItem() }
    fun prev() { controller?.seekToPreviousMediaItem() }
    fun seekTo(ms: Long) { controller?.seekTo(ms) }
    fun setQueue(items: List<MediaItem>) { controller?.let { it.setMediaItems(items); it.prepare(); it.play() } }
    override fun onCleared() { MediaController.releaseFuture(controllerFuture); super.onCleared() }
}