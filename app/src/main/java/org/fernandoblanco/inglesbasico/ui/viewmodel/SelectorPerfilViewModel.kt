package org.fernandoblanco.inglesbasico.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.fernandoblanco.inglesbasico.data.NinoRepository
import org.fernandoblanco.inglesbasico.data.PadreRepository
import org.fernandoblanco.inglesbasico.data.SesionUsuario
import org.fernandoblanco.inglesbasico.db.entity.NinoEntity
import org.fernandoblanco.inglesbasico.db.entity.PadreEntity

class SelectorPerfilViewModel(
    private val repoPadre: PadreRepository,
    private val repoNino: NinoRepository,
    private val sesion: SesionUsuario
) : ViewModel() {

    private val _padre = MutableStateFlow<PadreEntity?>(null)
    val padre: StateFlow<PadreEntity?> = _padre.asStateFlow()

    private val _ninos = MutableStateFlow<List<NinoEntity>>(emptyList())
    val ninos: StateFlow<List<NinoEntity>> = _ninos.asStateFlow()

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje.asStateFlow()

    private val _cargando = MutableStateFlow(false)
    val cargando: StateFlow<Boolean> = _cargando.asStateFlow()

    private val _verificandoPin = MutableStateFlow(false)
    val verificandoPin: StateFlow<Boolean> = _verificandoPin.asStateFlow()

    init { cargar() }

    fun limpiarMensaje() { _mensaje.value = null }

    private fun cargar() {
        viewModelScope.launch {
            val padreId = sesion.padreIdActivo ?: return@launch
            _padre.value = repoPadre.obtenerPorId(padreId)
            repoNino.observarNinosDePadre(padreId).collect { lista ->
                _ninos.value = lista
            }
        }
    }

    fun seleccionarNino(ninoId: Long, alExito: () -> Unit) {
        sesion.ninoIdActivo = ninoId
        alExito()
    }

    fun verificarContrasenaParaSalir(contrasena: String, alExito: () -> Unit) {
        viewModelScope.launch {
            _verificandoPin.value = true
            val padreId = sesion.padreIdActivo ?: run {
                _verificandoPin.value = false
                return@launch
            }
            val ok = repoPadre.verificarContrasena(padreId, contrasena)
            _verificandoPin.value = false
            if (ok) {
                sesion.ninoIdActivo = null
                alExito()
            } else {
                _mensaje.value = "Contraseña incorrecta"
            }
        }
    }

    fun cerrarSesionCompleta(alExito: () -> Unit) {
        sesion.padreIdActivo = null
        sesion.ninoIdActivo = null
        alExito()
    }

    fun crearNino(nombre: String, emoji: String, alExito: () -> Unit) {
        viewModelScope.launch {
            _cargando.value = true
            val r = repoNino.crearNino(nombre, emoji)
            _cargando.value = false
            r.onSuccess { alExito() }
                .onFailure { _mensaje.value = it.message ?: "No se pudo crear el perfil" }
        }
    }

    fun eliminarNino(ninoId: Long) {
        viewModelScope.launch {
            val nino = repoNino.obtenerPorId(ninoId) ?: return@launch
            repoNino.eliminarNino(nino)
        }
    }
}