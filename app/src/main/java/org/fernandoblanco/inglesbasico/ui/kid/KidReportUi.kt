package org.fernandoblanco.inglesbasico.ui.kid

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.fernandoblanco.inglesbasico.db.entity.NinoEntity
import org.fernandoblanco.inglesbasico.ui.design.KidBar
import org.fernandoblanco.inglesbasico.ui.design.KidBubble
import org.fernandoblanco.inglesbasico.ui.design.KidCard
import org.fernandoblanco.inglesbasico.ui.design.KidPill
import org.fernandoblanco.inglesbasico.ui.theme.Amarillo
import org.fernandoblanco.inglesbasico.ui.theme.Azul
import org.fernandoblanco.inglesbasico.ui.theme.AzulBrillante
import org.fernandoblanco.inglesbasico.ui.theme.InglesBasicoTheme
import org.fernandoblanco.inglesbasico.ui.theme.Morado
import org.fernandoblanco.inglesbasico.ui.theme.Naranja
import org.fernandoblanco.inglesbasico.ui.theme.PlayCream
import org.fernandoblanco.inglesbasico.ui.theme.PlayGold
import org.fernandoblanco.inglesbasico.ui.theme.PlaySurface
import org.fernandoblanco.inglesbasico.ui.theme.Rosa
import org.fernandoblanco.inglesbasico.ui.theme.Verde
import org.fernandoblanco.inglesbasico.ui.theme.VerdeBrillante
import java.util.Calendar

/** R17 — Barra de progreso horizontal grande, gruesa y verde brillante. */
@Composable
fun KidProgresoBarra(
    progreso: Float,
    etiqueta: String,
    modifier: Modifier = Modifier
) {
    val anim by animateFloatAsState(
        targetValue = progreso.coerceIn(0f, 1f),
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMediumLow
        ),
        label = "progresoKid"
    )
    Column(modifier, verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(
                etiqueta,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                "${(anim * 100).toInt()}%",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Black,
                color = Verde
            )
        }
        Box(
            Modifier
                .fillMaxWidth()
                .height(32.dp)
                .shadow(8.dp, KidBar, spotColor = Verde.copy(0.4f))
                .clip(KidBar)
                .background(Verde.copy(alpha = 0.2f))
        ) {
            Box(
                Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(anim)
                    .background(
                        Brush.horizontalGradient(
                            listOf(VerdeBrillante, Verde)
                        )
                    )
            )
        }
    }
}

/** R18 — Burbuja con estrella dorada gigante y puntaje total. */
@Composable
fun KidPuntajeBurbuja(puntaje: Int, modifier: Modifier = Modifier) {
    Box(
        modifier
            .shadow(20.dp, KidBubble, spotColor = PlayGold.copy(0.5f))
            .clip(KidBubble)
            .background(
                Brush.radialGradient(
                    colors = listOf(Amarillo, PlayGold, Naranja.copy(0.9f))
                )
            )
            .border(4.dp, Color.White.copy(0.7f), KidBubble)
            .padding(horizontal = 28.dp, vertical = 22.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text("⭐", style = MaterialTheme.typography.displayLarge)
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    "$puntaje",
                    style = MaterialTheme.typography.displayMedium,
                    fontWeight = FontWeight.Black,
                    color = Color.White
                )
                Text(
                    "puntos",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White.copy(0.9f)
                )
            }
        }
    }
}

/** R19 — Nivel actual con tres estrellas de logro. */
@Composable
fun KidNivelEstrellas(nivel: Int, estrellasEncendidas: Int, modifier: Modifier = Modifier) {
    Box(
        modifier
            .fillMaxWidth()
            .shadow(18.dp, KidCard, spotColor = Morado.copy(0.35f))
            .clip(KidCard)
            .background(
                Brush.horizontalGradient(listOf(Morado, AzulBrillante))
            )
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("🎮", style = MaterialTheme.typography.displaySmall)
            Text(
                "Nivel $nivel",
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Black,
                color = Color.White
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                repeat(3) { i ->
                    val encendida = i < estrellasEncendidas.coerceIn(0, 3)
                    Text(
                        if (encendida) "⭐" else "☆",
                        style = MaterialTheme.typography.headlineLarge,
                        modifier = Modifier.scale(if (encendida) 1.1f else 0.85f)
                    )
                }
            }
        }
    }
}

data class ActividadHistorialUi(
    val emoji: String,
    val titulo: String,
    val detalle: String,
    val orden: Int
)

/** R20 — Tarjeta de actividad en el historial. */
@Composable
fun KidHistorialTarjeta(item: ActividadHistorialUi, modifier: Modifier = Modifier) {
    Row(
        modifier
            .fillMaxWidth()
            .shadow(10.dp, KidCard, spotColor = Azul.copy(0.15f))
            .clip(KidCard)
            .background(PlaySurface)
            .border(2.dp, PlayCream, KidCard)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(Amarillo.copy(0.35f)),
            contentAlignment = Alignment.Center
        ) {
            Text(item.emoji, style = MaterialTheme.typography.headlineMedium)
        }
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                item.titulo,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                item.detalle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Text("✓", style = MaterialTheme.typography.headlineSmall, color = Verde)
    }
}

/** R21 — Reloj tierno + barras de tiempo (sesión y día) a partir de datos existentes. */
@Composable
fun KidTiempoUso(
    minutosTotal: Long,
    minutosDia: Long,
    minutosSesion: Long,
    modifier: Modifier = Modifier
) {
    val maxBar = maxOf(minutosTotal, minutosDia, minutosSesion, 1L).toFloat()
    Box(
        modifier
            .fillMaxWidth()
            .shadow(14.dp, KidCard, spotColor = Azul.copy(0.2f))
            .clip(KidCard)
            .background(PlayCream)
            .padding(20.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("⏰", style = MaterialTheme.typography.displaySmall)
                Column {
                    Text(
                        "Tu tiempo de juego",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    val h = minutosTotal / 60
                    val m = minutosTotal % 60
                    Text(
                        if (h > 0) "${h}h ${m}min en total" else "${m}min en total",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Azul
                    )
                }
            }
            KidTiempoBarra("Hoy", minutosDia, maxBar, AzulBrillante)
            KidTiempoBarra("Sesión", minutosSesion, maxBar, Rosa)
            KidTiempoBarra("Total", minutosTotal, maxBar, Morado)
        }
    }
}

@Composable
private fun KidTiempoBarra(etiqueta: String, minutos: Long, max: Float, color: Color) {
    val ratio = (minutos / max).coerceIn(0f, 1f)
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(
                etiqueta,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                "${minutos} min",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Black,
                color = color
            )
        }
        Box(
            Modifier
                .fillMaxWidth()
                .height(20.dp)
                .clip(KidBar)
                .background(color.copy(0.18f))
        ) {
            Box(
                Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(ratio)
                    .background(
                        Brush.horizontalGradient(listOf(color, color.copy(0.75f)))
                    )
            )
        }
    }
}

enum class KidNavDestino { PLAY, MENU, LOGROS, AJUSTES }

/** Barra inferior con botones circulares de colores. */
@Composable
fun KidBottomNavBar(
    seleccionado: KidNavDestino,
    onPlay: () -> Unit,
    onMenu: () -> Unit,
    onLogros: () -> Unit,
    onAjustes: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier
            .fillMaxWidth()
            .shadow(24.dp, KidPill, spotColor = Morado.copy(0.25f))
            .clip(KidPill)
            .background(
                Brush.horizontalGradient(
                    listOf(
                        Morado.copy(0.95f),
                        AzulBrillante.copy(0.92f)
                    )
                )
            )
            .padding(horizontal = 20.dp, vertical = 14.dp)
    ) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            KidNavBoton(
                icono = Icons.Filled.SportsEsports,
                etiqueta = "Play",
                color = Naranja,
                activo = seleccionado == KidNavDestino.PLAY,
                onClick = onPlay
            )
            KidNavBoton(
                icono = Icons.Filled.Menu,
                etiqueta = "Menú",
                color = Verde,
                activo = seleccionado == KidNavDestino.MENU,
                onClick = onMenu
            )
            KidNavBoton(
                icono = Icons.Filled.EmojiEvents,
                etiqueta = "Logros",
                color = Amarillo,
                activo = seleccionado == KidNavDestino.LOGROS,
                onClick = onLogros
            )
            KidNavBoton(
                icono = Icons.Filled.Settings,
                etiqueta = "Ajustes",
                color = Rosa,
                activo = seleccionado == KidNavDestino.AJUSTES,
                onClick = onAjustes
            )
        }
    }
}

@Composable
private fun KidNavBoton(
    icono: ImageVector,
    etiqueta: String,
    color: Color,
    activo: Boolean,
    onClick: () -> Unit
) {
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.88f else if (activo) 1.08f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "navBtn"
    )
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier
            .scale(scale)
            .clickable(interactionSource = interaction, indication = null) { onClick() }
    ) {
        Box(
            Modifier
                .size(if (activo) 58.dp else 52.dp)
                .shadow(if (activo) 12.dp else 6.dp, CircleShape, spotColor = color.copy(0.5f))
                .clip(CircleShape)
                .background(if (activo) color else color.copy(0.85f))
                .border(
                    width = if (activo) 3.dp else 0.dp,
                    color = Color.White,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(icono, contentDescription = etiqueta, tint = Color.White, modifier = Modifier.size(28.dp))
        }
        Text(
            etiqueta,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Black,
            color = Color.White.copy(if (activo) 1f else 0.85f)
        )
    }
}

/** Cabecera con mascota del niño en la pantalla de reportes. */
@Composable
fun KidReportesHeader(nombre: String, avatarEmoji: String, mascotaEmoji: String) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(bottom = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                "Reportes y Progreso",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                "¡Hola, $nombre!",
                style = MaterialTheme.typography.titleMedium,
                color = Morado
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(avatarEmoji, style = MaterialTheme.typography.displaySmall)
            Text(mascotaEmoji, style = MaterialTheme.typography.headlineLarge, modifier = Modifier.offset(y = 8.dp))
        }
    }
}

fun progresoNivel(puntajeTotal: Int): Float {
    val enNivel = puntajeTotal % 50
    return enNivel / 50f
}

fun estrellasDeNivel(puntajeTotal: Int): Int = when {
    progresoNivel(puntajeTotal) >= 0.66f -> 3
    progresoNivel(puntajeTotal) >= 0.33f -> 2
    progresoNivel(puntajeTotal) > 0f -> 1
    else -> 0
}

fun historialActividades(n: NinoEntity): List<ActividadHistorialUi> = buildList {
    if (n.sesionesVocabulario > 0) {
        add(
            ActividadHistorialUi(
                "📖",
                "Aprende palabras",
                "${n.tarjetasVocabulario} tarjetas en ${n.sesionesVocabulario} sesiones",
                n.sesionesVocabulario
            )
        )
    }
    if (n.partidasImagen > 0) {
        add(
            ActividadHistorialUi(
                "🖼️",
                "Elegir imagen correcta",
                "${n.aciertosImagen} aciertos en ${n.partidasImagen} partidas",
                n.partidasImagen
            )
        )
    }
    if (n.partidasAudio > 0) {
        add(
            ActividadHistorialUi(
                "👂",
                "Escuchar audio y responder",
                "${n.aciertosAudio} aciertos en ${n.partidasAudio} partidas",
                n.partidasAudio
            )
        )
    }
    if (n.partidasPalabras > 0) {
        add(
            ActividadHistorialUi(
                "✏️",
                "Completar palabras",
                "${n.aciertosPalabras} aciertos en ${n.partidasPalabras} partidas",
                n.partidasPalabras
            )
        )
    }
    if (n.partidasChat > 0) {
        add(
            ActividadHistorialUi(
                "🤖",
                "Conversar con la IA",
                "${n.aciertosChat} aciertos en ${n.partidasChat} turnos",
                n.partidasChat
            )
        )
    }
    if (n.rachaActual > 0) {
        add(
            ActividadHistorialUi(
                "🔥",
                "Racha diaria",
                "${n.rachaActual} días seguidos · Récord: ${n.rachaMaxima}",
                n.rachaActual
            )
        )
    }
}.sortedByDescending { it.orden }.take(8)

fun esMismoDia(timestamp: Long): Boolean {
    if (timestamp == 0L) return false
    val cal = Calendar.getInstance()
    val hoyDia = cal.get(Calendar.DAY_OF_YEAR)
    val hoyAnio = cal.get(Calendar.YEAR)
    cal.timeInMillis = timestamp
    return cal.get(Calendar.DAY_OF_YEAR) == hoyDia && cal.get(Calendar.YEAR) == hoyAnio
}

/** Tiempo real guardado en segundos, mostrado en minutos redondeados. */
fun tiemposVisualizacion(n: NinoEntity): Triple<Long, Long, Long> {
    val total = org.fernandoblanco.inglesbasico.data.segundosAMinutosDisplay(n.tiempoUsoTotalSegundos)
    val dia = org.fernandoblanco.inglesbasico.data.segundosAMinutosDisplay(
        if (n.ultimoDiaUso == org.fernandoblanco.inglesbasico.data.diaClaveActual()) n.segundosUsoHoy else 0L
    )
    val sesion = org.fernandoblanco.inglesbasico.data.segundosAMinutosDisplay(n.segundosSesionActual)
    return Triple(total, dia, sesion)
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun PreviewPantallaReportesKids() {
    InglesBasicoTheme {
        val nino = NinoEntity(
            padreId = 1,
            nombreMostrar = "Sofía",
            avatarEmoji = "🐱",
            puntajeTotal = 127,
            nivel = 3,
            partidasImagen = 12,
            aciertosImagen = 9,
            partidasAudio = 8,
            aciertosAudio = 6,
            partidasPalabras = 5,
            aciertosPalabras = 4,
            rachaActual = 3,
            rachaMaxima = 5,
            ultimaActividad = System.currentTimeMillis(),
            tiempoUsoTotalSegundos = 45 * 60,
            segundosUsoHoy = 12 * 60,
            segundosSesionActual = 8 * 60
        )
        Box(
            Modifier
                .background(
                    Brush.verticalGradient(
                        listOf(Morado.copy(0.15f), PlayCream, Azul.copy(0.1f))
                    )
                )
                .padding(16.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                KidReportesHeader("Sofía", "🐱", "🦊")
                KidProgresoBarra(progresoNivel(nino.puntajeTotal), "Tu avance")
                KidPuntajeBurbuja(nino.puntajeTotal)
                KidNivelEstrellas(nino.nivel, estrellasDeNivel(nino.puntajeTotal))
                val (total, dia, sesion) = tiemposVisualizacion(nino)
                KidTiempoUso(total, dia, sesion)
                historialActividades(nino).forEach { KidHistorialTarjeta(it) }
                KidBottomNavBar(
                    seleccionado = KidNavDestino.LOGROS,
                    onPlay = {},
                    onMenu = {},
                    onLogros = {},
                    onAjustes = {}
                )
            }
        }
    }
}
