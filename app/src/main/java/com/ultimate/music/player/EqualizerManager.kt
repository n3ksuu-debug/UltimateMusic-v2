package com.ultimate.music.player
import android.media.audiofx.Equalizer
import javax.inject.Inject
import javax.inject.Singleton
@Singleton
class EqualizerManager @Inject constructor() {
    private var eq: Equalizer? = null
    @Synchronized fun attach(id: Int) { if (eq != null) return; runCatching { eq = Equalizer(0, id).apply { enabled = true } } }
    @Synchronized fun release() { eq?.release(); eq = null }
}