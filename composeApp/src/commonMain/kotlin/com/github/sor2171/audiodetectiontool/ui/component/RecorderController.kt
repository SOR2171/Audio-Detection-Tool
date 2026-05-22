package com.github.sor2171.audiodetectiontool.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.github.sor2171.audiodetectiontool.core.utils.toAudioWindows
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.withContext
import space.kodio.core.AudioQuality
import space.kodio.core.Kodio

@Composable
fun RecorderController(
    isRecording: Boolean,
    isPausing: Boolean,
    audioQuality: AudioQuality = AudioQuality.Standard,
    bufferSize: Int = 2048,
    performOnWindow: suspend (FloatArray) -> Unit
) {
    LaunchedEffect(
        isRecording,
        isPausing,
        audioQuality,
        bufferSize
    ) {
        if (isRecording && !isPausing) {
            withContext(Dispatchers.Default) {
                val recorder = Kodio.recorder(quality = audioQuality)
                try {
                    delay(500) // wait for the recorder to be released
                    recorder.start()
                    recorder.liveAudioFlow?.toAudioWindows(
                        windowSize = bufferSize,
                        hopSize = bufferSize / 2,
                        channels = audioQuality.format.channels.count,
                        bytesPerSample = audioQuality.format.bytesPerSample
                    )?.conflate()?.collect(performOnWindow)
                } finally {
                    recorder.release()
                }
            }
        }
    }
}