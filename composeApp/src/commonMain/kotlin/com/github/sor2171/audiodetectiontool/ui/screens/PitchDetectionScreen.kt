package com.github.sor2171.audiodetectiontool.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.sor2171.audiodetectiontool.core.entity.NoteNameStyle
import com.github.sor2171.audiodetectiontool.getPlatform
import com.github.sor2171.audiodetectiontool.ui.component.RecordButtons
import com.github.sor2171.audiodetectiontool.ui.component.SettingCard
import com.github.sor2171.audiodetectiontool.ui.theme.AppTheme
import kotlin.math.abs

@Composable
fun PitchTrackerScreen() {
    var isRecording by remember { mutableStateOf(false) }
    var isPausing by remember { mutableStateOf(false) }
    var a4Frequency by remember { mutableStateOf(440) }
    var constrainedCents by remember { mutableStateOf(0) }
    var noteStyle by remember { mutableStateOf(NoteNameStyle.Scientific) }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            SettingCard(
                selectedStyle = noteStyle,
                a4Frequency = a4Frequency,
                onStyleChange = { noteStyle = it },
                onFrequencyChange = { a4Frequency = it }
            )

            RecordButtons(
                isRecording = isRecording,
                onStartRecording = { isRecording = true },
                onStopRecording = { isRecording = false; isPausing = false },
                onPauseRecording = { isPausing = !isPausing }
            )
        }

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val colorOutline = MaterialTheme.colorScheme.outline
                val colorPrimary = MaterialTheme.colorScheme.primary
                val colorSecondary = MaterialTheme.colorScheme.secondary
                val colorError = MaterialTheme.colorScheme.error

                Card(
                    elevation = CardDefaults.cardElevation(4.dp),
                    colors = CardDefaults.cardColors(colorSecondary),
                    modifier = Modifier.padding(16.dp).size(100.dp)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "A4",
                            style = MaterialTheme.typography.headlineLarge
                        )
                    }
                }

                Canvas(
                    modifier = Modifier.fillMaxWidth().height(40.dp).padding(horizontal = 20.dp)
                ) {
                    val width = size.width
                    val height = size.height
                    val centerY = height / 2
                    val centerX = width / 2

                    drawLine(
                        color = colorOutline,
                        start = Offset(0f, centerY),
                        end = Offset(width, centerY),
                        strokeWidth = 2.dp.toPx()
                    )

                    drawLine(
                        color = colorSecondary,
                        start = Offset(centerX, centerY - 15.dp.toPx()),
                        end = Offset(centerX, centerY + 15.dp.toPx()),
                        strokeWidth = 3.dp.toPx()
                    )

                    val indicatorX = centerX + (constrainedCents / 50f) * centerX

                    drawCircle(
                        color = when {
                            abs(constrainedCents) < 5f -> colorPrimary
                            abs(constrainedCents) < 20f -> colorSecondary
                            else -> colorError
                        },
                        radius = 8.dp.toPx(),
                        center = Offset(indicatorX, centerY)
                    )
                }
                Text(constrainedCents.toString())
            }
        }
    }
}

@Preview
@Composable
fun PitchTrackerScreenPreview() {
    val platformData = getPlatform()
    AppTheme(platformData) {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            PitchTrackerScreen()
        }
    }
}