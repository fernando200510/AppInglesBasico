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

private val KidLight = lightColorScheme(
    primary = KidTurquoise,
    onPrimary = Color.White,
    primaryContainer = KidMint,
    onPrimaryContainer = KidDeep,
    secondary = KidSun,
    onSecondary = KidDeep,
    secondaryContainer = Color(0xFFFFF3CC),
    onSecondaryContainer = KidDeep,
    tertiary = KidPurple,
    onTertiary = Color.White,
    tertiaryContainer = KidLavender,
    onTertiaryContainer = KidDeep,
    error = KidCoral,
    onError = Color.White,
    background = KidCream,
    onBackground = KidDeep,
    surface = Color.White,
    onSurface = KidDeep,
    surfaceVariant = KidMint,
    onSurfaceVariant = KidDeep,
    outline = KidTurquoiseDark.copy(alpha = 0.45f)
)

private val KidDark = darkColorScheme(
    primary = KidSky,
    onPrimary = KidDeep,
    primaryContainer = KidTurquoiseDark,
    onPrimaryContainer = Color.White,
    secondary = KidSun,
    onSecondary = KidDeep,
    tertiary = KidLavender,
    onTertiary = KidDeep,
    background = KidDeep,
    onBackground = Color(0xFFF5F5F5),
    surface = Color(0xFF3A3F52),
    onSurface = Color(0xFFF5F5F5),
    surfaceVariant = Color(0xFF4A5068),
    onSurfaceVariant = Color(0xFFE0E0E0)
)

@Composable
fun InglesBasicoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    @Suppress("UNUSED_PARAMETER") dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> KidDark
        else -> KidLight
    }
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
