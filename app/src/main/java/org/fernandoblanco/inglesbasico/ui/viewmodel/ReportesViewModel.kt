package org.fernandoblanco.inglesbasico.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import org.fernandoblanco.inglesbasico.data.NinoRepository
import org.fernandoblanco.inglesbasico.data.ReportesUiState
import org.fernandoblanco.inglesbasico.data.SesionUsuario
import org.fernandoblanco.inglesbasico.data.construirReportesUiState
import org.fernandoblanco.inglesbasico.db.entity.NinoEntity

/**
 * ViewModel del módulo de reportes (R17–R21).
 * Reacciona al cambio de [SesionUsuario.ninoIdActivo] y recarga datos del niño activo.
 */
class ReportesViewModel(
    private val repositorio: NinoRepository,
    sesion: SesionUsuario
) : ViewModel() {

    @OptIn(ExperimentalCoroutinesApi::class)
    val nino: StateFlow<NinoEntity?> = sesion.ninoIdActivoFlow
        .flatMapLatest { id ->
            if (id == null) flowOf(null) else repositorio.observarNino(id)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<ReportesUiState?> = sesion.ninoIdActivoFlow
        .flatMapLatest { ninoId ->
            if (ninoId == null) {
                flowOf(null)
            } else {
                combine(
                    repositorio.observarNino(ninoId),
                    repositorio.observarHistorial(ninoId),
                    repositorio.observarUsoDiario(ninoId, limite = 21)
                ) { nino, historial, usoDiario ->
                    nino?.let { construirReportesUiState(it, historial, usoDiario) }
                }
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)
}
