package com.pri.artsysearchapp.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.isSystemInDarkTheme

private val LightColors = lightColorScheme(
    primary = Color(0xFF2E4A7D),          // Navy blue
    onPrimary = Color.White,
    primaryContainer = Color(0xFFC5CAE9), // AppBar background
    onPrimaryContainer = Color.Black,
    secondary = Color(0xFF64748B),
    onSecondary = Color.White,
    background = Color(0xFFFAFAFA),       // Overall background
    onBackground = Color.Black,
    surface = Color.White,
    onSurface = Color.Black
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFF90B4ED),
    onPrimary = Color.Black,
    primaryContainer = Color(0xFF2E4A7D),
    onPrimaryContainer = Color.White,
    secondary = Color(0xFF94A3B8),
    onSecondary = Color.Black,
    background = Color(0xFF121212),
    onBackground = Color.White,
    surface = Color(0xFF1E293B),
    onSurface = Color.White
)

@Composable
fun ArtsySearchAppTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (useDarkTheme) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colors,
        typography = Typography(),
        shapes = Shapes(),
        content = content
    )
}