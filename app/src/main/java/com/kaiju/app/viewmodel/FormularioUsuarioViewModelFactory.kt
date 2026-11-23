package com.kaiju.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kaiju.app.remote.BackendApiClient
import com.kaiju.app.repository.UsuarioRepository

class FormularioUsuarioViewModelFactory: ViewModelProvider.Factory {
    private val repository = UsuarioRepository(BackendApiClient.api)

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FormularioUsuarioViewModel(repository) as T
    }
}

