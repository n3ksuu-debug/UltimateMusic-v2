package com.ultimate.music.player
import android.media.audiofx.Visualizer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VisualizerManager @Inject constructor() {
    private var visualizer: Visualizer? = null
    private val _waveform = MutableStateFlow<List<Float>>(List(32) { 0f })
    val waveform = _waveform.asStateFlow()

    fun attach(sessionId: Int) {
        if (visualizer != null) return
        runCatching {
            visualizer = Visualizer(sessionId).apply {
                captureSize = Visualizer.getCaptureSizeRange()[0]
                setDataCaptureListener(object : Visualizer.OnDataCaptureListener {
                    override fun onWaveFormDataCapture(v: Visualizer?, waveform: ByteArray?, samplingRate: Int) {
                        waveform?.let { wf ->
                            // Переводим байты в плавные значения от 0 до 1 для анимации
                            _waveform.value = wf.map { (it.toInt() and 0xFF) / 255f }
                        }
                    }
                    override fun onFftDataCapture(v: Visualizer?, fft: ByteArray?, samplingRate: Int) {}
                }, Visualizer.getMaxCaptureRate() / 2, true, false)
                enabled = true
            }
        }
    }
    fun release() { visualizer?.release(); visualizer = null }
}
