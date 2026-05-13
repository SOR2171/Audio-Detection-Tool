package com.github.sor2171.audiodetectiontool.ui.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Button
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.sor2171.audiodetectiontool.getPlatform
import com.github.sor2171.audiodetectiontool.ui.theme.AppTheme

@Composable
fun RecordButtons(
    isRecording: Boolean,
    onStartRecording: () -> Unit,
    onStopRecording: () -> Unit,
    onPauseRecording: () -> Unit
) {
    Box(
        modifier = Modifier.width(300.dp).height(80.dp),
        contentAlignment = Alignment.Center
    ) {
        AnimatedContent(
            targetState = isRecording,
            transitionSpec = {
                (fadeIn() + scaleIn(initialScale = 0.8f))
                    .togetherWith(fadeOut() + scaleOut(targetScale = 0.8f))
                    .using(SizeTransform(clip = false))
            },
            label = "RecordButtonAnimation"
        ) { recording ->
            if (recording) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    FilledTonalButton(
                        onClick = onStopRecording,
                        modifier = Modifier.size(width = 120.dp, height = 56.dp),
                        shape = RoundedCornerShape(16.dp),
                    ) {
                        Icon(Icons.Default.Stop, contentDescription = "Stop")
                    }

                    Button(
                        onClick = onPauseRecording,
                        modifier = Modifier.size(width = 120.dp, height = 56.dp),
                        shape = RoundedCornerShape(16.dp),
                    ) {
                        Icon(Icons.Default.Pause, contentDescription = "Pause")
                    }
                }
            } else {
                Button(
                    onClick = onStartRecording,
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Icon(Icons.Default.PlayArrow, contentDescription = "Start")
                }
            }
        }
    }
}

@Preview
@Composable
fun RecordButtonsPreview() {
    val platformData = getPlatform()
    AppTheme(platformData) {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier.fillMaxSize().padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                RecordButtons(
                    isRecording = false,
                    onStartRecording = {},
                    onStopRecording = {},
                    onPauseRecording = {}
                )

                RecordButtons(
                    isRecording = true,
                    onStartRecording = {},
                    onStopRecording = {},
                    onPauseRecording = {}
                )
            }
        }
    }
}