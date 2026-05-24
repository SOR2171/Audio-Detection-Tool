package com.github.sor2171.audiodetectiontool.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily

import audiodetectiontool.composeapp.generated.resources.NotoSansSC_VariableFont_wght
import audiodetectiontool.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.Font

@Composable
actual fun getAppFontFamily(): FontFamily = FontFamily(
    Font(Res.font.NotoSansSC_VariableFont_wght)
)
