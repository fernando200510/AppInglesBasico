package org.fernandoblanco.inglesbasico.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.fernandoblanco.inglesbasico.data.SesionUsuario
import org.fernandoblanco.inglesbasico.data.VocabItem
import org.fernandoblanco.inglesbasico.data.VocabularyBank

data class TarjetaVocab(
    val item: VocabItem,
    val vista: Boolean = false
)

class ActividadVocabularioViewModel(
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

    init { reiniciar() }

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
        val idx = _indice.value
        val lista = _tarjetas.value.toMutableList()
        if (idx < lista.size) {
            lista[idx] = lista[idx].copy(vista = true)
            _tarjetas.value = lista
        }
        _mostrarTrad.value = false
        if (idx >= TARJETAS_POR_SESION - 1) {
            _finSesion.value = true
        } else {
            _indice.value = idx + 1
        }
    }

    val tarjetaActual: VocabItem?
        get() = _tarjetas.value.getOrNull(_indice.value)?.item
}