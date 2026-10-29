package com.ultimate.music.player
import android.content.Intent
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
@AndroidEntryPoint
class PlaybackService : MediaSessionService() {
    @Inject lateinit var equalizerManager: EqualizerManager
    private var session: MediaSession? = null
    private lateinit var player: ExoPlayer
    override fun onCreate() {
        super.onCreate()
        player = ExoPlayer.Builder(this).setAudioAttributes(AudioAttributes.Builder().setContentType(C.AUDIO_CONTENT_TYPE_MUSIC).setUsage(C.USAGE_MEDIA).build(), true).setHandleAudioBecomingNoisy(true).build()
        runCatching { equalizerManager.attach(player.audioSessionId) }
        session = MediaSession.Builder(this, player).build()
    }
    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo) = session
    override fun onTaskRemoved(rootIntent: Intent?) { player.pause(); stopSelf() }
    override fun onDestroy() { session?.run { player.release(); release() }; session = null; equalizerManager.release(); super.onDestroy() }
}