package org.fernandoblanco.inglesbasico.data

import android.content.Context

class SesionUsuario(context: Context) {

    private val prefs = context.applicationContext.getSharedPreferences(PREFS, Context.MODE_PRIVATE)

    var usuarioIdActivo: Long?
        get() {
            val v = prefs.getLong(KEY_USUARIO_ID, -1L)
            return if (v < 0) null else v
        }
        set(value) {
            prefs.edit().apply {
                if (value == null) remove(KEY_USUARIO_ID) else putLong(KEY_USUARIO_ID, value)
            }.apply()
        }

    fun cerrarSesion() {
        usuarioIdActivo = null
    }

    companion object {
        private const val PREFS = "sesion_ingles_basico"
        private const val KEY_USUARIO_ID = "usuario_id"
    }
}
