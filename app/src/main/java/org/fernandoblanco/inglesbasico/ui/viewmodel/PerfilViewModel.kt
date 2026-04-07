package org.fernandoblanco.inglesbasico.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.fernandoblanco.inglesbasico.data.SesionUsuario
import org.fernandoblanco.inglesbasico.data.UsuarioRepository

class PerfilViewModel(
    private val repositorio: UsuarioRepository,
    private val sesion: SesionUsuario
) : ViewModel() {

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje.asStateFlow()

    private val _nombreMostrar = MutableStateFlow("")
    val nombreMostrar: StateFlow<String> = _nombreMostrar.asStateFlow()

    private val _usuario = MutableStateFlow("")
    val usuario: StateFlow<String> = _usuario.asStateFlow()

    init {
        viewModelScope.launch {
            val id = sesion.usuarioIdActivo ?: return@launch
            val u = repositorio.obtenerPorId(id) ?: return@launch
            _nombreMostrar.value = u.nombreMostrar
            _usuario.value = u.usuario
        }
    }

    fun limpiarMensaje() {
        _mensaje.value = null
    }

    fun setNombreMostrar(v: String) {
        _nombreMostrar.value = v
    }

    fun guardar(nuevaContrasena: String?, alExito: () -> Unit) {
        viewModelScope.launch {
            val id = sesion.usuarioIdActivo ?: run {
                _mensaje.value = "Sesión no válida"
                return@launch
            }
            val r = repositorio.actualizarPerfil(id, _nombreMostrar.value, nuevaContrasena)
            r.onSuccess { alExito() }
                .onFailure { _mensaje.value = it.message ?: "No se pudo guardar" }
        }
    }

    fun eliminarCuenta(alExito: () -> Unit) {
        viewModelScope.launch {
            val id = sesion.usuarioIdActivo ?: run {
                _mensaje.value = "Sesión no válida"
                return@launch
            }
            val r = repositorio.eliminarPerfil(id)
            r.onSuccess { alExito() }
                .onFailure { _mensaje.value = it.message ?: "No se pudo eliminar" }
        }
    }
}
