package com.ultimate.music.player
import android.media.audiofx.Equalizer
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EqualizerManager @Inject constructor() {
    private var eq: Equalizer? = null
    private val bandLevels = mutableMapOf<Short, Short>()

    @Synchronized fun attach(id: Int) { 
        if (eq != null) return
        runCatching { 
            eq = Equalizer(0, id).apply { enabled = true }
            // Применяем сохраненные настройки при старте
            bandLevels.forEach { (band, level) -> eq?.setBandLevel(band, level) }
        } 
    }

    @Synchronized fun setBand(bandIndex: Int, level: Short) {
        val e = eq ?: return
        if (bandIndex < e.numberOfBands) {
            e.setBandLevel(bandIndex.toShort(), level)
            bandLevels[bandIndex.toShort()] = level
        }
    }

    @Synchronized fun release() { eq?.release(); eq = null }
}
