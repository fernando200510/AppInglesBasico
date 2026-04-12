package org.fernandoblanco.inglesbasico.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.fernandoblanco.inglesbasico.data.CompaneroData
import org.fernandoblanco.inglesbasico.data.SesionUsuario
import org.fernandoblanco.inglesbasico.data.UsuarioRepository
import org.fernandoblanco.inglesbasico.data.VocabularyBank
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

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
    private val repositorio: UsuarioRepository,
    private val sesion: SesionUsuario
) : ViewModel() {

    companion object {
        const val TURNOS_POR_SESION = 6
        private const val API_URL = "https://api.anthropic.com/v1/messages"
        private const val API_KEY = "TU_API_KEY_AQUI"
        private const val MODELO = "claude-opus-4-6"
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

    private var nivelUsuario = 1
    private val historialApi = mutableListOf<Map<String, String>>()
    private var vocabularioSesion = ""

    init {
        inicializar()
    }

    private fun inicializar() {
        viewModelScope.launch {
            val id = sesion.usuarioIdActivo ?: return@launch
            val u = repositorio.obtenerPorId(id) ?: return@launch
            nivelUsuario = u.nivel
            _companero.value = CompaneroData.obtenerPorId(u.mascotaId)
            vocabularioSesion = VocabularyBank.items
                .shuffled()
                .take(8)
                .joinToString(", ") { "${it.emoji} ${it.en} = ${it.es}" }
            pedirPrimerMensaje()
        }
    }

    private fun pedirPrimerMensaje() {
        viewModelScope.launch {
            _cargando.value = true
            _emocionCompanero.value = EstadoEmocion("🤔", "thinking")
            historialApi.clear()

            val userMsg = "Comienza la sesión. Saluda al niño con entusiasmo en inglés (con traducción al español entre paréntesis) y haz tu primera pregunta con exactamente 4 opciones de respuesta. Solo JSON."
            historialApi.add(mapOf("role" to "user", "content" to userMsg))

            val respuesta = llamarApi(construirSystemPrompt())
            val mensaje = parsearRespuesta(respuesta)

            if (mensaje != null) {
                historialApi.add(mapOf("role" to "assistant", "content" to respuesta))
                _mensajes.value = listOf(mensaje)
                _emocionCompanero.value = EstadoEmocion("😊", "idle")
            } else {
                _error.value = "No se pudo conectar con el tutor. Verifica tu conexión."
                _emocionCompanero.value = EstadoEmocion("😅", "idle")
            }
            _cargando.value = false
        }
    }

    fun responderOpcion(opcionElegida: String) {
        if (_cargando.value || _finSesion.value) return
        val turnoAntes = _turnoActual.value
        viewModelScope.launch {
            _cargando.value = true
            _emocionCompanero.value = EstadoEmocion("🤔", "thinking")

            val esUltimoTurno = turnoAntes >= TURNOS_POR_SESION - 1
            val instruccion = if (esUltimoTurno) {
                "El niño eligió: \"$opcionElegida\". Evalúa si fue correcto, da un feedback muy motivador y despídete con cariño. Es el último turno. Pon opciones vacías []."
            } else {
                "El niño eligió: \"$opcionElegida\". Evalúa si fue correcto con feedback amigable y haz la siguiente pregunta con exactamente 4 opciones. Turno ${turnoAntes + 2} de $TURNOS_POR_SESION."
            }

            historialApi.add(mapOf("role" to "user", "content" to instruccion))

            val respuesta = llamarApi(construirSystemPrompt())
            val mensaje = parsearRespuesta(respuesta)

            if (mensaje != null) {
                historialApi.add(mapOf("role" to "assistant", "content" to respuesta))
                _mensajes.value = _mensajes.value + mensaje

                val fueCorrecto = mensaje.fueCorrecto ?: false
                val id = sesion.usuarioIdActivo

                if (fueCorrecto) {
                    _aciertos.value = _aciertos.value + 1
                    _emocionCompanero.value = EstadoEmocion("🎉", "happy")
                } else {
                    _emocionCompanero.value = EstadoEmocion("😢", "sad")
                }

                if (id != null) {
                    repositorio.registrarResultadoActividad(
                        id,
                        UsuarioRepository.TipoActividad.PALABRAS,
                        fueCorrecto
                    )
                    repositorio.actualizarRacha(id)
                }

                _turnoActual.value = turnoAntes + 1

                if (esUltimoTurno) {
                    _finSesion.value = true
                    _emocionCompanero.value = EstadoEmocion("🏆", "celebrate")
                }
            } else {
                _error.value = "Error al procesar la respuesta. Intenta de nuevo."
                _emocionCompanero.value = EstadoEmocion("😅", "idle")
            }
            _cargando.value = false
        }
    }

    fun limpiarError() {
        _error.value = null
    }

    fun reiniciar() {
        _mensajes.value = emptyList()
        _turnoActual.value = 0
        _finSesion.value = false
        _aciertos.value = 0
        _error.value = null
        _emocionCompanero.value = EstadoEmocion("😊", "idle")
        historialApi.clear()
        vocabularioSesion = VocabularyBank.items
            .shuffled()
            .take(8)
            .joinToString(", ") { "${it.emoji} ${it.en} = ${it.es}" }
        pedirPrimerMensaje()
    }

    private fun construirSystemPrompt(): String {
        val nivelTexto = when {
            nivelUsuario <= 2 -> "muy básico: colores, animales, números del 1 al 10, saludos"
            nivelUsuario <= 5 -> "básico: comida, ropa, familia, partes del cuerpo, verbos simples"
            else -> "intermedio: verbos en presente, adjetivos, rutinas diarias, descripciones"
        }
        return """
Eres un tutor de inglés súper amigable y divertido para niños hispanohablantes de 6 a 12 años.
El nivel del estudiante es: $nivelTexto.
Vocabulario sugerido para esta sesión: $vocabularioSesion.

REGLAS ABSOLUTAS:
1. Responde ÚNICAMENTE con un objeto JSON válido. Sin texto antes ni después. Sin comillas de código.
2. Cada mensaje en inglés DEBE incluir la traducción al español entre paréntesis, por ejemplo: "Hello! (¡Hola!) What is this? (¿Qué es esto?) 🐶"
3. El campo "textoEspanol" es un resumen adicional en español puro para que el niño entienda todo.
4. Las preguntas deben ser simples, visuales y usar emojis.
5. Cuando evalúes una respuesta del niño, debes indicar "fueCorrecto": true o false.
6. Siempre incluye exactamente 4 opciones a menos que sea la despedida final.

FORMATO JSON ESTRICTO:
{
  "textoIngles": "texto en inglés con (traducción en español) incluida",
  "textoEspanol": "explicación adicional en español",
  "opciones": ["Opción A", "Opción B", "Opción C", "Opción D"],
  "fueCorrecto": true
}

Nota: En el primer mensaje omite "fueCorrecto". En despedida final pon "opciones": [].
        """.trimIndent()
    }

    private suspend fun llamarApi(systemPrompt: String): String {
        return withContext(Dispatchers.IO) {
            try {
                val url = URL(API_URL)
                val conn = url.openConnection() as HttpURLConnection
                conn.requestMethod = "POST"
                conn.setRequestProperty("Content-Type", "application/json")
                conn.setRequestProperty("x-api-key", API_KEY)
                conn.setRequestProperty("anthropic-version", "2023-06-01")
                conn.connectTimeout = 15000
                conn.readTimeout = 30000
                conn.doOutput = true

                val mensajesJson = JSONArray()
                historialApi.forEach { msg ->
                    mensajesJson.put(
                        JSONObject().apply {
                            put("role", msg["role"])
                            put("content", msg["content"])
                        }
                    )
                }

                val body = JSONObject().apply {
                    put("model", MODELO)
                    put("max_tokens", 1024)
                    put("system", systemPrompt)
                    put("messages", mensajesJson)
                }.toString()

                conn.outputStream.write(body.toByteArray(Charsets.UTF_8))
                conn.outputStream.flush()

                val codigo = conn.responseCode
                val texto = if (codigo == 200) {
                    conn.inputStream.bufferedReader(Charsets.UTF_8).readText()
                } else {
                    conn.errorStream?.bufferedReader(Charsets.UTF_8)?.readText() ?: ""
                }
                conn.disconnect()

                if (codigo != 200) return@withContext ""

                val jsonResp = JSONObject(texto)
                jsonResp.getJSONArray("content").getJSONObject(0).getString("text")

            } catch (e: Exception) {
                ""
            }
        }
    }

    private fun parsearRespuesta(raw: String): MensajeChat? {
        if (raw.isBlank()) return null
        return try {
            val limpio = raw.trim()
                .removePrefix("```json")
                .removePrefix("```")
                .removeSuffix("```")
                .trim()

            val obj = JSONObject(limpio)
            val textoIngles = obj.optString("textoIngles", "").ifBlank { return null }
            val textoEspanol = obj.optString("textoEspanol", "")
            val opcionesArr = obj.optJSONArray("opciones")
            val opciones = if (opcionesArr != null) {
                (0 until opcionesArr.length())
                    .map { opcionesArr.getString(it) }
                    .filter { it.isNotBlank() }
            } else emptyList()
            val fueCorrecto = if (obj.has("fueCorrecto")) obj.getBoolean("fueCorrecto") else null

            MensajeChat(
                textoIngles = textoIngles,
                textoEspanol = textoEspanol,
                opciones = opciones,
                fueCorrecto = fueCorrecto
            )
        } catch (e: Exception) {
            null
        }
    }
}