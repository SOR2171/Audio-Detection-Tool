package com.github.sor2171.audiodetectiontool

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable

interface Platform {
    val name: String
    @Composable
    fun getDynamicColorScheme(darkTheme: Boolean): ColorScheme? = null
}

expect fun getPlatform(): Platform