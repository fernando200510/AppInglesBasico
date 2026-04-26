package org.fernandoblanco.inglesbasico.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import org.fernandoblanco.inglesbasico.data.NinoRepository
import org.fernandoblanco.inglesbasico.data.SesionUsuario
import org.fernandoblanco.inglesbasico.db.entity.NinoEntity

class ReportesViewModel(
    repositorio: NinoRepository,
    sesion: SesionUsuario
) : ViewModel() {

    val nino: StateFlow<NinoEntity?> = sesion.ninoIdActivo?.let { id ->
        repositorio.observarNino(id)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)
    } ?: flowOf<NinoEntity?>(null)
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)
}