package com.kaiju.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kaiju.app.model.Usuario
import com.kaiju.app.repository.UsuarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetalleUsuarioViewModel(private val repository: UsuarioRepository) : ViewModel() {
    private val _usuario = MutableStateFlow<Usuario?>(null)
    val usuario: StateFlow<Usuario?> = _usuario.asStateFlow()

    private val _cargando = MutableStateFlow(false)
    val cargando: StateFlow<Boolean> = _cargando.asStateFlow()

    private val _mensajeError = MutableStateFlow<String?>(null)
    val mensajeError: StateFlow<String?> = _mensajeError.asStateFlow()

    fun cargar(token: String, id: Int) {
        viewModelScope.launch {
            _cargando.value = true
            _mensajeError.value = null
            repository.obtenerUsuario(token, id)
                .onSuccess { _usuario.value = it }
                .onFailure { _mensajeError.value = it.message ?: "No se pudo cargar el usuario" }
            _cargando.value = false
        }
    }

    fun eliminar(token: String, id: Int, onExito: () -> Unit) {
        viewModelScope.launch {
            repository.eliminarUsuario(token, id)
                .onSuccess { onExito() }
                .onFailure { _mensajeError.value = it.message ?: "No se pudo eliminar el usuario" }
        }
    }
}