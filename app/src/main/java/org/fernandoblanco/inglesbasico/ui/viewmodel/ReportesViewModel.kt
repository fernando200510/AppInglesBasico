package org.fernandoblanco.inglesbasico.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import org.fernandoblanco.inglesbasico.data.SesionUsuario
import org.fernandoblanco.inglesbasico.data.UsuarioRepository
import org.fernandoblanco.inglesbasico.db.entity.UsuarioEntity

class ReportesViewModel(
    repositorio: UsuarioRepository,
    sesion: SesionUsuario
) : ViewModel() {

    val usuario: StateFlow<UsuarioEntity?> = sesion.usuarioIdActivo?.let { id ->
        repositorio.observarUsuarioActual(id)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)
    } ?: flowOf<UsuarioEntity?>(null)
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)
}
