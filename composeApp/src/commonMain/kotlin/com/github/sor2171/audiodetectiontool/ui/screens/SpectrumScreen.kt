package com.github.sor2171.audiodetectiontool.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import audiodetectiontool.composeapp.generated.resources.Res
import audiodetectiontool.composeapp.generated.resources.settings
import com.github.sor2171.audiodetectiontool.core.entity.NoteNameStyle
import com.github.sor2171.audiodetectiontool.core.service.SpectrumAnalyzer
import com.github.sor2171.audiodetectiontool.getPlatform
import com.github.sor2171.audiodetectiontool.ui.component.RecordButtons
import com.github.sor2171.audiodetectiontool.ui.component.RecorderController
import com.github.sor2171.audiodetectiontool.ui.component.SettingCard
import com.github.sor2171.audiodetectiontool.ui.theme.AppTheme
import org.jetbrains.compose.resources.stringResource
import space.kodio.core.AudioQuality
import kotlin.math.abs
import kotlin.math.pow

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
    val analyzer = remember(bufferSize) { SpectrumAnalyzer(bufferSize) }
    val spectrumData by analyzer.spectrumState.collectAsState()
    val historyBuffer = remember { mutableStateListOf<List<Float>>() }
    val textMeasurer = rememberTextMeasurer()

    var showSettingDialog by remember { mutableStateOf(false) }
    var historySize by remember { mutableStateOf(50) }

    val pianoBinRanges = remember(
        audioQuality,
        bufferSize,
        a4Frequency
    ) {
        val ranges = mutableListOf<IntRange>()
        val resolution = audioQuality.format.sampleRate.toFloat() / bufferSize
        for (n in 1..88) {
            val fLow = a4Frequency * 2f.pow((n - 0.5f - 49) / 12f)
            val fHigh = a4Frequency * 2f.pow((n + 0.5f - 49) / 12f)

            val maxBin = bufferSize / 2 - 1
            val binLow = (fLow / resolution).toInt().coerceIn(0, maxBin)
            val binHigh = (fHigh / resolution).toInt().coerceIn(0, maxBin)
            if (binLow > binHigh) {

                val fCenter = a4Frequency * 2f.pow((n - 49) / 12f)
                val binCenter = (fCenter / resolution).toInt().coerceIn(0, maxBin)
                ranges.add(binCenter..binCenter)
            } else {
                ranges.add(binLow..binHigh)
            }
        }
        ranges
    }

    RecorderController(
        isRecording = isRecording,
        isPausing = isPausing,
        audioQuality = audioQuality,
        bufferSize = bufferSize,
        performOnWindow = { floatWindow ->
            analyzer.processAudioWindow(floatWindow)
        }
    )

    if (showSettingDialog) {
        Dialog(
            onDismissRequest = { showSettingDialog = false }
        ) {
            SettingCard(
                selectedBufferSize = bufferSize,
                selectedQuality = audioQuality,
                selectedStyle = noteStyle,
                a4Frequency = a4Frequency,
                onStyleChange = onStyleChange,
                onQualityChange = onQualityChange,
                onBufferSizeChange = onBufferSizeChange,
                onFrequencyChange = onFrequencyChange
            )
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            RecordButtons(
                isRecording = isRecording,
                isPausing = isPausing,
                onStartRecording = onStartRecording,
                onStopRecording = { onStopRecording(); historyBuffer.clear() },
                onPauseRecording = onPauseRecording
            )

            Spacer(modifier = Modifier.fillMaxWidth(0.1f))

            IconButton(
                onClick = { showSettingDialog = true },
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = stringResource(Res.string.settings)
                )
            }

            Spacer(modifier = Modifier.fillMaxWidth(0.1f))
        }

        LaunchedEffect(spectrumData) {
            if (spectrumData.isEmpty()) return@LaunchedEffect
            val pianoData = pianoBinRanges.map { binRange ->
                var maxEnergy = -80f
                for (i in binRange) {
                    if (i < spectrumData.size) {
                        if (spectrumData[i] > maxEnergy) maxEnergy = spectrumData[i]
                    }
                }
                maxEnergy
            }
            historyBuffer.add(pianoData)
            if (historyBuffer.size > historySize) {
                historyBuffer.removeFirst()
            }
        }

        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {
            if (historyBuffer.isEmpty()) return@Canvas
            val sliceWidth = size.width / historySize
            val binHeight = size.height / 88f
            historyBuffer.forEachIndexed { timeIndex, pianoSlice ->
                val x = timeIndex * sliceWidth
                pianoSlice.forEachIndexed { pianoIndex, dbValue ->

                    val r = (1.5f - abs(4f * dbValue - 3f)).coerceIn(0f, 1f)
                    val g = (1.5f - abs(4f * dbValue - 2f)).coerceIn(0f, 1f)
                    val b = (1.5f - abs(4f * dbValue - 1f)).coerceIn(0f, 1f)

                    val color = Color(red = r, green = g, blue = b)
                    val y = size.height - (pianoIndex + 1) * binHeight
                    drawRect(
                        color = color,
                        topLeft = Offset(x = x, y = y),
                        size = Size(width = sliceWidth + 1f, height = binHeight + 1f)
                    )
                }
            }

            // White separators for each octave
            for (i in 0..7) {
                val bIndex = 2 + 12 * i
                val separatorY = size.height - (bIndex + 1) * binHeight
                drawLine(
                    color = Color.White,
                    start = Offset(0f, separatorY),
                    end = Offset(size.width, separatorY),
                    strokeWidth = 1f
                )
            }

            // C4 label
            val c4Index = 39
            val c4LineY = size.height - c4Index * binHeight
            val c4LayoutResult = textMeasurer.measure(
                text = "C4",
                style = TextStyle(
                    color = Color.White,
                    fontSize = 12.sp,
                    background = Color.Black.copy(alpha = 0.5f)
                )
            )
            drawText(
                textLayoutResult = c4LayoutResult,
                topLeft = Offset(
                    x = 4.dp.toPx(),
                    y = c4LineY - c4LayoutResult.size.height
                )
            )
        }
    }
}

@Preview
@Composable
private fun SpectrumScreenPreview() {
    val platformData = getPlatform()
    AppTheme(platformData) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            SpectrumScreen(
                bufferSize = 2048,
                audioQuality = AudioQuality.High,
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