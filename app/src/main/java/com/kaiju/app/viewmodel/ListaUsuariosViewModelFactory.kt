package com.kaiju.app.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kaiju.app.remote.BackendApiClient
import com.kaiju.app.repository.UsuarioRepository

class ListaUsuariosViewModelFactory(context: Context): ViewModelProvider.Factory {
    private val repository = UsuarioRepository(BackendApiClient.api)

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return InicioSesionViewModel(repository) as T
    }
}