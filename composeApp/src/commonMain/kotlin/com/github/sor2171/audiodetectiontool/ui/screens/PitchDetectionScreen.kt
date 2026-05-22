package com.github.sor2171.audiodetectiontool.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.sor2171.audiodetectiontool.core.entity.NoteData
import com.github.sor2171.audiodetectiontool.core.entity.NoteNameStyle
import com.github.sor2171.audiodetectiontool.core.service.PitchDetector
import com.github.sor2171.audiodetectiontool.core.utils.NoteNameFormatter
import com.github.sor2171.audiodetectiontool.core.utils.toAudioWindows
import com.github.sor2171.audiodetectiontool.getPlatform
import com.github.sor2171.audiodetectiontool.ui.component.PitchTrackerDisplayCard
import com.github.sor2171.audiodetectiontool.ui.component.RecordButtons
import com.github.sor2171.audiodetectiontool.ui.component.RecorderController
import com.github.sor2171.audiodetectiontool.ui.component.SettingCard
import com.github.sor2171.audiodetectiontool.ui.theme.AppTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import space.kodio.core.AudioQuality
import space.kodio.core.Kodio

@Composable
fun PitchTrackerScreen(
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
    var detectedFrequency by remember { mutableStateOf(0f) }
    val detector = remember(audioQuality, bufferSize) {
        PitchDetector(audioQuality.format.sampleRate, bufferSize)
    }

    RecorderController(
        isRecording = isRecording,
        isPausing = isPausing,
        audioQuality = audioQuality,
        bufferSize = bufferSize,
        performOnWindow = { floatWindow ->
            val frequency = detector.detectPitchYIN(floatWindow)
            withContext(Dispatchers.Main) {
                // stable the frequency by averaging with previous value
                detectedFrequency = if (frequency > 0f) {
                    if (detectedFrequency == 0f) frequency
                    else detectedFrequency * 0.5f + frequency * 0.5f
                } else {
                    0f
                }
            }
        }
    )

    val controlPanel = @Composable { modifier: Modifier ->
        ControlPanel(
            selectedBufferSize = bufferSize,
            audioQuality = audioQuality,
            noteStyle = noteStyle,
            a4Frequency = a4Frequency,
            isRecording = isRecording,
            isPausing = isPausing,
            onBufferSizeChange = onBufferSizeChange,
            onQualityChange = onQualityChange,
            onStyleChange = onStyleChange,
            onFrequencyChange = onFrequencyChange,
            onStartRecording = onStartRecording,
            onStopRecording = {
                onStopRecording()
                detectedFrequency = 0f
            },
            onPauseRecording = onPauseRecording,
            modifier = modifier
        )
    }

    val displayPanel = @Composable { modifier: Modifier ->
        DisplayPanel(
            detectedFrequency = detectedFrequency,
            a4Frequency = a4Frequency,
            noteStyle = noteStyle,
            modifier = modifier
        )
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        val isLandscape = maxWidth > maxHeight
        if (isLandscape) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier.weight(0.7f).fillMaxHeight(),
                    contentAlignment = Alignment.Center
                ) {
                    controlPanel(Modifier)
                }

                Box(
                    modifier = Modifier.weight(1f).fillMaxHeight(),
                    contentAlignment = Alignment.Center
                ) {
                    displayPanel(Modifier)
                }
            }
        } else {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceAround,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                controlPanel(Modifier)
                displayPanel(Modifier)
            }
        }
    }
}

@Composable
private fun ControlPanel(
    selectedBufferSize: Int,
    audioQuality: AudioQuality,
    noteStyle: NoteNameStyle,
    a4Frequency: Float,
    isRecording: Boolean,
    isPausing: Boolean,
    onBufferSizeChange: (Int) -> Unit,
    onQualityChange: (AudioQuality) -> Unit,
    onStyleChange: (NoteNameStyle) -> Unit,
    onFrequencyChange: (Float) -> Unit,
    onStartRecording: () -> Unit,
    onStopRecording: () -> Unit,
    onPauseRecording: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        SettingCard(
            selectedBufferSize = selectedBufferSize,
            selectedStyle = noteStyle,
            selectedQuality = audioQuality,
            a4Frequency = a4Frequency,
            onBufferSizeChange = onBufferSizeChange,
            onQualityChange = onQualityChange,
            onStyleChange = onStyleChange,
            onFrequencyChange = onFrequencyChange
        )

        RecordButtons(
            isRecording = isRecording,
            isPausing = isPausing,
            onStartRecording = onStartRecording,
            onStopRecording = onStopRecording,
            onPauseRecording = onPauseRecording
        )
    }
}

@Composable
private fun DisplayPanel(
    detectedFrequency: Float,
    a4Frequency: Float,
    noteStyle: NoteNameStyle,
    modifier: Modifier = Modifier
) {
    val (noteName, cents) = if (detectedFrequency > 20f)
        NoteNameFormatter.getNoteData(
            detectedFrequency,
            a4Frequency,
            noteStyle
        ) else NoteData("--", 0)

    PitchTrackerDisplayCard(
        noteName = noteName,
        cents = cents,
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
private fun PitchTrackerScreenPreview() {
    val platformData = getPlatform()
    AppTheme(platformData) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            PitchTrackerScreen(
                bufferSize = 2048,
                audioQuality = AudioQuality.Standard,
                a4Frequency = 440f,
                noteStyle = NoteNameStyle.Scientific,
                isRecording = false,
                isPausing = false,
                onBufferSizeChange = {},
                onQualityChange = {},
                onStyleChange = {},
                onFrequencyChange = {},
                onStartRecording = {},
                onStopRecording = {},
                onPauseRecording = {}
            )
        }
    }
}
