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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.sor2171.audiodetectiontool.core.entity.NoteNameStyle
import com.github.sor2171.audiodetectiontool.getPlatform
import com.github.sor2171.audiodetectiontool.ui.component.PitchTrackerDisplayCard
import com.github.sor2171.audiodetectiontool.ui.component.RecordButtons
import com.github.sor2171.audiodetectiontool.ui.component.SettingCard
import com.github.sor2171.audiodetectiontool.ui.theme.AppTheme

@Composable
fun PitchTrackerScreen() {
    var isRecording by remember { mutableStateOf(false) }
    var isPausing by remember { mutableStateOf(false) }
    var a4Frequency by remember { mutableStateOf(440) }
    var constrainedCents by remember { mutableStateOf(0) }
    var noteStyle by remember { mutableStateOf(NoteNameStyle.Scientific) }

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

            @Composable
            fun ControlPanel(modifier: Modifier = Modifier) {
                Column(
                    modifier = modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    SettingCard(
                        selectedStyle = noteStyle,
                        a4Frequency = a4Frequency,
                        onStyleChange = { noteStyle = it },
                        onFrequencyChange = { a4Frequency = it }
                    )

                    RecordButtons(
                        isRecording = isRecording,
                        isPausing = isPausing,
                        onStartRecording = { isRecording = true },
                        onStopRecording = { isRecording = false; isPausing = false },
                        onPauseRecording = { isPausing = !isPausing }
                    )
                }
            }

            @Composable
            fun DisplayPanel(modifier: Modifier = Modifier) {
                PitchTrackerDisplayCard(
                    noteName = "A4",
                    cents = constrainedCents,
                    modifier = modifier
                )
            }

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
                        ControlPanel()
                    }

                    Box(
                        modifier = Modifier.weight(1f).fillMaxHeight(),
                        contentAlignment = Alignment.Center
                    ) {
                        DisplayPanel()
                    }
                }
            } else {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceAround,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    ControlPanel()
                    DisplayPanel()
                }
            }
        }
    }
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