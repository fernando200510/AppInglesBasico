package org.fernandoblanco.inglesbasico.ui.viewmodel

import android.app.Application
import android.speech.tts.TextToSpeech
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.fernandoblanco.inglesbasico.data.SesionUsuario
import org.fernandoblanco.inglesbasico.data.UsuarioRepository
import java.util.Locale

data class PreguntaAudio(
    val palabraIngles: String,
    val respuestaCorrecta: String,
    val opciones: List<String>
)

class ActividadAudioViewModel(
    application: Application,
    private val repositorio: UsuarioRepository,
    private val sesion: SesionUsuario
) : AndroidViewModel(application) {

    private val banco = listOf(
        PreguntaAudio("Hello", "Hola", listOf("Hola", "Adiós", "Gracias", "Por favor")),
        PreguntaAudio("Thank you", "Gracias", listOf("Hola", "Gracias", "Agua", "Libro")),
        PreguntaAudio("Water", "Agua", listOf("Agua", "Fuego", "Casa", "Mesa"))
    )

    private var tts: TextToSpeech? = null
    private val _ttsListo = MutableStateFlow(false)
    val ttsListo: StateFlow<Boolean> = _ttsListo.asStateFlow()

    private val _indice = MutableStateFlow(0)
    val indice: StateFlow<Int> = _indice.asStateFlow()

    private val _feedback = MutableStateFlow<String?>(null)
    val feedback: StateFlow<String?> = _feedback.asStateFlow()

    val preguntaActual: PreguntaAudio get() = banco[_indice.value.coerceIn(0, banco.lastIndex)]

    init {
        tts = TextToSpeech(application) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts?.language = Locale.US
                _ttsListo.value = true
            }
        }
    }

    fun reproducir() {
        val p = preguntaActual
        tts?.speak(p.palabraIngles, TextToSpeech.QUEUE_FLUSH, null, "ingles_${p.palabraIngles}")
    }

    fun limpiarFeedback() {
        _feedback.value = null
    }

    fun responder(texto: String) {
        viewModelScope.launch {
            val id = sesion.usuarioIdActivo ?: return@launch
            val p = preguntaActual
            val ok = texto == p.respuestaCorrecta
            repositorio.registrarResultadoActividad(
                id,
                UsuarioRepository.TipoActividad.AUDIO,
                ok
            )
            _feedback.value = if (ok) "¡Correcto!" else "Escucha otra vez e intenta de nuevo"
            if (ok && _indice.value < banco.lastIndex) {
                _indice.value = _indice.value + 1
            }
        }
    }

    fun reiniciar() {
        _indice.value = 0
        _feedback.value = null
    }

    override fun onCleared() {
        tts?.stop()
        tts?.shutdown()
        tts = null
        super.onCleared()
    }
}
