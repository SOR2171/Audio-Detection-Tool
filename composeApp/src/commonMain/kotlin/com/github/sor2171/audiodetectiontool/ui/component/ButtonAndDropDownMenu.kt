package com.github.sor2171.audiodetectiontool.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp

@Composable
fun <T> ButtonAndDropDownMenu(
    length: Dp,
    onClick: () -> Unit,
    displayText: String,
    menuText: @Composable (enumItem: T) -> Unit,
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    onClickMenu: (T) -> Unit,
    entries: Array<T>,
) {
    ElevatedButton(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.width(length)
        ) {
            Text(
                displayText,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
            Icon(
                Icons.Default.ArrowDropDown,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = onDismissRequest,
            offset = DpOffset(0.dp, 8.dp)
        ) {
            entries.forEach { enumItem ->
                DropdownMenuItem(
                    text = { menuText(enumItem) },
                    onClick = {
                        onClickMenu(enumItem)
                        onDismissRequest()
                    }
                )
            }
        }
    }
}