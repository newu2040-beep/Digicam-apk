package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import com.example.data.model.AppTheme

@Composable
fun DigiCamTheme(
    appTheme: AppTheme = AppTheme.MATCHA,
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val palette = getThemePalette(appTheme, darkTheme)
    val colorScheme = if (darkTheme) {
        darkColorScheme(
            primary = palette.primary,
            onPrimary = palette.onPrimary,
            primaryContainer = palette.primaryContainer,
            onPrimaryContainer = palette.onPrimaryContainer,
            secondary = palette.secondary,
            tertiary = palette.tertiary,
            background = palette.background,
            surface = palette.surface,
            surfaceVariant = palette.surfaceVariant,
            onSurface = palette.onSurface
        )
    } else {
        lightColorScheme(
            primary = palette.primary,
            onPrimary = palette.onPrimary,
            primaryContainer = palette.primaryContainer,
            onPrimaryContainer = palette.onPrimaryContainer,
            secondary = palette.secondary,
            tertiary = palette.tertiary,
            background = palette.background,
            surface = palette.surface,
            surfaceVariant = palette.surfaceVariant,
            onSurface = palette.onSurface
        )
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
