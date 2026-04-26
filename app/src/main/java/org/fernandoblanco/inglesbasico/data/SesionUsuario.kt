package org.fernandoblanco.inglesbasico.data

import android.content.Context

class SesionUsuario(context: Context) {

    private val prefs = context.applicationContext
        .getSharedPreferences(PREFS, Context.MODE_PRIVATE)

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