package org.fernandoblanco.inglesbasico.ui.parent

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import org.fernandoblanco.inglesbasico.data.ParentActividadCheckUi
import org.fernandoblanco.inglesbasico.data.ParentHistorialEstado
import org.fernandoblanco.inglesbasico.data.ParentHistorialItem
import org.fernandoblanco.inglesbasico.data.ParentProgresoUi
import org.fernandoblanco.inglesbasico.data.ParentReportDashboard
import org.fernandoblanco.inglesbasico.data.ParentTiempoUi
import org.fernandoblanco.inglesbasico.data.TipoActividadReporte
import org.fernandoblanco.inglesbasico.data.formatearDuracionTotal
import org.fernandoblanco.inglesbasico.data.formatearFechaHistorial
import org.fernandoblanco.inglesbasico.db.entity.NinoEntity
import org.fernandoblanco.inglesbasico.ui.design.KidBar
import org.fernandoblanco.inglesbasico.ui.design.KidCard
import org.fernandoblanco.inglesbasico.ui.theme.Amarillo
import org.fernandoblanco.inglesbasico.ui.theme.Azul
import org.fernandoblanco.inglesbasico.ui.theme.AzulBrillante
import org.fernandoblanco.inglesbasico.ui.theme.Morado
import org.fernandoblanco.inglesbasico.ui.theme.MoradoSuave
import org.fernandoblanco.inglesbasico.ui.theme.Naranja
import org.fernandoblanco.inglesbasico.ui.theme.NaranjaOscuro
import org.fernandoblanco.inglesbasico.ui.theme.PlayCream
import org.fernandoblanco.inglesbasico.ui.theme.PlayGold
import org.fernandoblanco.inglesbasico.ui.theme.PlaySurface
import org.fernandoblanco.inglesbasico.ui.theme.Rojo
import org.fernandoblanco.inglesbasico.ui.theme.Verde
import org.fernandoblanco.inglesbasico.ui.theme.VerdeBrillante

/** R7 — Selector horizontal de perfiles con avatar y nombre. */
@Composable
fun ParentChildSelector(
    ninos: List<NinoEntity>,
    seleccionadoId: Long?,
    onSeleccionar: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        ninos.forEach { nino ->
            val activo = nino.id == seleccionadoId
            ParentChildChip(
                emoji = nino.avatarEmoji,
                nombre = nino.nombreMostrar,
                activo = activo,
                onClick = { onSeleccionar(nino.id) }
            )
        }
    }
}

@Composable
private fun ParentChildChip(
    emoji: String,
    nombre: String,
    activo: Boolean,
    onClick: () -> Unit
) {
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.96f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "chipScale"
    )
    Column(
        modifier = Modifier
            .scale(scale)
            .width(IntrinsicSize.Min)
            .clip(KidCard)
            .background(if (activo) MoradoSuave else PlaySurface)
            .border(
                width = if (activo) 3.dp else 1.dp,
                color = if (activo) Morado else PlayCream,
                shape = KidCard
            )
            .clickable(interactionSource = interaction, indication = null, onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Box(
            Modifier
                .size(44.dp)
                .shadow(if (activo) 8.dp else 2.dp, CircleShape, spotColor = Morado.copy(0.3f))
                .clip(CircleShape)
                .background(if (activo) Morado.copy(0.15f) else PlayCream),
            contentAlignment = Alignment.Center
        ) {
            Text(emoji, style = MaterialTheme.typography.headlineSmall)
        }
        Text(
            nombre,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = if (activo) FontWeight.Bold else FontWeight.Medium,
            color = if (activo) Morado else MaterialTheme.colorScheme.onSurface,
            maxLines = 1
        )
    }
}

@Composable
fun ParentResumenNino(
    nombre: String,
    avatarEmoji: String,
    ultimaActividadTexto: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(avatarEmoji, style = MaterialTheme.typography.headlineMedium)
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                "Reportes de $nombre",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                "Última actividad: $ultimaActividadTexto",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

/** R19 + R17 — Nivel, estrellas y barra de actividades habilitadas completadas. */
@Composable
fun ParentLevelProgressCard(
    progreso: ParentProgresoUi,
    modifier: Modifier = Modifier
) {
    val animPct by animateFloatAsState(
        targetValue = progreso.porcentaje / 100f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "pctProgreso"
    )
    Box(
        modifier
            .fillMaxWidth()
            .shadow(12.dp, KidCard, spotColor = Morado.copy(0.2f))
            .clip(KidCard)
            .background(PlaySurface)
            .padding(20.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    Modifier
                        .clip(KidBar)
                        .background(Morado.copy(0.12f))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        "Nivel ${progreso.nivel}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Morado
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    repeat(3) { i ->
                        Text(
                            if (i < progreso.estrellas) "★" else "☆",
                            style = MaterialTheme.typography.titleLarge,
                            color = Amarillo
                        )
                    }
                }
            }
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(
                    "Actividades completadas",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    "${progreso.completadas} de ${progreso.totalHabilitadas} · ${progreso.porcentaje}%",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Verde
                )
            }
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(24.dp)
                    .clip(KidBar)
                    .background(Verde.copy(alpha = 0.15f))
            ) {
                Box(
                    Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(animPct.coerceIn(0f, 1f))
                        .background(Brush.horizontalGradient(listOf(VerdeBrillante, Verde)))
                )
            }
            ParentActividadChecklist(progreso.checklist)
        }
    }
}

@Composable
private fun ParentActividadChecklist(checklist: List<ParentActividadCheckUi>) {
    Row(
        Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        checklist.forEach { item ->
            val color = colorDeTipo(item.tipo)
            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    item.tipo.emoji,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.alpha(if (item.habilitada) 1f else 0.35f)
                )
                Text(
                    when {
                        !item.habilitada -> "—"
                        item.conProgreso -> "✓"
                        else -> "○"
                    },
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = when {
                        !item.habilitada -> MaterialTheme.colorScheme.onSurfaceVariant
                        item.conProgreso -> Verde
                        else -> color.copy(0.6f)
                    },
                    textDecoration = if (!item.habilitada) TextDecoration.LineThrough else null
                )
            }
        }
    }
}

/** R18 — Puntaje total acumulado. */
@Composable
fun ParentScoreHero(puntaje: Int, modifier: Modifier = Modifier) {
    Box(
        modifier
            .fillMaxWidth()
            .shadow(16.dp, KidCard, spotColor = PlayGold.copy(0.45f))
            .clip(KidCard)
            .background(Brush.horizontalGradient(listOf(Amarillo, PlayGold, Naranja.copy(0.85f))))
            .padding(vertical = 28.dp, horizontal = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text("⭐", style = MaterialTheme.typography.displaySmall)
            Text(
                "$puntaje",
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Black,
                color = Color.White
            )
            Text(
                "puntos acumulados",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = Color.White.copy(0.92f)
            )
            Text(
                "Por aciertos en las 5 actividades",
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(0.8f),
                textAlign = TextAlign.Center
            )
        }
    }
}

/** R21 + R15 — Contenido de tiempo (sin tarjeta externa, para usar dentro de ParentChartCard). */
@Composable
fun ParentTimeContent(tiempo: ParentTiempoUi, modifier: Modifier = Modifier) {
    val barColor = when {
        tiempo.porcentajeLimite >= 1f -> Rojo
        tiempo.porcentajeLimite >= 0.9f -> Naranja
        tiempo.porcentajeLimite >= 0.7f -> NaranjaOscuro
        else -> AzulBrillante
    }
    val borderColor = when {
        tiempo.porcentajeLimite >= 1f -> Rojo
        tiempo.porcentajeLimite >= 0.9f -> Naranja
        else -> Color.Transparent
    }
    val animRatio by animateFloatAsState(
        targetValue = tiempo.porcentajeLimite.coerceIn(0f, 1f),
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "tiempoRatio"
    )
    Column(modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("⏱", style = MaterialTheme.typography.headlineMedium)
                Text(
                    "Tiempo de uso hoy",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }
            Text(
                "${tiempo.minutosHoy} min usados de ${tiempo.limiteDiario} min permitidos",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = barColor
            )
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(22.dp)
                    .clip(KidBar)
                    .background(barColor.copy(0.15f))
            ) {
                Box(
                    Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(animRatio)
                        .background(Brush.horizontalGradient(listOf(barColor, barColor.copy(0.75f))))
                )
            }
            if (tiempo.porcentajeLimite >= 1f) {
                Text(
                    "Límite diario alcanzado",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = Rojo
                )
            }
            Text(
                "Sesión actual: ${tiempo.minutosSesion} min · Total: ${formatearDuracionTotal(tiempo.minutosTotal)}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
    }
}

/** R21 + R15 — Tarjeta independiente de tiempo de uso. */
@Composable
fun ParentDailyTimeCard(tiempo: ParentTiempoUi, modifier: Modifier = Modifier) {
    Box(
        modifier
            .fillMaxWidth()
            .shadow(10.dp, KidCard, spotColor = Azul.copy(0.15f))
            .clip(KidCard)
            .background(PlaySurface)
            .padding(20.dp)
    ) {
        ParentTimeContent(tiempo)
    }
}

/** R20 — Línea de tiempo de actividades. */
@Composable
fun ParentActivityTimeline(
    items: List<ParentHistorialItem>,
    modifier: Modifier = Modifier
) {
    Column(modifier, verticalArrangement = Arrangement.spacedBy(0.dp)) {
        Text(
            "Historial de actividades",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        if (items.isEmpty()) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .clip(KidCard)
                    .background(PlayCream)
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "Aún no hay actividades registradas para este perfil.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            items.forEachIndexed { index, item ->
                ParentTimelineRow(
                    item = item,
                    esUltimo = index == items.lastIndex
                )
            }
        }
    }
}

@Composable
private fun ParentTimelineRow(item: ParentHistorialItem, esUltimo: Boolean) {
    val color = colorDeTipo(item.tipo)
    Row(
        Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .padding(bottom = if (esUltimo) 0.dp else 4.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(28.dp)
        ) {
            Box(
                Modifier
                    .size(12.dp)
                    .clip(CircleShape)
                    .background(color)
            )
            if (!esUltimo) {
                Box(
                    Modifier
                        .width(2.dp)
                        .height(72.dp)
                        .background(Morado.copy(0.25f))
                )
            }
        }
        Spacer(Modifier.width(12.dp))
        Box(
            Modifier
                .weight(1f)
                .padding(bottom = 16.dp)
                .shadow(6.dp, KidCard, spotColor = color.copy(0.12f))
                .clip(KidCard)
                .background(PlaySurface)
                .border(1.dp, PlayCream, KidCard)
                .padding(14.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(item.tipo.emoji, style = MaterialTheme.typography.titleLarge)
                    Text(
                        item.tipo.tituloParental,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )
                    ParentEstadoPill(item.estado)
                }
                Text(
                    formatearFechaHistorial(item.fechaHoraMillis),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    item.detalle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Composable
private fun ParentEstadoPill(estado: ParentHistorialEstado) {
    val (texto, fondo, textoColor) = when (estado) {
        ParentHistorialEstado.EXITO -> Triple("Éxito", Verde.copy(0.15f), Verde)
        ParentHistorialEstado.PARCIAL -> Triple("Parcial", Naranja.copy(0.15f), NaranjaOscuro)
        ParentHistorialEstado.INTENTO -> Triple("Práctica", PlayCream, MaterialTheme.colorScheme.onSurfaceVariant)
    }
    Box(
        Modifier
            .clip(KidBar)
            .background(fondo)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(texto, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = textoColor)
    }
}

@Composable
fun ParentReportesDashboard(
    dashboard: ParentReportDashboard,
    modifier: Modifier = Modifier
) {
    Column(modifier, verticalArrangement = Arrangement.spacedBy(16.dp)) {
        ParentChartCard("R17 · R19 — Progreso y nivel") {
            ParentProgressRingChart(dashboard.progreso)
        }
        ParentScoreHero(dashboard.puntaje)
        ParentLevelProgressCard(dashboard.progreso)
        if (dashboard.barrasAcierto.isNotEmpty()) {
            ParentChartCard("Rendimiento por actividad") {
                ParentActivityBarChart(dashboard.barrasAcierto)
            }
        }
        if (dashboard.distribucion.isNotEmpty()) {
            ParentChartCard("Distribución de sesiones") {
                ParentActivityDonutChart(dashboard.distribucion)
            }
        }
        ParentChartCard("R21 — Tiempo de uso") {
            ParentTimeContent(dashboard.tiempo)
            Spacer(Modifier.height(12.dp))
            ParentTimeBarChart(dashboard.tiempo)
        }
        ParentActivityTimeline(dashboard.historial)
    }
}

@Composable
private fun ParentChartCard(titulo: String, content: @Composable () -> Unit) {
    Box(
        Modifier
            .fillMaxWidth()
            .shadow(10.dp, KidCard, spotColor = Morado.copy(0.12f))
            .clip(KidCard)
            .background(PlaySurface)
            .padding(18.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(titulo, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            content()
        }
    }
}
