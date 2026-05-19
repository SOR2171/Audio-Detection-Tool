package com.github.sor2171.audiodetectiontool.ui.screens

import androidx.compose.runtime.Composable
import com.github.sor2171.audiodetectiontool.core.entity.NoteNameStyle
import space.kodio.core.AudioQuality

@Composable
fun SpectrumScreen(
    bufferSize: Int,
    audioQuality: AudioQuality,
    a4Frequency: Float,
    noteStyle: NoteNameStyle,
    isRecording: Boolean,
    isPausing: Boolean,
    onBufferSizeChange: (Int) -> Unit,
    onQualityChange: (AudioQuality) -> Unit,
    onStyleChange: (NoteNameStyle) -> Unit,
    onFrequencyChange: (Float) -> Unit,
    onStartRecording: () -> Unit,
    onStopRecording: () -> Unit,
    onPauseRecording: () -> Unit
) {

}