package org.fernandoblanco.inglesbasico.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.fernandoblanco.inglesbasico.InglesApp
import org.fernandoblanco.inglesbasico.ui.viewmodel.ActividadAudioViewModel
import org.fernandoblanco.inglesbasico.ui.viewmodel.ActividadChatViewModel
import org.fernandoblanco.inglesbasico.ui.viewmodel.ActividadImagenViewModel
import org.fernandoblanco.inglesbasico.ui.viewmodel.ActividadPalabrasViewModel
import org.fernandoblanco.inglesbasico.ui.viewmodel.ActividadVocabularioViewModel
import org.fernandoblanco.inglesbasico.ui.viewmodel.AuthPadreViewModel
import org.fernandoblanco.inglesbasico.ui.viewmodel.EditarNinoViewModel
import org.fernandoblanco.inglesbasico.ui.viewmodel.NinoPerfilViewModel
import org.fernandoblanco.inglesbasico.ui.viewmodel.PadrePerfilViewModel
import org.fernandoblanco.inglesbasico.ui.viewmodel.ReportesViewModel
import org.fernandoblanco.inglesbasico.ui.viewmodel.SelectorPerfilViewModel

@Suppress("UNCHECKED_CAST")
class InglesViewModelFactory(
    private val app: InglesApp
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val repoPadre = app.repositorioPadre
        val repoNino = app.repositorioNino
        val sesion = app.sesion
        return when {
            modelClass.isAssignableFrom(AuthPadreViewModel::class.java) ->
                AuthPadreViewModel(repoPadre, sesion) as T
            modelClass.isAssignableFrom(SelectorPerfilViewModel::class.java) ->
                SelectorPerfilViewModel(repoPadre, repoNino, sesion) as T
            modelClass.isAssignableFrom(NinoPerfilViewModel::class.java) ->
                NinoPerfilViewModel(repoNino, sesion) as T
            modelClass.isAssignableFrom(PadrePerfilViewModel::class.java) ->
                PadrePerfilViewModel(repoPadre, sesion) as T
            modelClass.isAssignableFrom(EditarNinoViewModel::class.java) ->
                EditarNinoViewModel(repoNino) as T
            modelClass.isAssignableFrom(ReportesViewModel::class.java) ->
                ReportesViewModel(repoNino, sesion) as T
            modelClass.isAssignableFrom(ActividadImagenViewModel::class.java) ->
                ActividadImagenViewModel(repoNino, sesion) as T
            modelClass.isAssignableFrom(ActividadAudioViewModel::class.java) ->
                ActividadAudioViewModel(app, repoNino, sesion) as T
            modelClass.isAssignableFrom(ActividadPalabrasViewModel::class.java) ->
                ActividadPalabrasViewModel(repoNino, sesion) as T
            modelClass.isAssignableFrom(ActividadVocabularioViewModel::class.java) ->
                ActividadVocabularioViewModel(sesion) as T
            modelClass.isAssignableFrom(ActividadChatViewModel::class.java) ->
                ActividadChatViewModel(repoNino, sesion) as T
            else -> throw IllegalArgumentException("ViewModel desconocido: ${modelClass.name}")
        }
    }
}