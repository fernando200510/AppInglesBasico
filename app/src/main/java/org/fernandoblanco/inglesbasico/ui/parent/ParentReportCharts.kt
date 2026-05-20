package org.fernandoblanco.inglesbasico.ui.parent

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.fernandoblanco.inglesbasico.data.ParentChartBar
import org.fernandoblanco.inglesbasico.data.ParentChartSlice
import org.fernandoblanco.inglesbasico.data.ParentProgresoUi
import org.fernandoblanco.inglesbasico.data.ParentTiempoUi
import org.fernandoblanco.inglesbasico.data.TipoActividadReporte
import org.fernandoblanco.inglesbasico.ui.theme.Amarillo
import org.fernandoblanco.inglesbasico.ui.theme.Azul
import org.fernandoblanco.inglesbasico.ui.theme.AzulBrillante
import org.fernandoblanco.inglesbasico.ui.theme.Morado
import org.fernandoblanco.inglesbasico.ui.theme.Naranja
import org.fernandoblanco.inglesbasico.ui.theme.Verde

/** R17 + R19 — Anillo de progreso del nivel y actividades. */
@Composable
fun ParentProgressRingChart(progreso: ParentProgresoUi, modifier: Modifier = Modifier) {
    val animActividades by animateFloatAsState(
        targetValue = progreso.porcentaje / 100f,
        animationSpec = tween(800),
        label = "ringActividades"
    )
    val animNivel by animateFloatAsState(
        targetValue = progreso.progresoNivelPct / 100f,
        animationSpec = tween(800),
        label = "ringNivel"
    )
    Row(
        modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ParentRing(
            progreso = animActividades,
            color = Verde,
            titulo = "Actividades",
            subtitulo = "${progreso.completadas}/${progreso.totalHabilitadas}",
            centro = "${progreso.porcentaje}%"
        )
        ParentRing(
            progreso = animNivel,
            color = Morado,
            titulo = "Nivel ${progreso.nivel}",
            subtitulo = "★".repeat(progreso.estrellas) + "☆".repeat(3 - progreso.estrellas),
            centro = "${progreso.progresoNivelPct}%"
        )
    }
}

@Composable
private fun ParentRing(
    progreso: Float,
    color: Color,
    titulo: String,
    subtitulo: String,
    centro: String
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Box(contentAlignment = Alignment.Center) {
            Canvas(Modifier.size(100.dp)) {
                val stroke = 12.dp.toPx()
                val radio = size.minDimension / 2f - stroke
                val centroCanvas = Offset(size.width / 2f, size.height / 2f)
                drawCircle(
                    color = color.copy(0.15f),
                    radius = radio,
                    center = centroCanvas,
                    style = Stroke(width = stroke)
                )
                val barrido = 360f * progreso.coerceIn(0f, 1f)
                drawArc(
                    color = color,
                    startAngle = -90f,
                    sweepAngle = barrido,
                    useCenter = false,
                    topLeft = Offset(centroCanvas.x - radio, centroCanvas.y - radio),
                    size = Size(radio * 2, radio * 2),
                    style = Stroke(width = stroke, cap = StrokeCap.Round)
                )
            }
            Text(centro, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black, color = color)
        }
        Text(titulo, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
        Text(subtitulo, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

/** Gráfico de barras — % acierto por cada actividad. */
@Composable
fun ParentActivityBarChart(barras: List<ParentChartBar>, modifier: Modifier = Modifier) {
    if (barras.isEmpty()) return
    Column(modifier, verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(
            "Rendimiento por actividad",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        barras.forEach { barra ->
            val anim by animateFloatAsState(barra.valor / barra.maximo.coerceAtLeast(1f), tween(700), label = "bar")
            val color = colorDeTipo(barra.tipo)
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(barra.tipo.emoji, style = MaterialTheme.typography.titleMedium)
                Column(Modifier.weight(1f)) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(
                            barra.tipo.tituloParental,
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.weight(1f)
                        )
                        Text(barra.etiqueta, style = MaterialTheme.typography.labelMedium, color = color)
                    }
                    Canvas(Modifier.fillMaxWidth().height(14.dp).padding(top = 4.dp)) {
                        drawRoundRect(
                            color = color.copy(0.2f),
                            size = Size(size.width, size.height)
                        )
                        drawRoundRect(
                            color = color,
                            size = Size(size.width * anim.coerceIn(0f, 1f), size.height)
                        )
                    }
                }
            }
        }
    }
}

/** Gráfico de dona — distribución de sesiones por actividad. */
@Composable
fun ParentActivityDonutChart(slices: List<ParentChartSlice>, modifier: Modifier = Modifier) {
    if (slices.isEmpty()) return
    val total = slices.sumOf { it.valor.toDouble() }.toFloat().coerceAtLeast(1f)
    Column(modifier, horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            "Sesiones por actividad",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth()
        )
        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Canvas(Modifier.size(120.dp)) {
                var start = -90f
                slices.forEach { slice ->
                    val sweep = 360f * (slice.valor / total)
                    drawArc(
                        color = colorDeTipo(slice.tipo),
                        startAngle = start,
                        sweepAngle = sweep,
                        useCenter = true,
                        size = Size(size.width, size.height)
                    )
                    start += sweep
                }
                drawCircle(
                    color = Color.White,
                    radius = size.minDimension * 0.28f,
                    center = Offset(size.width / 2f, size.height / 2f)
                )
            }
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                slices.forEach { slice ->
                    val pct = ((slice.valor / total) * 100).toInt()
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Canvas(Modifier.size(10.dp)) {
                            drawCircle(colorDeTipo(slice.tipo), radius = size.minDimension / 2f)
                        }
                        Text(
                            "${slice.tipo.emoji} ${pct}%",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }
}

/** R21 — Barras de minutos por día (últimos 7 días) + sesión actual. */
@Composable
fun ParentTimeBarChart(tiempo: ParentTiempoUi, modifier: Modifier = Modifier) {
    val dias = tiempo.minutosPorDia
    val items = if (dias.isNotEmpty()) {
        dias.mapIndexed { index, dia ->
            val color = if (index == dias.lastIndex) AzulBrillante else Azul
            Triple(dia.etiqueta, dia.minutos.toFloat(), color)
        }
    } else {
        listOf(
            Triple("Sesión", tiempo.minutosSesion.toFloat(), AzulBrillante),
            Triple("Hoy", tiempo.minutosHoy.toFloat(), Azul)
        )
    }
    val max = maxOf(items.maxOf { it.second }, tiempo.limiteDiario.toFloat(), 1f)
    Column(modifier, verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            if (dias.isNotEmpty()) "Minutos por día (última semana)" else "Tiempo de uso (min)",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Row(
            Modifier.fillMaxWidth().height(140.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.Bottom
        ) {
            items.forEach { (label, valor, color) ->
                val anim by animateFloatAsState((valor / max).coerceIn(0f, 1f), tween(700), label = "timeBar")
                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        if (valor < 1f && valor > 0f) "<1" else "${valor.toInt()}",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = color
                    )
                    Canvas(Modifier.size(width = if (dias.size > 4) 36.dp else 48.dp, height = 100.dp)) {
                        val barH = size.height * anim.coerceIn(if (valor > 0f) 0.08f else 0f, 1f)
                        drawRoundRect(
                            color = color.copy(0.2f),
                            topLeft = Offset(0f, 0f),
                            size = Size(size.width, size.height)
                        )
                        if (barH > 0f) {
                            drawRoundRect(
                                color = color,
                                topLeft = Offset(0f, size.height - barH),
                                size = Size(size.width, barH)
                            )
                        }
                    }
                    Text(label, style = MaterialTheme.typography.labelSmall, maxLines = 1)
                }
            }
        }
        if (tiempo.minutosSesion > 0) {
            Text(
                "Sesión actual: ${tiempo.minutosSesion} min",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

fun colorDeTipo(tipo: TipoActividadReporte): Color = when (tipo) {
    TipoActividadReporte.VOCABULARIO -> Amarillo
    TipoActividadReporte.IMAGEN -> Naranja
    TipoActividadReporte.AUDIO -> Morado
    TipoActividadReporte.PALABRAS -> Verde
    TipoActividadReporte.CHAT -> Azul
}
