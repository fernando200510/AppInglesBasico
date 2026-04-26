package org.fernandoblanco.inglesbasico.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.fernandoblanco.inglesbasico.data.NinoRepository
import org.fernandoblanco.inglesbasico.data.SesionUsuario
import org.fernandoblanco.inglesbasico.db.entity.NinoEntity

class NinoPerfilViewModel(
    private val repositorio: NinoRepository,
    private val sesion: SesionUsuario
) : ViewModel() {

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje.asStateFlow()

    val perfil: StateFlow<NinoEntity?> = sesion.ninoIdActivo?.let { id ->
        repositorio.observarNino(id)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)
    } ?: flowOf<NinoEntity?>(null)
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    fun limpiarMensaje() { _mensaje.value = null }

    fun actualizarNombre(nombre: String, alExito: () -> Unit) {
        viewModelScope.launch {
            val id = sesion.ninoIdActivo ?: return@launch
            val r = repositorio.actualizarNombre(id, nombre)
            r.onSuccess { alExito() }
                .onFailure { _mensaje.value = it.message }
        }
    }

    fun actualizarAvatar(emoji: String, alExito: () -> Unit) {
        viewModelScope.launch {
            val id = sesion.ninoIdActivo ?: return@launch
            val r = repositorio.actualizarAvatar(id, emoji)
            r.onSuccess { alExito() }
                .onFailure { _mensaje.value = it.message }
        }
    }

    fun guardarMascota(mascotaId: String, alExito: () -> Unit) {
        viewModelScope.launch {
            val id = sesion.ninoIdActivo ?: return@launch
            val r = repositorio.actualizarMascota(id, mascotaId)
            r.onSuccess { alExito() }
                .onFailure { _mensaje.value = it.message }
        }
    }
}