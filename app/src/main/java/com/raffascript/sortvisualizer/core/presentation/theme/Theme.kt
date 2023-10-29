package com.raffascript.sortvisualizer.core.presentation.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = LapisLazuli,
    secondary = MetallicBlue,
    tertiary = MetallicBlue,
    onPrimary = White,
    onSecondary = White,
    onTertiary = White,
    primaryContainer = LapisLazuli,
    secondaryContainer = MetallicBlue,
    tertiaryContainer = MetallicBlue,
    onPrimaryContainer = White,
    onSecondaryContainer = White,
    onTertiaryContainer = White,
    background = RaisinBlack,
    surface = Onyx,
    onBackground = White,
    onSurface = White
    )

private val LightColorScheme = lightColorScheme(
    primary = LapisLazuli,
    secondary = MetallicBlue,
    tertiary = MetallicBlue,
    onPrimary = White,
    onSecondary = White,
    onTertiary = White,
    primaryContainer = LapisLazuli,
    secondaryContainer = MetallicBlue,
    tertiaryContainer = MetallicBlue,
    onPrimaryContainer = White,
    onSecondaryContainer = White,
    onTertiaryContainer = White,
    background = White,
    surface = White,
    onBackground = Black,
    onSurface = Black
)

@Composable
fun AlgorithmsVisualizerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        /*dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }*/
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}