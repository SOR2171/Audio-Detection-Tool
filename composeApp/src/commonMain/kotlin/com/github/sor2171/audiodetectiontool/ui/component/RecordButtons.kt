package com.github.sor2171.audiodetectiontool.ui.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.sor2171.audiodetectiontool.getPlatform
import com.github.sor2171.audiodetectiontool.ui.theme.AppTheme

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun RecordButtons(
    isRecording: Boolean,
    isPausing: Boolean,
    onStartRecording: () -> Unit,
    onStopRecording: () -> Unit,
    onPauseRecording: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.height(80.dp), contentAlignment = Alignment.Center
    ) {
        AnimatedContent(
            targetState = isRecording, transitionSpec = {
                if (isRecording) {
                    (scaleIn(
                        initialScale = 0.5f,
                        animationSpec = tween(durationMillis = 300)
                    ) + fadeIn(animationSpec = tween(durationMillis = 300))) togetherWith (scaleOut(
                        targetScale = 1.5f,
                        animationSpec = tween(durationMillis = 300)
                    ) + fadeOut(animationSpec = tween(durationMillis = 300)))
                } else {
                    (scaleIn(
                        initialScale = 1.5f,
                        animationSpec = tween(durationMillis = 300)
                    ) + fadeIn(animationSpec = tween(durationMillis = 300))) togetherWith (scaleOut(
                        targetScale = 0.5f,
                        animationSpec = tween(durationMillis = 300)
                    ) + fadeOut(animationSpec = tween(durationMillis = 300)))
                }
            }, label = "RecordButtonAnimation"
        ) { recording ->
            if (recording) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    FilledTonalButtonWithIcon(
                        onClick = onStopRecording,
                        icon = Icons.Default.Stop,
                        contentDescription = "Stop",
                        color = MaterialTheme.colorScheme.errorContainer,
                        modifier = Modifier.size(width = 130.dp, height = 64.dp)
                    )

                    ButtonWithIcon(
                        onClick = onPauseRecording,
                        icon = if (isPausing) Icons.Default.PlayArrow else Icons.Default.Pause,
                        contentDescription = if (isPausing) "Resume" else "Pause",
                        modifier = Modifier.size(width = 130.dp, height = 64.dp)
                    )
                }
            } else {
                Button(
                    onClick = onStartRecording,
                    modifier = Modifier.fillMaxWidth(0.8f).height(64.dp),
                    shape = CircleShape
                ) {
                    Icon(
                        Icons.Default.PlayArrow,
                        contentDescription = "Start",
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ButtonWithIcon(
    onClick: () -> Unit,
    icon: ImageVector,
    contentDescription: String,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(20.dp)
    ) {
        Icon(
            icon,
            contentDescription = contentDescription,
            modifier = Modifier.size(28.dp)
        )
    }
}

@Composable
fun FilledTonalButtonWithIcon(
    onClick: () -> Unit,
    icon: ImageVector,
    contentDescription: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    FilledTonalButton(
        onClick = onClick,
        modifier = modifier,
        colors = ButtonDefaults.filledTonalButtonColors(containerColor = color),
        shape = RoundedCornerShape(20.dp)
    ) {
        Icon(
            icon,
            contentDescription = contentDescription,
            modifier = Modifier.size(28.dp),
            tint = MaterialTheme.colorScheme.onErrorContainer
        )
    }
}

@Preview
@Composable
fun RecordButtonsPreview() {
    val platformData = getPlatform()
    AppTheme(platformData) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier.fillMaxSize().padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                RecordButtons(
                    isRecording = false,
                    isPausing = false,
                    onStartRecording = {},
                    onStopRecording = {},
                    onPauseRecording = {})

                RecordButtons(
                    isRecording = true,
                    isPausing = false,
                    onStartRecording = {},
                    onStopRecording = {},
                    onPauseRecording = {})

                RecordButtons(
                    isRecording = true,
                    isPausing = true,
                    onStartRecording = {},
                    onStopRecording = {},
                    onPauseRecording = {})
            }
        }
    }
}