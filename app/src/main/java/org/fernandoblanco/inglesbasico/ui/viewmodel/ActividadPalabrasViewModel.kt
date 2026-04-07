package org.fernandoblanco.inglesbasico.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.fernandoblanco.inglesbasico.data.SesionUsuario
import org.fernandoblanco.inglesbasico.data.UsuarioRepository

data class PreguntaPalabra(
    val incompleta: String,
    val correcta: String,
    val opciones: List<String>
)

class ActividadPalabrasViewModel(
    private val repositorio: UsuarioRepository,
    private val sesion: SesionUsuario
) : ViewModel() {

    private val banco = listOf(
        PreguntaPalabra("H__LO", "HELLO", listOf("HELLO", "HOLLOW", "HALLO", "HILL")),
        PreguntaPalabra("B__K", "BOOK", listOf("BOOK", "BACK", "BANK", "BEAK")),
        PreguntaPalabra("W__TER", "WATER", listOf("WATER", "WAITER", "WINTER", "WAFER"))
    )

    private val _indice = MutableStateFlow(0)
    val indice: StateFlow<Int> = _indice.asStateFlow()

    private val _feedback = MutableStateFlow<String?>(null)
    val feedback: StateFlow<String?> = _feedback.asStateFlow()

    val preguntaActual: PreguntaPalabra get() = banco[_indice.value.coerceIn(0, banco.lastIndex)]

    fun limpiarFeedback() {
        _feedback.value = null
    }

    fun responder(palabra: String) {
        viewModelScope.launch {
            val id = sesion.usuarioIdActivo ?: return@launch
            val p = preguntaActual
            val ok = palabra == p.correcta
            repositorio.registrarResultadoActividad(
                id,
                UsuarioRepository.TipoActividad.PALABRAS,
                ok
            )
            _feedback.value = if (ok) "¡Bien hecho!" else "Prueba otra opción"
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
