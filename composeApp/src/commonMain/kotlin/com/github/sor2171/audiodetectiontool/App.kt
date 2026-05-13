package com.github.sor2171.audiodetectiontool

import androidx.compose.material3.Surface
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.github.sor2171.audiodetectiontool.ui.theme.AppTheme
import com.github.sor2171.audiodetectiontool.ui.screens.PitchTrackerScreen

@Composable
@Preview
fun App() {
    val platformData = getPlatform()
    AppTheme(platformData) {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            PitchTrackerScreen()
        }
    }
}