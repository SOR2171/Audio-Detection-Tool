package com.github.sor2171.audiodetectiontool.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.github.sor2171.audiodetectiontool.core.entity.NoteNameStyle
import com.github.sor2171.audiodetectiontool.core.ultils.Const

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingCard(
    selectedStyle: NoteNameStyle,
    a4Frequency: Int,
    onStyleChange: (NoteNameStyle) -> Unit,
    onFrequencyChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var noteStyleExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(2.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults
            .cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(12.dp)
        ) {
            Box {
                ElevatedButton(
                    onClick = { noteStyleExpanded = true },
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.width(50.dp)
                    ){
                        Text(
                            Const.symbolOfNoteStyle[selectedStyle]!!,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                        Icon(
                            Icons.Default.ArrowDropDown,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                }

                DropdownMenu(
                    expanded = noteStyleExpanded,
                    onDismissRequest = { noteStyleExpanded = false },
                    offset = DpOffset(0.dp, 8.dp)
                ) {
                    NoteNameStyle.entries.forEach { style ->
                        DropdownMenuItem(
                            text = { Text(Const.symbolOfNoteStyle[style]!!) },
                            onClick = {
                                onStyleChange(style)
                                noteStyleExpanded = false
                            }
                        )
                    }
                }
            }

            Text("=", style = MaterialTheme.typography.titleLarge)

            OutlinedTextField(
                suffix = { Text("Hz") },
                onValueChange = {
                    val frequency = it.toIntOrNull()
                    if (frequency != null) {
                        onFrequencyChange(frequency)
                    } else {
                        onFrequencyChange(440)
                    }
                },
                value = a4Frequency.toString(),
                singleLine = true,
                modifier = Modifier.width(120.dp),
                shape = RoundedCornerShape(12.dp),
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

@Preview
@Composable
fun SettingCardPreview() {
    SettingCard(
        selectedStyle = NoteNameStyle.Scientific,
        a4Frequency = 440,
        onStyleChange = {},
        onFrequencyChange = {}
    )
}