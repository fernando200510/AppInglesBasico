package org.fernandoblanco.inglesbasico

import android.app.Application
import org.fernandoblanco.inglesbasico.data.NinoRepository
import org.fernandoblanco.inglesbasico.data.PadreRepository
import org.fernandoblanco.inglesbasico.data.RachaProgramador
import org.fernandoblanco.inglesbasico.data.SesionUsuario
import org.fernandoblanco.inglesbasico.db.InglesDatabase

class InglesApp : Application() {
    val baseDeDatos by lazy { InglesDatabase.obtener(this) }
    val sesion by lazy { SesionUsuario(this) }
    val repositorioPadre by lazy { PadreRepository(baseDeDatos.padreDao(), sesion) }
    val repositorioNino by lazy {
        NinoRepository(
            baseDeDatos.ninoDao(),
            baseDeDatos.historialDao(),
            baseDeDatos.usoDiarioDao(),
            sesion
        )
    }

    override fun onCreate() {
        super.onCreate()
        RachaProgramador.programar(this)
    }
}
