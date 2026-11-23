package com.kaiju.app.viewmodel

import androidx.lifecycle.ViewModel
import com.kaiju.app.model.Usuario
import com.kaiju.app.model.UsuarioResumen
import com.kaiju.app.repository.AlmacenSesion
import com.kaiju.app.repository.usuarioPorId
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SesionViewModel(private val almacen: AlmacenSesion): ViewModel() {

    private val _usuarioActivo = MutableStateFlow<UsuarioResumen?>(almacen.leerUsuario())
    val usuarioActivo: StateFlow<UsuarioResumen?> = _usuarioActivo.asStateFlow()

    private val _token = MutableStateFlow<String?>(almacen.leerToken())
    val token: StateFlow<String?> = _token.asStateFlow()

    fun iniciar(tokenJwt: String, usuario: UsuarioResumen) {
        _usuarioActivo.value = usuario
        _token.value = tokenJwt
        almacen.guardarSesion(tokenJwt, usuario)
    }

    fun cerrarSesion() {
        _usuarioActivo.value = null
        _token.value = null
        almacen.borrar()
    }
}