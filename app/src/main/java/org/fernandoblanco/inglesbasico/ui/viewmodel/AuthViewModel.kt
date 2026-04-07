package org.fernandoblanco.inglesbasico.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.fernandoblanco.inglesbasico.data.SesionUsuario
import org.fernandoblanco.inglesbasico.data.UsuarioRepository

class AuthViewModel(
    private val repositorio: UsuarioRepository,
    private val sesion: SesionUsuario
) : ViewModel() {

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje.asStateFlow()

    private val _cargando = MutableStateFlow(false)
    val cargando: StateFlow<Boolean> = _cargando.asStateFlow()

    fun limpiarMensaje() {
        _mensaje.value = null
    }

    fun iniciarSesion(usuario: String, contrasena: String, alExito: () -> Unit) {
        viewModelScope.launch {
            _cargando.value = true
            val r = repositorio.iniciarSesion(usuario, contrasena)
            _cargando.value = false
            r.onSuccess { alExito() }
                .onFailure { _mensaje.value = it.message ?: "Error al iniciar sesión" }
        }
    }

    fun registrar(usuario: String, nombre: String, contrasena: String, alExito: () -> Unit) {
        viewModelScope.launch {
            _cargando.value = true
            val r = repositorio.registrar(usuario, nombre, contrasena)
            _cargando.value = false
            r.onSuccess {
                sesion.usuarioIdActivo = it
                alExito()
            }.onFailure { _mensaje.value = it.message ?: "No se pudo crear el perfil" }
        }
    }
}
