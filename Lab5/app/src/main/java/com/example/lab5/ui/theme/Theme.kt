package com.example.teaencyclopedia.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

import androidx.compose.material3.Typography

val AppTypography = Typography()

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF8BC34A),
    onPrimary = Color.White,
    secondary = Color(0xFF4CAF50),
    onSecondary = Color.White,
    background = Color(0xFFF1F8E9),
    onBackground = Color.Black,
    surface = Color.White,
    onSurface = Color.Black
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF8BC34A),
    onPrimary = Color.Black,
    secondary = Color(0xFF4CAF50),
    onSecondary = Color.Black,
    background = Color(0xFF303030),
    onBackground = Color.White,
    surface = Color(0xFF424242),
    onSurface = Color.White
)

@Composable
fun TeaEncyclopediaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography, // Используем AppTypography
        content = content
    )
}

