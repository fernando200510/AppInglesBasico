package org.fernandoblanco.inglesbasico.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.fernandoblanco.inglesbasico.data.NinoRepository
import org.fernandoblanco.inglesbasico.data.SesionUsuario
import org.fernandoblanco.inglesbasico.db.entity.HistorialActividadEntity
import org.fernandoblanco.inglesbasico.db.entity.NinoEntity
import org.fernandoblanco.inglesbasico.db.entity.UsoDiarioEntity

class ReportesParentalesViewModel(
    private val repositorio: NinoRepository,
    private val sesion: SesionUsuario
) : ViewModel() {

    private val _ninoIdSeleccionado = MutableStateFlow<Long?>(null)
    val ninoIdSeleccionado: StateFlow<Long?> = _ninoIdSeleccionado.asStateFlow()

    val ninos: StateFlow<List<NinoEntity>> = (sesion.padreIdActivo?.let { padreId ->
        repositorio.observarNinosDePadre(padreId)
    } ?: flowOf(emptyList()))
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    val ninoSeleccionado: StateFlow<NinoEntity?> = _ninoIdSeleccionado
        .flatMapLatest { id ->
            if (id == null) flowOf(null) else repositorio.observarNino(id)
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val historial: StateFlow<List<HistorialActividadEntity>> = _ninoIdSeleccionado
        .flatMapLatest { id ->
            if (id == null) flowOf(emptyList()) else repositorio.observarHistorial(id)
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    val usoDiario: StateFlow<List<UsoDiarioEntity>> = _ninoIdSeleccionado
        .flatMapLatest { id ->
            if (id == null) flowOf(emptyList()) else repositorio.observarUsoDiario(id)
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    init {
        viewModelScope.launch {
            ninos.collect { lista ->
                if (lista.isEmpty()) {
                    _ninoIdSeleccionado.value = null
                    return@collect
                }
                val actual = _ninoIdSeleccionado.value
                if (actual == null || lista.none { it.id == actual }) {
                    _ninoIdSeleccionado.value = lista.first().id
                }
            }
        }
    }

    fun seleccionarNino(ninoId: Long) {
        _ninoIdSeleccionado.value = ninoId
    }
}
