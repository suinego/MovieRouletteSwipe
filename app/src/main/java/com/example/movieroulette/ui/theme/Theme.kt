package com.example.movieroulette.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val NeoNightColors = darkColorScheme(
    primary          = NeonCyan,
    secondary        = NeonMagenta,
    tertiary         = NeonPink,
    background       = DarkBg,
    surface          = DarkSurface,
    surfaceVariant   = DarkCard,
    onPrimary        = DarkBg,
    onSecondary      = DarkBg,
    onTertiary       = DarkBg,
    onBackground     = OnDark,
    onSurface        = OnDark,
    onSurfaceVariant = SubText,
)

@Composable
fun MovieRouletteTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = NeoNightColors,
        typography  = Typography,
        content     = content
    )
}
