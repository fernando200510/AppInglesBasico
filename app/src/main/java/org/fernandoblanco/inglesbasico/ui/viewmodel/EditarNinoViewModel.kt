package org.fernandoblanco.inglesbasico.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.fernandoblanco.inglesbasico.data.NinoRepository
import org.fernandoblanco.inglesbasico.db.entity.NinoEntity

class EditarNinoViewModel(
    private val repositorio: NinoRepository
) : ViewModel() {

    private val _nino = MutableStateFlow<NinoEntity?>(null)
    val nino: StateFlow<NinoEntity?> = _nino.asStateFlow()

    private val _nombre = MutableStateFlow("")
    val nombre: StateFlow<String> = _nombre.asStateFlow()

    private val _emoji = MutableStateFlow("🐱")
    val emoji: StateFlow<String> = _emoji.asStateFlow()

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje.asStateFlow()

    private val _cargando = MutableStateFlow(false)
    val cargando: StateFlow<Boolean> = _cargando.asStateFlow()

    fun limpiarMensaje() { _mensaje.value = null }
    fun setNombre(v: String) { _nombre.value = v }
    fun setEmoji(v: String) { _emoji.value = v }

    fun cargar(ninoId: Long) {
        viewModelScope.launch {
            val n = repositorio.obtenerPorId(ninoId) ?: return@launch
            _nino.value = n
            _nombre.value = n.nombreMostrar
            _emoji.value = n.avatarEmoji
        }
    }

    fun guardar(ninoId: Long, alExito: () -> Unit) {
        viewModelScope.launch {
            _cargando.value = true
            val nombre = _nombre.value.trim()
            if (nombre.isBlank()) {
                _mensaje.value = "El nombre no puede estar vacío"
                _cargando.value = false
                return@launch
            }
            repositorio.actualizarNombre(ninoId, nombre)
            repositorio.actualizarAvatar(ninoId, _emoji.value)
            _cargando.value = false
            alExito()
        }
    }

    fun eliminar(ninoId: Long, alExito: () -> Unit) {
        viewModelScope.launch {
            val n = repositorio.obtenerPorId(ninoId) ?: return@launch
            repositorio.eliminarNino(n)
            alExito()
        }
    }
}