package com.danstoll.kenkencompose

import androidx.compose.Composable
import androidx.ui.graphics.Color
import androidx.ui.material.ColorPalette
import androidx.ui.material.MaterialTheme
import androidx.ui.material.surface.Surface

private val green = Color(0xFF1EB980)
private val themeColors = ColorPalette(
    primary = green,
    surface = Color.LightGray,
    onSurface = Color.Black
)

private val lightThemeColors = ColorPalette(
    primary = Color(0xFFDD0D3C),
    primaryVariant = Color(0xFFC20029),
    onPrimary = Color.White,
    secondary = Color.White,
    onSecondary = Color.Black,
    background = Color.White,
    onBackground = Color.Black,
    surface = Color.White,
    onSurface = Color.Black,
    error = Color(0xFFD00036),
    onError = Color.White
)

const val appBarText = "KenKen Compose!"

@Composable
fun MyAppTheme(children: @Composable() () -> Unit) {
    MaterialTheme(colors = themeColors) {
            children()
    }
}