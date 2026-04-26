package org.fernandoblanco.inglesbasico.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val EsquemaClaro = lightColorScheme(
    primary = PlayOrange,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFFECB3),
    onPrimaryContainer = PlayOrangeDark,
    secondary = PlayPurple,
    onSecondary = Color.White,
    secondaryContainer = PlayPurpleLight,
    onSecondaryContainer = PlayPurple,
    tertiary = PlayBlue,
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFE3F2FD),
    onTertiaryContainer = Color(0xFF1565C0),
    background = FondoClaro,
    onBackground = TextoPrincipalClaro,
    surface = SuperficieClaro,
    onSurface = TextoPrincipalClaro,
    surfaceVariant = Color(0xFFE1E2EC),
    onSurfaceVariant = Color(0xFF44474E),
    error = Color(0xFFBA1A1A),
    onError = Color.White
)

private val EsquemaOscuro = darkColorScheme(
    primary = PlayOrange,
    onPrimary = Color(0xFF452100),
    primaryContainer = Color(0xFF914D00),
    onPrimaryContainer = Color(0xFFFFDDB3),
    secondary = Color(0xFFCE93D8), // Un lila más suave para oscuridad
    onSecondary = Color(0xFF3B004B),
    secondaryContainer = Color(0xFF53006A),
    onSecondaryContainer = Color(0xFFF3E5F5),
    tertiary = Color(0xFF90CAF9), // Un azul cielo suave
    onTertiary = Color(0xFF003258),
    tertiaryContainer = Color(0xFF00497D),
    onTertiaryContainer = Color(0xFFD1E4FF),
    background = FondoOscuro,
    onBackground = TextoPrincipalOscuro,
    surface = SuperficieOscuro,
    onSurface = TextoPrincipalOscuro,
    surfaceVariant = TarjetaOscuro,
    onSurfaceVariant = Color(0xFFC4C6D0),
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005)
)

@Composable
fun InglesBasicoTheme(
    oscuro: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val esquema = if (oscuro) EsquemaOscuro else EsquemaClaro

    MaterialTheme(
        colorScheme = esquema,
        typography = Typography,
        content = content
    )
}