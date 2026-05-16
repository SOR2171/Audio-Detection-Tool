package com.github.sor2171.audiodetectiontool.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import audiodetectiontool.composeapp.generated.resources.Res
import audiodetectiontool.composeapp.generated.resources.audio_quality
import audiodetectiontool.composeapp.generated.resources.buffer_size
import com.github.sor2171.audiodetectiontool.core.entity.NoteNameStyle
import com.github.sor2171.audiodetectiontool.core.utils.Const
import org.jetbrains.compose.resources.stringResource
import space.kodio.core.AudioQuality

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingCard(
    selectedBufferSize: Int,
    selectedQuality: AudioQuality,
    selectedStyle: NoteNameStyle,
    a4Frequency: Float,
    onStyleChange: (NoteNameStyle) -> Unit,
    onQualityChange: (AudioQuality) -> Unit,
    onBufferSizeChange: (Int) -> Unit,
    onFrequencyChange: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    var bufferSizeExpanded by remember { mutableStateOf(false) }
    var qualityExpanded by remember { mutableStateOf(false) }
    var noteStyleExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = modifier.width(300.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(12.dp)
                    .fillMaxWidth()
            ) {
                Row {
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        stringResource(Res.string.buffer_size) + ":",
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                    )
                }

                ButtonAndDropDownMenu(
                    length = 100.dp,
                    onClick = { bufferSizeExpanded = true },
                    displayText = selectedBufferSize.toString(),
                    menuText = { Text(it.toString()) },
                    expanded = bufferSizeExpanded,
                    onDismissRequest = { bufferSizeExpanded = false },
                    onClickMenu = { onBufferSizeChange(it) },
                    entries = Const.bufferSizes
                )
            }

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(12.dp)
                    .fillMaxWidth()
            ) {
                Row {
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        stringResource(Res.string.audio_quality) + ":",
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                    )
                }

                ButtonAndDropDownMenu(
                    length = 100.dp,
                    onClick = { qualityExpanded = true },
                    displayText = selectedQuality.format.sampleRate.toString() + "Hz",
                    menuText = { Text(it.format.sampleRate.toString() + "Hz") },
                    expanded = qualityExpanded,
                    onDismissRequest = { qualityExpanded = false },
                    onClickMenu = { onQualityChange(it) },
                    entries = AudioQuality.entries.toTypedArray()
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(12.dp)
            ) {
                var wrongFrequency by remember { mutableStateOf(false) }

                ButtonAndDropDownMenu(
                    length = 50.dp,
                    onClick = { noteStyleExpanded = true },
                    displayText = Const.symbolOfNoteStyle[selectedStyle]!!,
                    menuText = { Text(Const.symbolOfNoteStyle[it]!!) },
                    expanded = noteStyleExpanded,
                    onDismissRequest = { noteStyleExpanded = false },
                    onClickMenu = { onStyleChange(it) },
                    entries = NoteNameStyle.entries.toTypedArray()
                )

                Text("=", style = MaterialTheme.typography.titleLarge)

                OutlinedTextField(
                    suffix = { Text("Hz") },
                    onValueChange = {
                        val frequency = it.toFloatOrNull()
                        if (frequency != null && frequency > 0) {
                            onFrequencyChange(frequency)
                            wrongFrequency = false
                        } else {
                            wrongFrequency = true
                        }
                    },
                    value = a4Frequency.toString(),
                    singleLine = true,
                    modifier = Modifier.width(120.dp),
                    shape = RoundedCornerShape(12.dp),
                    isError = wrongFrequency,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.secondary
                    )
                )
            }
        }
    }
}

@Preview
@Composable
private fun SettingCardPreview() {
    SettingCard(
        selectedBufferSize = 2048,
        selectedStyle = NoteNameStyle.Scientific,
        selectedQuality = AudioQuality.Standard,
        a4Frequency = 440f,
        onBufferSizeChange = {},
        onQualityChange = {},
        onStyleChange = {},
        onFrequencyChange = {}
    )
}