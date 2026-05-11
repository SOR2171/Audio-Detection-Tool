package com.github.sor2171.audiodetectiontool

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "AudioDetectionTool",
    ) {
        App()
    }
}