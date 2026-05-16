package com.github.sor2171.audiodetectiontool.ui.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import audiodetectiontool.composeapp.generated.resources.Res
import audiodetectiontool.composeapp.generated.resources.cent
import org.jetbrains.compose.resources.stringResource
import kotlin.math.abs

@Composable
fun PitchTrackerDisplayCard(
    noteName: String,
    cents: Int,
    modifier: Modifier = Modifier
) {
    val animatedCents by animateFloatAsState(
        targetValue = cents.toFloat(),
        label = "CentsAnimation"
    )

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val colorScheme = MaterialTheme.colorScheme
            val textStyle = MaterialTheme.typography

            Card(
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(6.dp),
                colors = CardDefaults.cardColors(containerColor = colorScheme.secondary),
                modifier = Modifier.padding(bottom = 16.dp).size(120.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = noteName,
                        style = textStyle.displayLarge.copy(color = colorScheme.onSecondary)
                    )
                }
            }

            Canvas(
                modifier = Modifier.fillMaxWidth().height(60.dp).padding(horizontal = 10.dp)
            ) {
                val width = size.width
                val height = size.height
                val centerY = height / 2
                val centerX = width / 2
                val outlineColor = colorScheme.outline
                val primaryColor = colorScheme.primary
                val secondaryColor = colorScheme.secondary
                val errorColor = colorScheme.error

                drawRoundRect(
                    color = secondaryColor,
                    topLeft = Offset(centerX - 4.dp.toPx(), centerY - 20.dp.toPx()),
                    size = Size(8.dp.toPx(), 40.dp.toPx()),
                    cornerRadius = CornerRadius(4.dp.toPx())
                )

                for (i in -50..50) {
                    val x = centerX + (i / 50f) * centerX
                    val tickHeight = when {
                        i % 10 == 0 -> 25.dp.toPx()
                        i % 5 == 0 -> 15.dp.toPx()
                        else -> 8.dp.toPx()
                    }
                    val strokeWidth = when {
                        i % 10 == 0 -> 2.dp.toPx()
                        i % 5 == 0 -> 1.5.dp.toPx()
                        else -> 1.dp.toPx()
                    }

                    if (i != 0) {
                        drawLine(
                            color = outlineColor,
                            start = Offset(x, centerY - tickHeight / 2),
                            end = Offset(x, centerY + tickHeight / 2),
                            strokeWidth = strokeWidth
                        )
                    }
                }

                val indicatorX = centerX + (animatedCents / 50f) * centerX
                val absCents = abs(animatedCents)
                drawCircle(
                    color = when {
                        absCents < 5f -> primaryColor
                        absCents < 20f -> secondaryColor
                        else -> errorColor
                    },
                    radius = 12.dp.toPx(),
                    center = Offset(indicatorX, centerY)
                )
            }

            Text(
                text = (if (cents > 0) "+" else "") + "$cents ${stringResource(Res.string.cent)}",
                style = textStyle.bodyLarge.copy(color = colorScheme.onSurfaceVariant)
            )
        }
    }
}

@Preview
@Composable
private fun PitchTrackerDisplayCardPreview() {
    MaterialTheme {
        Column(
            modifier = Modifier.padding(24.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            PitchTrackerDisplayCard(noteName = "A4", cents = 0)
            PitchTrackerDisplayCard(noteName = "A#4", cents = 15)
            PitchTrackerDisplayCard(noteName = "G4", cents = -35)
        }
    }
}