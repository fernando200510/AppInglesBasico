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
    val correctaEs: String,
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

    private val _preguntaVisible = MutableStateFlow<PreguntaImagen?>(null)
    val preguntaVisible: StateFlow<PreguntaImagen?> = _preguntaVisible.asStateFlow()

    private val _aciertosSesion = MutableStateFlow(0)
    val aciertosSesion: StateFlow<Int> = _aciertosSesion.asStateFlow()

    private val _finSesion = MutableStateFlow<Pair<Int, Int>?>(null)
    val finSesion: StateFlow<Pair<Int, Int>?> = _finSesion.asStateFlow()

    private val _feedback = MutableStateFlow<String?>(null)
    val feedback: StateFlow<String?> = _feedback.asStateFlow()

    private val _feedbackOk = MutableStateFlow<Boolean?>(null)
    val feedbackOk: StateFlow<Boolean?> = _feedbackOk.asStateFlow()

    private val _solucionCorrecta = MutableStateFlow<String?>(null)
    val solucionCorrecta: StateFlow<String?> = _solucionCorrecta.asStateFlow()

    private val _procesando = MutableStateFlow(false)
    val procesando: StateFlow<Boolean> = _procesando.asStateFlow()

    private val _sonido = MutableSharedFlow<Boolean>(extraBufferCapacity = 1)
    val sonido: SharedFlow<Boolean> = _sonido.asSharedFlow()

    init {
        reiniciar()
    }

    private fun sincronizarPreguntaVisible() {
        _preguntaVisible.value = when {
            _finSesion.value != null -> null
            else -> preguntasSesion.getOrNull(_indice.value)
        }
    }

    fun reiniciar() {
        val base = pool.shuffled().distinctBy { it.enKey.lowercase() }.take(PREGUNTAS_POR_SESION)
        preguntasSesion = base.map { item ->
            val opcionesItems = VocabularyBank.randomOptions(item, pool, 4)
            val opcionesConTrad = opcionesItems.map { enTexto ->
                val vocabItem = pool.find { it.en == enTexto }
                if (vocabItem != null) "${vocabItem.emoji} $enTexto — ${vocabItem.es}"
                else enTexto
            }
            PreguntaImagen(
                emoji = item.emoji,
                correctaEn = item.en,
                correctaEs = item.es,
                opciones = opcionesConTrad
            )
        }
        _indice.value = 0
        _aciertosSesion.value = 0
        _finSesion.value = null
        _feedback.value = null
        _feedbackOk.value = null
        _solucionCorrecta.value = null
        _procesando.value = false
        sincronizarPreguntaVisible()
    }

    fun responder(opcionElegida: String) {
        if (_finSesion.value != null || _procesando.value) return
        val idx = _indice.value
        val pregunta = preguntasSesion.getOrNull(idx) ?: return
        _procesando.value = true
        viewModelScope.launch {
            val id = sesionUsuario.usuarioIdActivo
            if (id == null) { _procesando.value = false; return@launch }
            val ok = opcionElegida.contains(pregunta.correctaEn, ignoreCase = true)
            _sonido.tryEmit(ok)
            repositorio.registrarResultadoActividad(id, UsuarioRepository.TipoActividad.IMAGEN, ok)
            _feedbackOk.value = ok
            _feedback.value = if (ok) mensajeCorrecto() else mensajeIncorrecto()
            _solucionCorrecta.value = if (ok) null else "${pregunta.emoji} ${pregunta.correctaEn} — ${pregunta.correctaEs}"
            if (ok) _aciertosSesion.value = _aciertosSesion.value + 1
            delay(950)
            _feedback.value = null
            _feedbackOk.value = null
            _solucionCorrecta.value = null
            if (idx >= PREGUNTAS_POR_SESION - 1) {
                _finSesion.value = _aciertosSesion.value to PREGUNTAS_POR_SESION
            } else {
                _indice.value = idx + 1
            }
            sincronizarPreguntaVisible()
            _procesando.value = false
        }
    }

    private fun mensajeCorrecto() = listOf("¡Genial!", "¡Muy bien!", "¡Lo lograste!", "¡Eres increíble!", "¡Correcto!").random()
    private fun mensajeIncorrecto() = listOf("¡Casi!", "¡Sigue intentando!", "Esa no era", "¡Ánimo!").random()
}