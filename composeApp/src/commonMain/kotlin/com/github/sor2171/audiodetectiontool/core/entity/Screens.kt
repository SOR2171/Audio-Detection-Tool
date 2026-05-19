package com.github.sor2171.audiodetectiontool.core.entity

import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Mic
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import audiodetectiontool.composeapp.generated.resources.Res
import audiodetectiontool.composeapp.generated.resources.pitch_detector
import audiodetectiontool.composeapp.generated.resources.spectrum
import org.jetbrains.compose.resources.stringResource

enum class Screens(
    val title: @Composable () -> String,
    val icon: ImageVector
) {
    PitchDetector(
        title = { stringResource(Res.string.pitch_detector) },
        icon = androidx.compose.material.icons.Icons.Default.Mic
    ),

    Spectrum(
        title = { stringResource(Res.string.spectrum) },
        icon = androidx.compose.material.icons.Icons.Default.Analytics
    )
}