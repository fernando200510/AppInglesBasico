package org.fernandoblanco.inglesbasico.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.fernandoblanco.inglesbasico.data.SesionUsuario
import org.fernandoblanco.inglesbasico.data.UsuarioRepository
import org.fernandoblanco.inglesbasico.data.VocabularyBank

data class PreguntaImagen(
    val emoji: String,
    val correctaEn: String,
    val opciones: List<String>
)

class ActividadImagenViewModel(
    private val repositorio: UsuarioRepository,
    private val sesionUsuario: SesionUsuario
) : ViewModel() {

    companion object {
        const val PREGUNTAS_POR_SESION = 10
    }

    private val pool = VocabularyBank.items

    private var preguntasSesion: List<PreguntaImagen> = emptyList()

    private val _indice = MutableStateFlow(0)
    val indice: StateFlow<Int> = _indice.asStateFlow()

    private val _aciertosSesion = MutableStateFlow(0)
    val aciertosSesion: StateFlow<Int> = _aciertosSesion.asStateFlow()

    private val _finSesion = MutableStateFlow<Pair<Int, Int>?>(null)
    /** null = en juego; Pair(aciertos, total) al terminar */
    val finSesion: StateFlow<Pair<Int, Int>?> = _finSesion.asStateFlow()

    private val _feedback = MutableStateFlow<String?>(null)
    val feedback: StateFlow<String?> = _feedback.asStateFlow()

    private val _feedbackOk = MutableStateFlow<Boolean?>(null)
    val feedbackOk: StateFlow<Boolean?> = _feedbackOk.asStateFlow()

    private val _procesando = MutableStateFlow(false)
    val procesando: StateFlow<Boolean> = _procesando.asStateFlow()

    private val _sonido = MutableSharedFlow<Boolean>(extraBufferCapacity = 1)
    val sonido: SharedFlow<Boolean> = _sonido.asSharedFlow()

    init {
        reiniciar()
    }

    val preguntaActual: PreguntaImagen?
        get() {
            if (_finSesion.value != null) return null
            val i = _indice.value
            return preguntasSesion.getOrNull(i)
        }

    fun limpiarFeedback() {
        _feedback.value = null
        _feedbackOk.value = null
    }

    fun reiniciar() {
        val barajado = pool.shuffled()
        val elegidas = barajado.take(PREGUNTAS_POR_SESION)
        preguntasSesion = elegidas.map { item ->
            val opciones = VocabularyBank.randomOptions(item, pool, 4)
            PreguntaImagen(emoji = item.emoji, correctaEn = item.en, opciones = opciones)
        }
        _indice.value = 0
        _aciertosSesion.value = 0
        _finSesion.value = null
        _feedback.value = null
        _feedbackOk.value = null
        _procesando.value = false
    }

    fun responder(inglesElegido: String) {
        if (_finSesion.value != null || _procesando.value) return
        val p = preguntaActual ?: return
        viewModelScope.launch {
            _procesando.value = true
            val id = sesionUsuario.usuarioIdActivo ?: run {
                _procesando.value = false
                return@launch
            }
            val ok = inglesElegido.equals(p.correctaEn, ignoreCase = true)
            _sonido.tryEmit(ok)
            repositorio.registrarResultadoActividad(
                id,
                UsuarioRepository.TipoActividad.IMAGEN,
                ok
            )
            _feedback.value = if (ok) mensajeCorrecto() else mensajeIncorrecto()
            _feedbackOk.value = ok
            if (ok) _aciertosSesion.value = _aciertosSesion.value + 1
            delay(750)
            _feedback.value = null
            _feedbackOk.value = null
            val ultima = _indice.value >= PREGUNTAS_POR_SESION - 1
            if (ultima) {
                _finSesion.value = _aciertosSesion.value to PREGUNTAS_POR_SESION
            } else {
                _indice.value = _indice.value + 1
            }
            _procesando.value = false
        }
    }

    private fun mensajeCorrecto(): String = listOf(
        "¡Genial!",
        "¡Muy bien!",
        "¡Lo lograste!",
        "¡Eres increíble!",
        "¡Correcto!"
    ).random()

    private fun mensajeIncorrecto(): String = listOf(
        "¡Casi! Sigue intentando",
        "¡Tú puedes! Otra vez",
        "No era esa, ¡prueba otra!",
        "¡Ánimo! Ya casi"
    ).random()
}
