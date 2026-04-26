package org.fernandoblanco.inglesbasico.ui.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.fernandoblanco.inglesbasico.data.SesionUsuario
import org.fernandoblanco.inglesbasico.data.NinoRepository
import org.fernandoblanco.inglesbasico.db.entity.NinoEntity

class PerfilViewModel(
    private val repositorio: NinoRepository,
    private val sesion: SesionUsuario
) : ViewModel() {

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje.asStateFlow()

    private val _nombreMostrar = MutableStateFlow("")
    val nombreMostrar: StateFlow<String> = _nombreMostrar.asStateFlow()

    val perfil: StateFlow<NinoEntity?> = sesion.ninoIdActivo?.let { id ->
        repositorio.observarNino(id)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)
    } ?: flowOf<NinoEntity?>(null)
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    init {
        viewModelScope.launch {
            val id = sesion.ninoIdActivo ?: return@launch
            val nino = repositorio.obtenerPorId(id) ?: return@launch
            _nombreMostrar.value = nino.nombreMostrar
        }
    }

    fun limpiarMensaje() {
        _mensaje.value = null
    }

    fun setNombreMostrar(v: String) {
        _nombreMostrar.value = v
    }

    fun guardarAvatar(emoji: String, alExito: () -> Unit) {
        viewModelScope.launch {
            val id = sesion.ninoIdActivo ?: run {
                _mensaje.value = "Sesión no válida"
                return@launch
            }
            val r = repositorio.actualizarAvatar(id, emoji)
            r.onSuccess { alExito() }
                .onFailure { _mensaje.value = it.message ?: "No se pudo guardar el avatar" }
        }
    }

    fun guardarCambios(alExito: () -> Unit) {
        viewModelScope.launch {
            val id = sesion.ninoIdActivo ?: run {
                _mensaje.value = "Sesión no válida"
                return@launch
            }
            val r = repositorio.actualizarNombre(id, _nombreMostrar.value)
            r.onSuccess { alExito() }
                .onFailure { _mensaje.value = it.message ?: "No se pudo guardar" }
        }
    }

    fun eliminarPerfilActual(alExito: () -> Unit) {
        viewModelScope.launch {
            val id = sesion.ninoIdActivo ?: return@launch
            val nino = repositorio.obtenerPorId(id) ?: return@launch
            val r = repositorio.eliminarNino(nino)
            r.onSuccess { alExito() }
                .onFailure { _mensaje.value = it.message ?: "No se pudo eliminar" }
        }
    }
}