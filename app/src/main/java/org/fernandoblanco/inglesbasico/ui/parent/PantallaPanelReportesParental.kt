package org.fernandoblanco.inglesbasico.ui.parent

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import org.fernandoblanco.inglesbasico.data.construirDashboard
import org.fernandoblanco.inglesbasico.data.formatearUltimaActividad
import org.fernandoblanco.inglesbasico.ui.InglesViewModelFactory
import org.fernandoblanco.inglesbasico.ui.design.PlayScreenGradient
import org.fernandoblanco.inglesbasico.ui.viewmodel.ReportesParentalesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaPanelReportesParental(
    factory: InglesViewModelFactory,
    nav: NavHostController
) {
    val vm: ReportesParentalesViewModel = viewModel(factory = factory)
    val ninos by vm.ninos.collectAsState()
    val seleccionadoId by vm.ninoIdSeleccionado.collectAsState()
    val nino by vm.ninoSeleccionado.collectAsState()
    val historial by vm.historial.collectAsState()
    val usoDiario by vm.usoDiario.collectAsState()
    val scroll = rememberScrollState()

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Panel parental · Reportes",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { nav.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
                )
            )
        }
    ) { pad ->
        PlayScreenGradient(Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .padding(pad)
                    .fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 8.dp)
                ) {
                    if (ninos.isNotEmpty()) {
                        ParentChildSelector(
                            ninos = ninos,
                            seleccionadoId = seleccionadoId,
                            onSeleccionar = vm::seleccionarNino
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scroll)
                        .padding(horizontal = 20.dp)
                        .padding(bottom = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    when {
                        ninos.isEmpty() -> {
                            Spacer(Modifier.height(48.dp))
                            Text(
                                "No hay perfiles de niños.\nCrea uno desde la pantalla principal.",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center
                            )
                        }
                        nino == null -> {
                            Spacer(Modifier.height(48.dp))
                            Text(
                                "Cargando datos del perfil…",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center
                            )
                        }
                        else -> {
                            val n = nino!!
                            val dashboard = remember(n, historial, usoDiario) {
                                construirDashboard(n, historial, usoDiario)
                            }
                            ParentResumenNino(
                                nombre = n.nombreMostrar,
                                avatarEmoji = n.avatarEmoji,
                                ultimaActividadTexto = formatearUltimaActividad(n.ultimaActividad)
                            )
                            Spacer(Modifier.height(16.dp))
                            ParentReportesDashboard(
                                dashboard = dashboard,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
        }
    }
}
