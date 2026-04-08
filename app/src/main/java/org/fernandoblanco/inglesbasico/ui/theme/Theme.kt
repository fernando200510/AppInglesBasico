package org.fernandoblanco.inglesbasico.ui.theme

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

private val AppLight = lightColorScheme(
    primary = PlayBlue,
    onPrimary = Color.White,
    primaryContainer = KidCardBlue,
    onPrimaryContainer = PlayInk,
    secondary = PlayPurple,
    onSecondary = Color.White,
    secondaryContainer = PlayPurpleSoft,
    onSecondaryContainer = PlayInk,
    tertiary = PlayGreen,
    onTertiary = Color.White,
    tertiaryContainer = PlayGreenSoft,
    onTertiaryContainer = PlayInk,
    error = PlayError,
    onError = Color.White,
    background = PlayCream,
    onBackground = PlayInk,
    surface = PlaySurface,
    onSurface = PlayInk,
    surfaceVariant = Color(0xFFF0F4FF),
    onSurfaceVariant = PlayInk,
    outline = PlayBlue.copy(alpha = 0.35f)
)

private val AppDark = darkColorScheme(
    primary = PlayBlue,
    onPrimary = PlayInk,
    primaryContainer = PlayBlueDark,
    onPrimaryContainer = Color.White,
    secondary = PlayPurpleSoft,
    onSecondary = PlayInk,
    tertiary = PlayGreen,
    onTertiary = Color.White,
    background = Color(0xFF12141C),
    onBackground = Color(0xFFF5F5F5),
    surface = Color(0xFF1E2230),
    onSurface = Color(0xFFF5F5F5),
    surfaceVariant = Color(0xFF2A2F42),
    onSurfaceVariant = Color(0xFFE0E4F0)
)

@Composable
fun InglesBasicoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    @Suppress("UNUSED_PARAMETER") dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) AppDark else AppLight
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
