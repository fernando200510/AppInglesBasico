package org.fernandoblanco.inglesbasico.ui.reports

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.fernandoblanco.inglesbasico.data.ReporteDiaBarra
import org.fernandoblanco.inglesbasico.ui.theme.AmarilloSuave
import org.fernandoblanco.inglesbasico.ui.theme.AzulBrillante
import org.fernandoblanco.inglesbasico.ui.theme.MoradoSuave
import org.fernandoblanco.inglesbasico.ui.theme.Rosa
import org.fernandoblanco.inglesbasico.ui.theme.VerdeSuave

private val COLORES_PASTEL = listOf(
    MoradoSuave,
    AzulBrillante.copy(alpha = 0.55f),
    VerdeSuave,
    AmarilloSuave,
    Rosa.copy(alpha = 0.6f),
    AzulBrillante.copy(alpha = 0.35f),
    MoradoSuave.copy(alpha = 0.8f)
)

/** R21 — Gráfico de barras verticales estilo Apple Screen Time. */
@Composable
fun ScreenTimeBarChart(
    barras: List<ReporteDiaBarra>,
    modifier: Modifier = Modifier
) {
    if (barras.isEmpty()) return
    val max = maxOf(barras.maxOf { it.minutos }.toFloat(), 1f)
    Column(modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
            Modifier
                .fillMaxWidth()
                .height(160.dp)
                .padding(horizontal = 4.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.Bottom
        ) {
            barras.forEachIndexed { index, barra ->
                val color = if (barra.esHoy) {
                    MaterialTheme.colorScheme.primary
                } else {
                    COLORES_PASTEL[index % COLORES_PASTEL.size]
                }
                val ratio = (barra.minutos.toFloat() / max).coerceIn(0f, 1f)
                val anim by animateFloatAsState(
                    targetValue = ratio,
                    animationSpec = tween(700),
                    label = "barraTiempo"
                )
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    if (barra.minutos > 0) {
                        Text(
                            text = "${barra.minutos}",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = if (barra.esHoy) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.onSurfaceVariant
                            }
                        )
                    }
                    Canvas(Modifier.size(width = 28.dp, height = 120.dp)) {
                        val barH = size.height * anim.coerceIn(
                            if (barra.minutos > 0) 0.06f else 0f,
                            1f
                        )
                        val radio = CornerRadius(8.dp.toPx(), 8.dp.toPx())
                        drawRoundRect(
                            color = color.copy(alpha = 0.25f),
                            topLeft = Offset.Zero,
                            size = Size(size.width, size.height),
                            cornerRadius = radio
                        )
                        if (barH > 0f) {
                            drawRoundRect(
                                color = color,
                                topLeft = Offset(0f, size.height - barH),
                                size = Size(size.width, barH),
                                cornerRadius = radio
                            )
                        }
                    }
                    Text(
                        text = barra.etiqueta,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = if (barra.esHoy) FontWeight.Bold else FontWeight.Normal,
                        color = if (barra.esHoy) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        }
                    )
                }
            }
        }
    }
}
