package org.fernandoblanco.inglesbasico.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.fernandoblanco.inglesbasico.data.NinoRepository
import org.fernandoblanco.inglesbasico.data.VocabItem
import org.fernandoblanco.inglesbasico.data.VocabularyBank
import org.fernandoblanco.inglesbasico.data.SesionUsuario

data class TarjetaVocab(
    val item: VocabItem,
    val vista: Boolean = false
)

class ActividadVocabularioViewModel(
    private val repositorio: NinoRepository,
    private val sesion: SesionUsuario
) : ViewModel() {

    companion object {
        const val TARJETAS_POR_SESION = 10
    }

    private val _tarjetas = MutableStateFlow<List<TarjetaVocab>>(emptyList())
    val tarjetas: StateFlow<List<TarjetaVocab>> = _tarjetas.asStateFlow()

    private val _indice = MutableStateFlow(0)
    val indice: StateFlow<Int> = _indice.asStateFlow()

    private val _finSesion = MutableStateFlow(false)
    val finSesion: StateFlow<Boolean> = _finSesion.asStateFlow()

    private val _mostrarTrad = MutableStateFlow(false)
    val mostrarTrad: StateFlow<Boolean> = _mostrarTrad.asStateFlow()

    val tarjetaActual: StateFlow<VocabItem?> = combine(_tarjetas, _indice) { list, idx ->
        list.getOrNull(idx)?.item
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    init {
        reiniciar()
    }

    fun reiniciar() {
        val items = VocabularyBank.items.shuffled().take(TARJETAS_POR_SESION)
        _tarjetas.value = items.map { TarjetaVocab(it) }
        _indice.value = 0
        _finSesion.value = false
        _mostrarTrad.value = false
    }

    fun revelarTraduccion() {
        _mostrarTrad.value = true
    }

    fun siguiente() {
        val currentIdx = _indice.value
        if (currentIdx >= TARJETAS_POR_SESION - 1) {
            _finSesion.value = true
            val ninoId = sesion.ninoIdActivo
            if (ninoId != null) {
                viewModelScope.launch {
                    repositorio.registrarSesionVocabulario(ninoId, TARJETAS_POR_SESION)
                }
            }
        } else {
            _mostrarTrad.value = false
            _indice.value = currentIdx + 1
        }
    }
}
