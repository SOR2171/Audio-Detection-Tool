package com.github.sor2171.audiodetectiontool

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import audiodetectiontool.composeapp.generated.resources.Res
import audiodetectiontool.composeapp.generated.resources.app_name
import audiodetectiontool.composeapp.generated.resources.icon
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = stringResource(Res.string.app_name),
        state = rememberWindowState(width = 1100.dp, height = 800.dp),
        icon = painterResource(Res.drawable.icon)
    ) {
        App()
    }
}