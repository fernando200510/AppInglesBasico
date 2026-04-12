package org.fernandoblanco.inglesbasico.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.fernandoblanco.inglesbasico.InglesApp
import org.fernandoblanco.inglesbasico.ui.viewmodel.ActividadAudioViewModel
import org.fernandoblanco.inglesbasico.ui.viewmodel.ActividadChatViewModel
import org.fernandoblanco.inglesbasico.ui.viewmodel.ActividadImagenViewModel
import org.fernandoblanco.inglesbasico.ui.viewmodel.ActividadPalabrasViewModel
import org.fernandoblanco.inglesbasico.ui.viewmodel.AuthViewModel
import org.fernandoblanco.inglesbasico.ui.viewmodel.PerfilViewModel
import org.fernandoblanco.inglesbasico.ui.viewmodel.ReportesViewModel

@Suppress("UNCHECKED_CAST")
class InglesViewModelFactory(
    private val app: InglesApp
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val repo = app.repositorioUsuario
        val sesion = app.sesion
        return when {
            modelClass.isAssignableFrom(AuthViewModel::class.java) ->
                AuthViewModel(repo, sesion) as T
            modelClass.isAssignableFrom(PerfilViewModel::class.java) ->
                PerfilViewModel(repo, sesion) as T
            modelClass.isAssignableFrom(ReportesViewModel::class.java) ->
                ReportesViewModel(repo, sesion) as T
            modelClass.isAssignableFrom(ActividadImagenViewModel::class.java) ->
                ActividadImagenViewModel(repo, sesion) as T
            modelClass.isAssignableFrom(ActividadAudioViewModel::class.java) ->
                ActividadAudioViewModel(app, repo, sesion) as T
            modelClass.isAssignableFrom(ActividadPalabrasViewModel::class.java) ->
                ActividadPalabrasViewModel(repo, sesion) as T
            modelClass.isAssignableFrom(ActividadChatViewModel::class.java) ->
                ActividadChatViewModel(repo, sesion) as T
            else -> throw IllegalArgumentException("ViewModel desconocido: ${modelClass.name}")
        }
    }
}