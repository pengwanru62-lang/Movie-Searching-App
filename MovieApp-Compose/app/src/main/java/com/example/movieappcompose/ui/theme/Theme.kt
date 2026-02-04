package com.example.movieappcompose.ui.theme

import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = Color(0xFF1F8EF1),
    primaryVariant = Color(0xFF0F6BE6),
    secondary = Color(0xFF6AD1FF)
)

@Composable
fun MovieAppComposeTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colors = DarkColorPalette,
        typography = AppTypography, // use renamed typography instance
        shapes = Shapes(),
        content = content
    )
}
