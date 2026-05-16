package org.fernandoblanco.inglesbasico.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.fernandoblanco.inglesbasico.data.PadreRepository
import org.fernandoblanco.inglesbasico.data.SesionUsuario
import org.fernandoblanco.inglesbasico.db.entity.PadreEntity
import org.fernandoblanco.inglesbasico.security.PasswordHasher

class PadrePerfilViewModel(
    private val repositorio: PadreRepository,
    private val sesion: SesionUsuario
) : ViewModel() {

    private val _padre = MutableStateFlow<PadreEntity?>(null)
    val padre: StateFlow<PadreEntity?> = _padre.asStateFlow()

    private val _nombre = MutableStateFlow("")
    val nombre: StateFlow<String> = _nombre.asStateFlow()

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje.asStateFlow()

    private val _cargando = MutableStateFlow(false)
    val cargando: StateFlow<Boolean> = _cargando.asStateFlow()

    init { cargar() }

    fun limpiarMensaje() { _mensaje.value = null }
    fun setNombre(v: String) { _nombre.value = v }

    private fun cargar() {
        viewModelScope.launch {
            val id = sesion.padreIdActivo ?: return@launch
            val p = repositorio.obtenerPorId(id) ?: return@launch
            _padre.value = p
            _nombre.value = p.nombreMostrar
        }
    }

    fun guardar(nuevaContrasena: String?, alExito: () -> Unit) {
        viewModelScope.launch {
            _cargando.value = true
            val id = sesion.padreIdActivo ?: run { _cargando.value = false; return@launch }
            val actual = repositorio.obtenerPorId(id) ?: run { _cargando.value = false; return@launch }
            val nombre = _nombre.value.trim().ifEmpty { actual.nombreMostrar }
            val actualizado = if (!nuevaContrasena.isNullOrBlank()) {
                if (nuevaContrasena.length < 4) {
                    _mensaje.value = "La contraseña debe tener al menos 4 caracteres"
                    _cargando.value = false
                    return@launch
                }
                val sal = PasswordHasher.generarSal()
                actual.copy(nombreMostrar = nombre, sal = sal, hashContrasena = PasswordHasher.hash(nuevaContrasena, sal))
            } else {
                actual.copy(nombreMostrar = nombre)
            }
            repositorio.actualizarPadre(actualizado)
            _padre.value = actualizado
            _cargando.value = false
            alExito()
        }
    }
}