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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
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
import com.github.sor2171.audiodetectiontool.ui.component.SettingCard
import com.github.sor2171.audiodetectiontool.ui.theme.AppTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import space.kodio.core.AudioQuality
import space.kodio.core.Kodio

@Composable
fun PitchTrackerScreen() {
    var audioQuality by rememberSaveable { mutableStateOf(AudioQuality.Standard) }
    var bufferSize by rememberSaveable { mutableStateOf(4096) }
    var isRecording by rememberSaveable { mutableStateOf(false) }
    var isPausing by rememberSaveable { mutableStateOf(false) }
    var a4Frequency by rememberSaveable { mutableStateOf(440) }
    var noteStyle by rememberSaveable { mutableStateOf(NoteNameStyle.Scientific) }

    var detectedFrequency by remember { mutableStateOf(0f) }
    val detector = remember(audioQuality, bufferSize) {
        PitchDetector(audioQuality.format.sampleRate, bufferSize)
    }

    LaunchedEffect(
        isRecording,
        isPausing,
        bufferSize
    ) {
        if (isRecording && !isPausing) {
            withContext(Dispatchers.Default) {
                val recorder = Kodio.recorder(quality = audioQuality)
                try {
                    recorder.start()
                    recorder.liveAudioFlow
                        ?.toAudioWindows(
                            windowSize = bufferSize,
                            hopSize = bufferSize / 2,
                            channels = audioQuality.format.channels.count
                        )
                        ?.collect { floatWindow ->
                            val frequency = detector.startDetection(floatWindow)
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
                } finally {
                    recorder.release()
                }
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
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
                        ControlPanel(
                            noteStyle = noteStyle,
                            a4Frequency = a4Frequency,
                            isRecording = isRecording,
                            isPausing = isPausing,
                            onStyleChange = { noteStyle = it },
                            onFrequencyChange = { a4Frequency = it },
                            onStartRecording = { isRecording = true },
                            onStopRecording = {
                                isRecording = false
                                isPausing = false
                                detectedFrequency = 0f
                            },
                            onPauseRecording = { isPausing = !isPausing }
                        )
                    }

                    Box(
                        modifier = Modifier.weight(1f).fillMaxHeight(),
                        contentAlignment = Alignment.Center
                    ) {
                        DisplayPanel(
                            detectedFrequency = detectedFrequency,
                            a4Frequency = a4Frequency,
                            noteStyle = noteStyle
                        )
                    }
                }
            } else {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceAround,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    ControlPanel(
                        noteStyle = noteStyle,
                        a4Frequency = a4Frequency,
                        isRecording = isRecording,
                        isPausing = isPausing,
                        onStyleChange = { noteStyle = it },
                        onFrequencyChange = { a4Frequency = it },
                        onStartRecording = { isRecording = true },
                        onStopRecording = {
                            isRecording = false
                            isPausing = false
                            detectedFrequency = 0f
                        },
                        onPauseRecording = { isPausing = !isPausing }
                    )
                    DisplayPanel(
                        detectedFrequency = detectedFrequency,
                        a4Frequency = a4Frequency,
                        noteStyle = noteStyle
                    )
                }
            }
        }
    }
}

@Composable
private fun ControlPanel(
    noteStyle: NoteNameStyle,
    a4Frequency: Int,
    isRecording: Boolean,
    isPausing: Boolean,
    onStyleChange: (NoteNameStyle) -> Unit,
    onFrequencyChange: (Int) -> Unit,
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
            selectedStyle = noteStyle,
            a4Frequency = a4Frequency,
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
    a4Frequency: Int,
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
fun PitchTrackerScreenPreview() {
    val platformData = getPlatform()
    AppTheme(platformData) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            PitchTrackerScreen()
        }
    }
}
