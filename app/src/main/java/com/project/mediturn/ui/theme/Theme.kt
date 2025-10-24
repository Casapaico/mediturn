package com.project.mediturn.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Colores médicos
private val MedicalBlue = Color(0xFF2196F3)
private val MedicalGreen = Color(0xFF4CAF50)
private val LightBlue = Color(0xFFBBDEFB)

private val DarkColorScheme = darkColorScheme(
    primary = MedicalBlue,
    secondary = MedicalGreen,
    tertiary = LightBlue
)

private val LightColorScheme = lightColorScheme(
    primary = MedicalBlue,
    secondary = MedicalGreen,
    tertiary = LightBlue
)

@Composable
fun MediTurnTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

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
        content = content
    )
}