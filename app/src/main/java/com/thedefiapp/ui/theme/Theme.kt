package com.thedefiapp.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = Green200,
    primaryVariant = Green200,
    onPrimary = Color.Black,
    secondary = Yellow200,
    onSecondary = Color.Black,
    background = BlackLight,
    surface = BlackLight,
    onSurface = White,
    onBackground = White,
    error = Red,
    onError = Color.Black
)

@Composable
fun TheDefiAppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colors = DarkColorPalette,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
