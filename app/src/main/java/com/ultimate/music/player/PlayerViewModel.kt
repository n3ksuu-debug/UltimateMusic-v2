package com.ultimate.music.player
import android.content.ComponentName
import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
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
    private val _albumArt = MutableStateFlow<Uri?>(null); val albumArt = _albumArt.asStateFlow()
    
    // Список всех песен в памяти телефона
    private val _allTracks = MutableStateFlow<List<MediaItem>>(emptyList()); val allTracks = _allTracks.asStateFlow()

    init {
        val token = SessionToken(ctx, ComponentName(ctx, PlaybackService::class.java))
        controllerFuture = MediaController.Builder(ctx, token).buildAsync()
        controllerFuture.addListener({
            val c = controllerFuture.get(); controller = c
            c.addListener(object : Player.Listener {
                override fun onIsPlayingChanged(isPlaying: Boolean) { _isPlaying.value = isPlaying }
                override fun onMediaMetadataChanged(md: MediaMetadata) {
                    _title.value = md.title?.toString() ?: "Микс дня"
                    _artist.value = md.artist?.toString() ?: "Нейро-поток"
                    _albumArt.value = md.artworkUri
                }
                override fun onPlaybackStateChanged(state: Int) { _duration.value = c.duration.coerceAtLeast(0) }
                override fun onMediaItemTransition(item: MediaItem?, reason: Int) { _duration.value = c.duration.coerceAtLeast(0) }
            })
        }, MoreExecutors.directExecutor())
        viewModelScope.launch { while (true) { controller?.let { _position.value = it.currentPosition }; delay(500) } }
        
        loadLocalTracks()
    }

    fun loadLocalTracks() {
        val collection = if (Build.VERSION.SDK_INT >= 29) MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL) else MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(MediaStore.Audio.Media._ID, MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.ARTIST)
        val items = mutableListOf<MediaItem>()
        runCatching {
            ctx.contentResolver.query(collection, projection, null, null, null)?.use { c ->
                val id = c.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
                val t = c.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
                val a = c.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
                while (c.moveToNext() && items.size < 100) {
                    items += MediaItem.Builder()
                        .setUri(ContentUris.withAppendedId(collection, c.getLong(id)))
                        .setMediaMetadata(MediaMetadata.Builder().setTitle(c.getString(t)).setArtist(c.getString(a)).build())
                        .build()
                }
            }
        }
        _allTracks.value = items
    }

    fun playTrack(track: MediaItem) {
        controller?.let {
            it.setMediaItem(track)
            it.prepare()
            it.play()
        }
    }

    fun playFlow() {
        val tracks = _allTracks.value.shuffled()
        if (tracks.isNotEmpty()) {
            controller?.let { it.setMediaItems(tracks); it.prepare(); it.play() }
        }
    }

    fun playPause() { controller?.let { if (it.isPlaying) it.pause() else it.play() } }
    fun next() { controller?.seekToNextMediaItem() }
    fun prev() { controller?.seekToPreviousMediaItem() }
    fun seekTo(ms: Long) { controller?.seekTo(ms) }
    fun setQueue(items: List<MediaItem>) { controller?.let { it.setMediaItems(items); it.prepare(); it.play() } }
    override fun onCleared() { MediaController.releaseFuture(controllerFuture); super.onCleared() }
}
