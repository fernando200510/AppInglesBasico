package org.fernandoblanco.inglesbasico.ui.design

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import org.fernandoblanco.inglesbasico.ui.theme.PlayBlue
import org.fernandoblanco.inglesbasico.ui.theme.PlayCream
import org.fernandoblanco.inglesbasico.ui.theme.PlayInk
import org.fernandoblanco.inglesbasico.ui.theme.PlayPurple
import org.fernandoblanco.inglesbasico.ui.theme.PlaySurface
import org.fernandoblanco.inglesbasico.ui.theme.PlayYellow

@Composable
fun PlayScreenGradient(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        PlayBlue.copy(alpha = 0.18f),
                        PlayCream,
                        PlayYellow.copy(alpha = 0.12f),
                        PlayCream
                    )
                )
            )
    ) { content() }
}

/**
 * Botón 100 % sólido: sin capas blancas internas del Material Button.
 */
@Composable
fun PlaySolidButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    loading: Boolean = false,
    containerColor: Color = PlayBlue,
    contentColor: Color = Color.White,
    heightDp: Int = 58
) {
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val bounce by animateFloatAsState(
        targetValue = if (pressed && enabled && !loading) 0.94f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "btnBounce"
    )
    val shape = RoundedCornerShape(22.dp)
    Box(
        modifier = modifier
            .height(heightDp.dp)
            .fillMaxWidth()
            .scale(bounce)
            .shadow(
                elevation = 12.dp,
                shape = shape,
                ambientColor = containerColor.copy(alpha = 0.28f),
                spotColor = containerColor.copy(alpha = 0.38f)
            )
            .clip(shape)
            .background(
                if (enabled && !loading) containerColor else containerColor.copy(alpha = 0.42f)
            )
            .clickable(
                interactionSource = interaction,
                indication = null,
                enabled = enabled && !loading
            ) { onClick() }
            .padding(horizontal = 20.dp),
        contentAlignment = Alignment.Center
    ) {
        if (loading) {
            CircularProgressIndicator(
                modifier = Modifier.size(30.dp),
                color = contentColor,
                strokeWidth = 2.5.dp
            )
        } else {
            Text(
                text,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = contentColor,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun PlayOutlineButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    borderColor: Color = PlayPurple,
    textColor: Color = PlayInk
) {
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.97f else 1f,
        animationSpec = spring(dampingRatio = 0.55f, stiffness = 400f),
        label = "outline"
    )
    val shape = RoundedCornerShape(22.dp)
    Box(
        modifier = modifier
            .height(52.dp)
            .fillMaxWidth()
            .scale(scale)
            .shadow(6.dp, shape, ambientColor = borderColor.copy(0.15f), spotColor = borderColor.copy(0.2f))
            .border(2.dp, borderColor.copy(alpha = 0.9f), shape)
            .clip(shape)
            .background(PlaySurface)
            .clickable(interactionSource = interaction, indication = null) { onClick() }
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold,
            color = textColor
        )
    }
}

@Composable
fun playTextFieldColors(): TextFieldColors = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = PlayPurple,
    unfocusedBorderColor = PlayBlue.copy(alpha = 0.45f),
    focusedLabelColor = PlayPurple,
    unfocusedLabelColor = PlayInk.copy(alpha = 0.55f),
    cursorColor = PlayBlue,
    focusedContainerColor = PlaySurface,
    unfocusedContainerColor = PlaySurface,
    focusedTextColor = PlayInk,
    unfocusedTextColor = PlayInk
)

@Composable
fun LoginEntrance(content: @Composable () -> Unit) {
    var show by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        delay(40)
        show = true
    }
    AnimatedVisibility(
        visible = show,
        enter = fadeIn(tween(420)) +
            slideInVertically(
                animationSpec = tween(420, easing = FastOutSlowInEasing)
            ) { it / 5 }
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            content()
        }
    }
}
