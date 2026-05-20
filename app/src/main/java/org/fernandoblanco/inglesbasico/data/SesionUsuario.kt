package org.fernandoblanco.inglesbasico.data

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SesionUsuario(context: Context) {

    private val prefs = context.applicationContext
        .getSharedPreferences(PREFS, Context.MODE_PRIVATE)

    private val _ninoIdActivoFlow = MutableStateFlow<Long?>(null)

    /** Emite cada vez que cambia el perfil infantil activo (R21 / filtrado por usuario). */
    val ninoIdActivoFlow: StateFlow<Long?> = _ninoIdActivoFlow.asStateFlow()

    @Volatile
    var ninoTiempoActivo: Long? = null

    @Volatile
    private var ultimoCheckpointMs: Long = 0L

    fun marcarInicioMonitoreo(ninoId: Long) {
        ninoTiempoActivo = ninoId
        ultimoCheckpointMs = System.currentTimeMillis()
    }

    fun reanudarMonitoreo(ninoId: Long) {
        ninoTiempoActivo = ninoId
        if (ultimoCheckpointMs == 0L) {
            ultimoCheckpointMs = System.currentTimeMillis()
        }
    }

    fun segundosDesdeCheckpoint(): Long {
        val inicio = ultimoCheckpointMs
        if (inicio == 0L) return 0L
        return ((System.currentTimeMillis() - inicio) / 1000L).coerceAtLeast(0L)
    }

    fun consumirCheckpoint() {
        ultimoCheckpointMs = System.currentTimeMillis()
    }

    fun detenerMonitoreo() {
        ninoTiempoActivo = null
        ultimoCheckpointMs = 0L
    }

    var padreIdActivo: Long?
        get() {
            val v = prefs.getLong(KEY_PADRE_ID, -1L)
            return if (v < 0) null else v
        }
        set(value) {
            prefs.edit().apply {
                if (value == null) remove(KEY_PADRE_ID) else putLong(KEY_PADRE_ID, value)
            }.apply()
        }

    var ninoIdActivo: Long?
        get() {
            val v = prefs.getLong(KEY_NINO_ID, -1L)
            return if (v < 0) null else v
        }
        set(value) {
            prefs.edit().apply {
                if (value == null) remove(KEY_NINO_ID) else putLong(KEY_NINO_ID, value)
            }.apply()
            _ninoIdActivoFlow.value = value
        }

    init {
        _ninoIdActivoFlow.value = ninoIdActivo
    }

    fun cerrarSesionNino() {
        ninoIdActivo = null
    }

    fun cerrarSesionCompleta() {
        padreIdActivo = null
        ninoIdActivo = null
    }

    companion object {
        private const val PREFS = "sesion_ingles_basico"
        private const val KEY_PADRE_ID = "padre_id"
        private const val KEY_NINO_ID = "nino_id"
    }
}