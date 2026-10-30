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
import com.ultimate.music.data.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    @ApplicationContext private val ctx: Context,
    private val repo: SettingsRepository,
    private val equalizerManager: EqualizerManager,
    private val visualizerManager: VisualizerManager // <-- ДОБАВЛЕНО
) : ViewModel() {
    private val controllerFuture: ListenableFuture<MediaController>
    private var controller: MediaController? = null
    
    private val _isPlaying = MutableStateFlow(false); val isPlaying = _isPlaying.asStateFlow()
    private val _title = MutableStateFlow("Микс дня"); val title = _title.asStateFlow()
    private val _artist = MutableStateFlow("Нейро-поток"); val artist = _artist.asStateFlow()
    private val _position = MutableStateFlow(0L); val position = _position.asStateFlow()
    private val _duration = MutableStateFlow(0L); val duration = _duration.asStateFlow()
    private val _albumArt = MutableStateFlow<Uri?>(null); val albumArt = _albumArt.asStateFlow()
    private val _currentTrackId = MutableStateFlow<String?>(null); val currentTrackId = _currentTrackId.asStateFlow()
    
    // Волна для визуализатора
    val waveform = visualizerManager.waveform
    
    private val _allTracks = MutableStateFlow<List<MediaItem>>(emptyList()); val allTracks = _allTracks.asStateFlow()
    val likedTracks = repo.likedTracks.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptySet())
    val statsMinutes = repo.statsMinutes.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)
    
    private var sleepTimerJob: Job? = null

    init {
        val token = SessionToken(ctx, ComponentName(ctx, PlaybackService::class.java))
        controllerFuture = MediaController.Builder(ctx, token).buildAsync()
        controllerFuture.addListener({
            val c = controllerFuture.get(); controller = c
            c.addListener(object : Player.Listener {
                override fun onIsPlayingChanged(isPlaying: Boolean) { 
                    _isPlaying.value = isPlaying 
                    if (isPlaying) fadeVolume(0f, 1f, 1000) // Плавное появление звука
                }
                override fun onMediaMetadataChanged(md: MediaMetadata) {
                    _title.value = md.title?.toString() ?: "Микс дня"
                    _artist.value = md.artist?.toString() ?: "Нейро-поток"
                    _albumArt.value = md.artworkUri
                    _currentTrackId.value = c.currentMediaItem?.mediaId
                }
                override fun onPlaybackStateChanged(state: Int) { 
                    _duration.value = c.duration.coerceAtLeast(0) 
                    if (state == Player.STATE_ENDED) { /* Трек закончился */ }
                }
                override fun onMediaItemTransition(item: MediaItem?, reason: Int) { 
                    _duration.value = c.duration.coerceAtLeast(0)
                    _currentTrackId.value = item?.mediaId
                    if (c.isPlaying) fadeVolume(0f, 1f, 1500) // Кроссфейд при переключении
                }
            })
        }, MoreExecutors.directExecutor())
        
        // Тикер позиции + статистика
        viewModelScope.launch { 
            while (true) { 
                controller?.let { _position.value = it.currentPosition }
                delay(500) 
            } 
        }
        
        // Считаем минуты прослушивания
        viewModelScope.launch {
            while (true) {
                delay(60_000)
                if (_isPlaying.value) repo.addMinute()
            }
        }
        
        loadLocalTracks()
    }
    
    // Логика плавного затухания/появления (Кроссфейд)
    private fun fadeVolume(from: Float, to: Float, durationMs: Long) {
        viewModelScope.launch {
            val steps = 20
            val stepTime = durationMs / steps
            for (i in 0..steps) {
                val vol = from + (to - from) * i / steps
                controller?.volume = vol
                delay(stepTime)
            }
        }
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
                while (c.moveToNext() && items.size < 500) {
                    val uri = ContentUris.withAppendedId(collection, c.getLong(id))
                    items += MediaItem.Builder()
                        .setUri(uri)
                        .setMediaId(uri.toString())
                        .setMediaMetadata(MediaMetadata.Builder().setTitle(c.getString(t)).setArtist(c.getString(a)).build())
                        .build()
                }
            }
        }
        _allTracks.value = items
    }

    fun toggleLike(trackId: String) = viewModelScope.launch { repo.toggleLike(trackId) }
    fun playTrack(track: MediaItem) { controller?.let { it.setMediaItem(track); it.prepare(); it.play() } }
    fun playFlow() { if (_allTracks.value.isNotEmpty()) { controller?.let { it.setMediaItems(_allTracks.value.shuffled()); it.prepare(); it.play() } } }
    fun playPause() { controller?.let { if (it.isPlaying) it.pause() else it.play() } }
    fun next() { controller?.seekToNextMediaItem() }
    fun prev() { controller?.seekToPreviousMediaItem() }
    fun seekTo(ms: Long) { controller?.seekTo(ms) }
    fun setSleepTimer(minutes: Int) {
        sleepTimerJob?.cancel()
        if (minutes > 0) { sleepTimerJob = viewModelScope.launch { delay(minutes * 60_000L); controller?.pause() } }
    }
    fun setEqBand(band: Int, level: Short) = equalizerManager.setBand(band, level)
    override fun onCleared() { MediaController.releaseFuture(controllerFuture); super.onCleared() }
}
