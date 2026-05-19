package com.github.sor2171.audiodetectiontool

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.github.sor2171.audiodetectiontool.core.entity.NoteNameStyle
import com.github.sor2171.audiodetectiontool.core.entity.Screens
import com.github.sor2171.audiodetectiontool.ui.screens.PitchTrackerScreen
import com.github.sor2171.audiodetectiontool.ui.screens.SpectrumScreen
import com.github.sor2171.audiodetectiontool.ui.theme.AppTheme
import space.kodio.core.AudioQuality

@Composable
@Preview
fun App() {
    val platformData = getPlatform()

    var bufferSize by rememberSaveable { mutableStateOf(2048) }
    var audioQuality by rememberSaveable { mutableStateOf(AudioQuality.Standard) }
    var a4Frequency by rememberSaveable { mutableStateOf(440f) }
    var noteStyle by rememberSaveable { mutableStateOf(NoteNameStyle.Scientific) }

    var isRecording by rememberSaveable { mutableStateOf(false) }
    var isPausing by rememberSaveable { mutableStateOf(false) }

    var currentScreen by rememberSaveable { mutableStateOf(Screens.PitchDetector) }

    @Composable
    fun NavigationRailItems() {
        Column(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Screens.entries.forEach { screen ->
                val icon = @Composable {
                    Icon(
                        screen.icon,
                        contentDescription = screen.title()
                    )
                }
                val label = @Composable { Text(screen.title()) }
                val selected = currentScreen == screen
                val onClick = { currentScreen = screen }

                NavigationRailItem(
                    selected = selected,
                    onClick = onClick,
                    icon = icon,
                    label = label
                )
            }
        }
    }

    @Composable
    fun RowScope.NavigationBarItems() {
        Screens.entries.forEach { screen ->
            val icon = @Composable {
                Icon(
                    screen.icon,
                    contentDescription = screen.title()
                )
            }
            val label = @Composable { Text(screen.title()) }
            val selected = currentScreen == screen
            val onClick = { currentScreen = screen }

            NavigationBarItem(
                selected = selected,
                onClick = onClick,
                icon = icon,
                label = label
            )
        }
    }

    val pitchTrackerScreen = @Composable { ->
        PitchTrackerScreen(
            bufferSize = bufferSize,
            audioQuality = audioQuality,
            a4Frequency = a4Frequency,
            noteStyle = noteStyle,
            isRecording = isRecording,
            isPausing = isPausing,
            onBufferSizeChange = { bufferSize = it },
            onQualityChange = { audioQuality = it },
            onStyleChange = { noteStyle = it },
            onFrequencyChange = { a4Frequency = it },
            onStartRecording = { isRecording = true },
            onStopRecording = { isRecording = false; isPausing = false },
            onPauseRecording = { isPausing = !isPausing }
        )
    }

    val spectrumScreen = @Composable { ->
        SpectrumScreen(
            bufferSize = bufferSize,
            audioQuality = audioQuality,
            a4Frequency = a4Frequency,
            noteStyle = noteStyle,
            isRecording = isRecording,
            isPausing = isPausing,
            onBufferSizeChange = { bufferSize = it },
            onQualityChange = { audioQuality = it },
            onStyleChange = { noteStyle = it },
            onFrequencyChange = { a4Frequency = it },
            onStartRecording = { isRecording = true },
            onStopRecording = { isRecording = false; isPausing = false },
            onPauseRecording = { isPausing = !isPausing }
        )
    }


    AppTheme(platformData) {
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val showNavigationRail = maxWidth > maxHeight

            Row(modifier = Modifier.fillMaxSize()) {
                if (showNavigationRail) {
                    NavigationRail(
                        containerColor = NavigationBarDefaults.containerColor
                    ) {
                        NavigationRailItems()
                    }
                }

                Scaffold(
                    modifier = Modifier.weight(1f),
                    bottomBar = {
                        if (!showNavigationRail) {
                            NavigationBar {
                                NavigationBarItems()
                            }
                        }
                    }
                ) { innerPadding ->
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        AnimatedContent(
                            targetState = currentScreen,
                            transitionSpec = { fadeIn().togetherWith(fadeOut()) },
                            label = "ScreenTransition"
                        ) { screen ->
                            when (screen) {
                                Screens.PitchDetector -> pitchTrackerScreen()
                                Screens.Spectrum -> spectrumScreen()
                            }
                        }
                    }
                }
            }
        }
    }
}