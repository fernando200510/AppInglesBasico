package org.fernandoblanco.inglesbasico.ui.theme

import android.os.Build
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val EsquemaClaro = lightColorScheme(
    primary = Naranja,
    onPrimary = SuperficieClaro,
    primaryContainer = Color(0xFFFFEDE6),
    onPrimaryContainer = NaranjaOscuro,
    secondary = Morado,
    onSecondary = SuperficieClaro,
    secondaryContainer = MoradoSuave,
    onSecondaryContainer = Morado,
    tertiary = Verde,
    onTertiary = SuperficieClaro,
    tertiaryContainer = VerdeSuave,
    onTertiaryContainer = VerdeOscuro,
    background = FondoClaro,
    onBackground = TextoPrincipalClaro,
    surface = SuperficieClaro,
    onSurface = TextoPrincipalClaro,
    surfaceVariant = Color(0xFFF5F0FF),
    onSurfaceVariant = TextoSecundarioClaro,
    error = Rojo,
    onError = SuperficieClaro,
)

private val EsquemaOscuro = darkColorScheme(
    primary = Naranja,
    onPrimary = Color(0xFF1A0A00),
    primaryContainer = NaranjaOscuro,
    onPrimaryContainer = Color(0xFFFFDDD0),
    secondary = Rosa,
    onSecondary = Color(0xFF1A0010),
    secondaryContainer = Color(0xFF5C1A45),
    onSecondaryContainer = Color(0xFFFFD0E8),
    tertiary = Verde,
    onTertiary = Color(0xFF001A14),
    tertiaryContainer = VerdeOscuro,
    onTertiaryContainer = Color(0xFFB0FFE8),
    background = FondoOscuro,
    onBackground = TextoPrincipalOscuro,
    surface = SuperficieOscuro,
    onSurface = TextoPrincipalOscuro,
    surfaceVariant = TarjetaOscuro,
    onSurfaceVariant = TextoSecundarioOscuro,
    error = Color(0xFFFF6B6B),
    onError = Color(0xFF1A0000),
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