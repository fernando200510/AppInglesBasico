package org.fernandoblanco.inglesbasico

import android.app.Application
import org.fernandoblanco.inglesbasico.data.SesionUsuario
import org.fernandoblanco.inglesbasico.data.UsuarioRepository
import org.fernandoblanco.inglesbasico.db.InglesDatabase

class InglesApp : Application() {
    val baseDeDatos by lazy { InglesDatabase.obtener(this) }
    val sesion by lazy { SesionUsuario(this) }
    val repositorioUsuario by lazy {
        UsuarioRepository(baseDeDatos.usuarioDao(), sesion)
    }
}