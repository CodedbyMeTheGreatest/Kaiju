package com.kaiju.app.viewmodel

import androidx.lifecycle.ViewModel
import com.kaiju.app.model.Usuario
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SesionViewModel: ViewModel() {
    private val _usuarioActivo = MutableStateFlow<Usuario?>(null)
    val usuarioActivo: StateFlow<Usuario?> = _usuarioActivo.asStateFlow()

    fun iniciar(usuario: Usuario) { _usuarioActivo.value = usuario }
    fun cerrarSesion() { _usuarioActivo.value = null }
}