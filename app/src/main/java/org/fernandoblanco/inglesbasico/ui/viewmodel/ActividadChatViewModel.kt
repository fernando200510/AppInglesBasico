package org.fernandoblanco.inglesbasico.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.fernandoblanco.inglesbasico.data.CompaneroData
import org.fernandoblanco.inglesbasico.data.NinoRepository
import org.fernandoblanco.inglesbasico.data.SesionUsuario
import org.fernandoblanco.inglesbasico.data.VocabItem
import org.fernandoblanco.inglesbasico.data.VocabularyBank

data class MensajeChat(
    val textoIngles: String,
    val textoEspanol: String,
    val opciones: List<String>,
    val fueCorrecto: Boolean? = null
)

data class EstadoEmocion(
    val emoji: String,
    val animacion: String
)

class ActividadChatViewModel(
    private val repositorio: NinoRepository,
    private val sesion: SesionUsuario
) : ViewModel() {

    companion object {
        const val TURNOS_POR_SESION = 6
    }

    private val _mensajes = MutableStateFlow<List<MensajeChat>>(emptyList())
    val mensajes: StateFlow<List<MensajeChat>> = _mensajes.asStateFlow()

    private val _cargando = MutableStateFlow(false)
    val cargando: StateFlow<Boolean> = _cargando.asStateFlow()

    private val _turnoActual = MutableStateFlow(0)
    val turnoActual: StateFlow<Int> = _turnoActual.asStateFlow()

    private val _finSesion = MutableStateFlow(false)
    val finSesion: StateFlow<Boolean> = _finSesion.asStateFlow()

    private val _emocionCompanero = MutableStateFlow(EstadoEmocion("😊", "idle"))
    val emocionCompanero: StateFlow<EstadoEmocion> = _emocionCompanero.asStateFlow()

    private val _aciertos = MutableStateFlow(0)
    val aciertos: StateFlow<Int> = _aciertos.asStateFlow()

    private val _companero = MutableStateFlow(CompaneroData.todos.first())
    val companero: StateFlow<org.fernandoblanco.inglesbasico.data.Companero> = _companero.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private var nivelNino = 1
    private val tiposPregunta = listOf("imagen", "español", "definicion")
    private var preguntasSesion: List<VocabItem> = emptyList()
    private var indicePregunta = 0

    init { inicializar() }

    private fun inicializar() {
        viewModelScope.launch {
            val id = sesion.ninoIdActivo ?: return@launch
            val n = repositorio.obtenerPorId(id) ?: return@launch
            nivelNino = n.nivel
            _companero.value = CompaneroData.obtenerPorId(n.mascotaId)
            preguntasSesion = VocabularyBank.items.shuffled().take(TURNOS_POR_SESION)
            mostrarBienvenida()
        }
    }

    private fun mostrarBienvenida() {
        viewModelScope.launch {
            _cargando.value = true
            _emocionCompanero.value = EstadoEmocion("🤔", "thinking")
            delay(800)
            val comp = _companero.value
            val saludo = comp.frasesBienvenida.random()
            val primera = preguntasSesion.firstOrNull()
            if (primera != null) {
                val opciones = VocabularyBank.randomOptions(primera, VocabularyBank.items, 4)
                val tipo = tiposPregunta.random()
                val (textoEn, textoEs) = generarPregunta(primera, tipo)
                _mensajes.value = listOf(
                    MensajeChat(
                        textoIngles = "Hello! (¡Hola!) I'm ${comp.nombre}! 😄 $textoEn",
                        textoEspanol = "$saludo $textoEs",
                        opciones = opciones
                    )
                )
            }
            _emocionCompanero.value = EstadoEmocion("😊", "idle")
            _cargando.value = false
        }
    }

    fun responderOpcion(opcionElegida: String) {
        if (_cargando.value || _finSesion.value) return
        val turnoAntes = _turnoActual.value
        val itemActual = preguntasSesion.getOrNull(indicePregunta) ?: return
        viewModelScope.launch {
            _cargando.value = true
            _emocionCompanero.value = EstadoEmocion("🤔", "thinking")
            delay(700)
            val fueCorrecto = opcionElegida.equals(itemActual.en, ignoreCase = true)
            val comp = _companero.value
            val id = sesion.ninoIdActivo
            if (fueCorrecto) {
                _aciertos.value = _aciertos.value + 1
                _emocionCompanero.value = EstadoEmocion("🎉", "happy")
                if (id != null) {
                    repositorio.registrarResultadoActividad(id, NinoRepository.TipoActividad.PALABRAS, true)
                    repositorio.actualizarRacha(id)
                }
            } else {
                _emocionCompanero.value = EstadoEmocion("😢", "sad")
                if (id != null) repositorio.registrarResultadoActividad(id, NinoRepository.TipoActividad.PALABRAS, false)
            }
            _turnoActual.value = turnoAntes + 1
            indicePregunta++
            val esUltimo = _turnoActual.value >= TURNOS_POR_SESION
            if (esUltimo) {
                val feedbackFinal = if (fueCorrecto) comp.frasesAcierto.random() else comp.frasesFallo.random()
                val despedida = generarDespedida(_aciertos.value, TURNOS_POR_SESION)
                _mensajes.value = _mensajes.value + MensajeChat(
                    textoIngles = if (fueCorrecto) "Correct! (¡Correcto!) 🎉 $despedida"
                    else "Almost! (¡Casi!) The answer was: ${itemActual.en} ${itemActual.emoji}. $despedida",
                    textoEspanol = feedbackFinal,
                    opciones = emptyList(),
                    fueCorrecto = fueCorrecto
                )
                _finSesion.value = true
                _emocionCompanero.value = EstadoEmocion("🏆", "celebrate")
            } else {
                val siguienteItem = preguntasSesion.getOrNull(indicePregunta)
                if (siguienteItem != null) {
                    val tipo = tiposPregunta.random()
                    val (textoEn, textoEs) = generarPregunta(siguienteItem, tipo)
                    val opciones = VocabularyBank.randomOptions(siguienteItem, VocabularyBank.items, 4)
                    val feedbackEn = if (fueCorrecto)
                        listOf("Correct! (¡Correcto!) 🎉", "Amazing! (¡Increíble!) ⭐", "Great job! (¡Muy bien!) 🌟").random()
                    else
                        listOf("Almost! (¡Casi!) The answer was: ${itemActual.en} ${itemActual.emoji}", "Not quite! (¡No era esa!) It was: ${itemActual.en} ${itemActual.emoji}").random()
                    val feedbackEs = if (fueCorrecto) comp.frasesAcierto.random() else comp.frasesFallo.random()
                    _mensajes.value = _mensajes.value + MensajeChat(
                        textoIngles = "$feedbackEn Now... $textoEn",
                        textoEspanol = "$feedbackEs $textoEs",
                        opciones = opciones,
                        fueCorrecto = fueCorrecto
                    )
                }
            }
            _cargando.value = false
        }
    }

    fun limpiarError() { _error.value = null }

    fun reiniciar() {
        _mensajes.value = emptyList()
        _turnoActual.value = 0
        _finSesion.value = false
        _aciertos.value = 0
        _error.value = null
        indicePregunta = 0
        _emocionCompanero.value = EstadoEmocion("😊", "idle")
        preguntasSesion = VocabularyBank.items.shuffled().take(TURNOS_POR_SESION)
        mostrarBienvenida()
    }

    private fun generarPregunta(item: VocabItem, tipo: String): Pair<String, String> {
        return when (tipo) {
            "imagen" -> Pair("What is this? (¿Qué es esto?) ${item.emoji}", "¿Cuál es la palabra en inglés para ${item.emoji}?")
            "español" -> Pair("How do you say \"${item.es}\" in English? (¿Cómo se dice \"${item.es}\" en inglés?)", "Elige la traducción correcta al inglés.")
            else -> Pair("Which word means \"${item.es}\"? (¿Cuál palabra significa \"${item.es}\"?)", "Selecciona la palabra correcta en inglés.")
        }
    }

    private fun generarDespedida(aciertos: Int, total: Int): String {
        val p = (aciertos.toFloat() / total * 100).toInt()
        return when {
            p == 100 -> "Perfect score! (¡Puntaje perfecto!) 🏆🌟"
            p >= 70 -> "Great session! (¡Gran sesión!) 🎉 $aciertos/$total correct!"
            p >= 50 -> "Good effort! (¡Buen esfuerzo!) 💪 Keep practicing!"
            else -> "Keep going! (¡Sigue adelante!) 🌈 You'll improve!"
        }
    }
}