package com.helpboldigraphi.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColors = darkColorScheme(
    primary = ElectricViolet,
    onPrimary = Color.White,
    primaryContainer = ElectricVioletDeep,
    secondary = RoyalGold,
    onSecondary = Color(0xFF1A1100),
    background = ObsidianCanvas,
    onBackground = ObsidianTextPrimary,
    surface = ObsidianSurfaceCard,
    onSurface = ObsidianTextPrimary,
    surfaceVariant = ObsidianSurfaceElevated,
    onSurfaceVariant = ObsidianTextSecondary,
    outline = ObsidianOutline
)

private val LightColors = lightColorScheme(
    primary = LightViolet,
    onPrimary = Color.White,
    primaryContainer = LightSurfaceLow,
    secondary = LightGold,
    onSecondary = Color.White,
    background = LightCanvas,
    onBackground = LightTextPrimary,
    surface = LightSurfaceCard,
    onSurface = LightTextPrimary,
    surfaceVariant = LightSurfaceElevated,
    onSurfaceVariant = LightTextSecondary,
    outline = LightOutline
)

@Composable
fun HelpBoldigraphiTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = BoldigraphiTypography,
        content = content
    )
}
