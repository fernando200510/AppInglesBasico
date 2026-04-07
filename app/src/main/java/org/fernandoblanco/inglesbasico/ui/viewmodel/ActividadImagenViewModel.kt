package org.fernandoblanco.inglesbasico.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.fernandoblanco.inglesbasico.data.SesionUsuario
import org.fernandoblanco.inglesbasico.data.UsuarioRepository

data class PreguntaImagen(
    val consigna: String,
    val correcta: String,
    val opciones: List<String>
)

class ActividadImagenViewModel(
    private val repositorio: UsuarioRepository,
    private val sesion: SesionUsuario
) : ViewModel() {

    private val banco = listOf(
        PreguntaImagen("Elige el gato", "🐱", listOf("🐱", "🐶", "🐦", "🐟")),
        PreguntaImagen("Elige el perro", "🐶", listOf("🐱", "🐶", "🦁", "🐸")),
        PreguntaImagen("Elige el pájaro", "🐦", listOf("🐴", "🐦", "🐍", "🐙"))
    )

    private val _indice = MutableStateFlow(0)
    val indice: StateFlow<Int> = _indice.asStateFlow()

    private val _feedback = MutableStateFlow<String?>(null)
    val feedback: StateFlow<String?> = _feedback.asStateFlow()

    val preguntaActual: PreguntaImagen get() = banco[_indice.value.coerceIn(0, banco.lastIndex)]

    fun limpiarFeedback() {
        _feedback.value = null
    }

    fun responder(emoji: String) {
        viewModelScope.launch {
            val id = sesion.usuarioIdActivo ?: return@launch
            val p = preguntaActual
            val ok = emoji == p.correcta
            repositorio.registrarResultadoActividad(
                id,
                UsuarioRepository.TipoActividad.IMAGEN,
                ok
            )
            _feedback.value = if (ok) "¡Correcto!" else "Intenta de nuevo"
            if (ok && _indice.value < banco.lastIndex) {
                _indice.value = _indice.value + 1
            }
        }
    }

    fun reiniciar() {
        _indice.value = 0
        _feedback.value = null
    }
}
