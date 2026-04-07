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

data class PreguntaPalabra(
    val incompleta: String,
    val correcta: String,
    val opciones: List<String>
)

class ActividadPalabrasViewModel(
    private val repositorio: UsuarioRepository,
    private val sesionUsuario: SesionUsuario
) : ViewModel() {

    companion object {
        const val PREGUNTAS_POR_SESION = 10
    }

    private val pool = VocabularyBank.items.filter { it.en.any { ch -> ch.isLetter() } && it.en.length >= 4 }

    private var preguntasSesion: List<PreguntaPalabra> = emptyList()

    private val _indice = MutableStateFlow(0)
    val indice: StateFlow<Int> = _indice.asStateFlow()

    private val _aciertosSesion = MutableStateFlow(0)
    val aciertosSesion: StateFlow<Int> = _aciertosSesion.asStateFlow()

    private val _finSesion = MutableStateFlow<Pair<Int, Int>?>(null)
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

    val preguntaActual: PreguntaPalabra?
        get() {
            if (_finSesion.value != null) return null
            return preguntasSesion.getOrNull(_indice.value)
        }

    fun limpiarFeedback() {
        _feedback.value = null
        _feedbackOk.value = null
    }

    fun reiniciar() {
        val barajado = pool.shuffled()
        val elegidas = barajado.take(PREGUNTAS_POR_SESION)
        preguntasSesion = elegidas.map { item ->
            val (mask, full) = VocabularyBank.enmascarar(item.en)
            val wrong = pool
                .filter { it.en.uppercase() != full }
                .shuffled()
                .take(3)
                .map { VocabularyBank.enmascarar(it.en).second }
            val opciones = (wrong + full).distinct().shuffled()
            PreguntaPalabra(incompleta = mask, correcta = full, opciones = opciones)
        }
        _indice.value = 0
        _aciertosSesion.value = 0
        _finSesion.value = null
        _feedback.value = null
        _feedbackOk.value = null
        _procesando.value = false
    }

    fun responder(palabra: String) {
        if (_finSesion.value != null || _procesando.value) return
        val p = preguntaActual ?: return
        viewModelScope.launch {
            _procesando.value = true
            val id = sesionUsuario.usuarioIdActivo ?: run {
                _procesando.value = false
                return@launch
            }
            val ok = palabra.equals(p.correcta, ignoreCase = true)
            _sonido.tryEmit(ok)
            repositorio.registrarResultadoActividad(
                id,
                UsuarioRepository.TipoActividad.PALABRAS,
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
        "¡Fantástico!",
        "¡Palabra completada!",
        "¡Eres un campeón!",
        "¡Muy bien!",
        "¡Súper!"
    ).random()

    private fun mensajeIncorrecto(): String = listOf(
        "Prueba otra letra…",
        "¡Casi! Otra opción",
        "¡Ánimo!",
        "Sigue intentando"
    ).random()
}
