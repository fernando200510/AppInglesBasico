package org.fernandoblanco.inglesbasico.ui.kid

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.fernandoblanco.inglesbasico.ui.design.PlayScreenGradient
import org.fernandoblanco.inglesbasico.ui.design.PlaySolidButton
import org.fernandoblanco.inglesbasico.ui.theme.Amarillo
import org.fernandoblanco.inglesbasico.ui.theme.Azul
import org.fernandoblanco.inglesbasico.ui.theme.Morado
import org.fernandoblanco.inglesbasico.ui.theme.Naranja
import org.fernandoblanco.inglesbasico.ui.theme.Rojo
import org.fernandoblanco.inglesbasico.ui.theme.Verde
import org.fernandoblanco.inglesbasico.ui.theme.VerdeSuave

@Composable
fun KidGameBackground(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    PlayScreenGradient(modifier) { content() }
}

@Composable
fun KidSessionProgress(actual: Int, total: Int) {
    val target = if (total == 0) 0f else actual.toFloat() / total
    val animated by animateFloatAsState(
        targetValue = target,
        animationSpec = tween(420, easing = FastOutSlowInEasing),
        label = "prog"
    )
    Column(Modifier.fillMaxWidth()) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Pregunta $actual / $total",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                "${(animated * 100).toInt()} %",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold,
                color = Naranja
            )
        }
        Spacer(Modifier.height(10.dp))
        LinearProgressIndicator(
            progress = { animated },
            modifier = Modifier
                .fillMaxWidth()
                .height(16.dp)
                .clip(RoundedCornerShape(12.dp))
                .shadow(4.dp, RoundedCornerShape(12.dp)),
            color = Naranja,
            trackColor = VerdeSuave,
        )
    }
}

@Composable
fun GameFeedbackBlock(
    texto: String?,
    esCorrecto: Boolean?,
    solucionCorrecta: String?,
    modifier: Modifier = Modifier
) {
    val visible = texto != null && esCorrecto != null
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(tween(220)) + expandVertically(tween(280)),
        exit = fadeOut(tween(160)) + shrinkVertically(tween(200))
    ) {
        val t = texto ?: return@AnimatedVisibility
        val ok = esCorrecto ?: return@AnimatedVisibility
        val borde = if (ok) Verde else Rojo
        val fondo = if (ok) Verde.copy(alpha = 0.14f) else Rojo.copy(alpha = 0.1f)
        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .border(3.dp, borde, RoundedCornerShape(24.dp)),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = fondo),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    t,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.fillMaxWidth()
                )
                if (!ok && solucionCorrecta != null) {
                    Text(
                        "La correcta es: $solucionCorrecta",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = Verde,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
fun KidFinSesion(
    aciertos: Int,
    total: Int,
    onJugarOtra: () -> Unit,
    onSalir: () -> Unit
) {
    val pct = if (total == 0) 0 else (aciertos * 100) / total
    val titulo = mensajeFinalSesion(pct)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .shadow(16.dp, RoundedCornerShape(36.dp), spotColor = Morado.copy(0.2f)),
        shape = RoundedCornerShape(36.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            Modifier.padding(28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("🏆", style = MaterialTheme.typography.displayMedium)
            Text(
                titulo,
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center,
                color = Morado,
                fontWeight = FontWeight.Bold
            )
            Text(
                "Aciertos: $aciertos de $total",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                "$pct %",
                style = MaterialTheme.typography.displaySmall,
                color = Naranja,
                fontWeight = FontWeight.Black
            )
            Spacer(Modifier.height(4.dp))
            PlaySolidButton(
                text = "Jugar otra vez",
                onClick = onJugarOtra,
                containerColor = Verde,
                heightDp = 56
            )
            PlaySolidButton(
                text = "Volver al menú",
                onClick = onSalir,
                containerColor = Amarillo,
                contentColor = Color(0xFF1A1A2E),
                heightDp = 52
            )
        }
    }
}

fun mensajeFinalSesion(porcentaje: Int): String = when {
    porcentaje >= 80 -> "¡Excelente!"
    porcentaje >= 50 -> "¡Vas bien!"
    else -> "Sigue practicando"
}

@Composable
fun KidOptionButton(
    label: String,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (pressed && enabled) 0.93f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessMedium),
        label = "opt"
    )
    val shape = RoundedCornerShape(24.dp)
    val base = Morado
    Box(
        modifier = modifier
            .height(72.dp)
            .fillMaxWidth()
            .scale(scale)
            .shadow(10.dp, shape, ambientColor = base.copy(0.22f), spotColor = base.copy(0.32f))
            .clip(shape)
            .background(if (enabled) base else base.copy(alpha = 0.38f))
            .clickable(interactionSource = interaction, indication = null, enabled = enabled) { onClick() }
            .padding(horizontal = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            label,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = Color.White,
            maxLines = 2,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun KidListenButton(enabled: Boolean, onClick: () -> Unit) {
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val tapScale by animateFloatAsState(
        targetValue = if (pressed) 0.95f else 1f,
        animationSpec = spring(dampingRatio = 0.5f, stiffness = 400f),
        label = "listenTap"
    )
    val infinite = rememberInfiniteTransition(label = "pulseL")
    val pulse by infinite.animateFloat(
        initialValue = 1f,
        targetValue = if (enabled) 1.05f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(700, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseListen"
    )
    val shape = RoundedCornerShape(28.dp)
    val col = Azul
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(84.dp)
            .scale(tapScale * pulse)
            .shadow(14.dp, shape, ambientColor = col.copy(0.25f), spotColor = col.copy(0.35f))
            .clip(shape)
            .background(if (enabled) col else col.copy(alpha = 0.45f))
            .clickable(interactionSource = interaction, indication = null, enabled = enabled) { onClick() }
            .padding(horizontal = 20.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text("🔊", style = MaterialTheme.typography.headlineMedium)
            Text(
                if (enabled) "Escuchar" else "Preparando audio…",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

@Composable
fun GamePlayContentCard(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(12.dp, RoundedCornerShape(28.dp), spotColor = Azul.copy(0.12f)),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            Modifier
                .border(1.dp, Azul.copy(alpha = 0.12f), RoundedCornerShape(28.dp))
                .padding(22.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            content()
        }
    }
}