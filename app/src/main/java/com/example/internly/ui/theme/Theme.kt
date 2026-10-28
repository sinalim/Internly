package com.example.internly.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Blue,
    onPrimary = Color.White,
    secondary = Navy,
    background = Color.White,
    surface = Color.White,
)

@Composable
fun InternlyTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = LightColors, content = content)
}
