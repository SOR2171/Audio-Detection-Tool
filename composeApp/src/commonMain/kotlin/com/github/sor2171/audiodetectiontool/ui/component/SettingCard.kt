package com.github.sor2171.audiodetectiontool.ui.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.sor2171.audiodetectiontool.core.entity.NoteNameStyle
import com.github.sor2171.audiodetectiontool.core.ultils.Const

@Composable
fun SettingCard(
    selectedStyle: NoteNameStyle,
    a4Frequency: Int,
    onStyleChange: (NoteNameStyle) -> Unit,
    onFrequencyChange: (Int) -> Unit
) {
    var noteStyleExpanded by remember { mutableStateOf(false) }
    Card(
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Row(
            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(6.dp),
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
            modifier = androidx.compose.ui.Modifier.padding(8.dp)
        ) {
            OutlinedButton(
                onClick = { noteStyleExpanded = true }
            ) {
                Text(Const.symbolOfNoteStyle[selectedStyle]!!)
                Icon(
                    Icons.Default.ArrowDropDown,
                    contentDescription = null
                )
            }

            DropdownMenu(
                expanded = noteStyleExpanded,
                onDismissRequest = { noteStyleExpanded = false }
            ) {
                NoteNameStyle.entries.forEach { style ->
                    DropdownMenuItem(
                        text = { Text(style.name) },
                        onClick = {
                            onStyleChange(style)
                            noteStyleExpanded = false
                        }
                    )
                }
            }

            Text("=", style = androidx.compose.material3.MaterialTheme.typography.headlineSmall)

            OutlinedTextField(
                suffix = { Text("Hz") },
                onValueChange = {
                    val frequency = it.toIntOrNull()
                    if (frequency != null) {
                        onStyleChange(selectedStyle)
                    } else {
                        onFrequencyChange(440)
                    }
                },
                value = a4Frequency.toString(),
                singleLine = true,
                modifier = androidx.compose.ui.Modifier.width(100.dp)
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