package com.kaiju.app.viewmodel

import androidx.lifecycle.ViewModel
import com.kaiju.app.model.Usuario
import com.kaiju.app.repository.AlmacenSesion
import com.kaiju.app.repository.usuarioPorId
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SesionViewModel(private val almacen: AlmacenSesion): ViewModel() {

    private val _usuarioActivo = MutableStateFlow(almacen.leerIdUsuario()?.let { usuarioPorId(it) }
    )
    val usuarioActivo: StateFlow<Usuario?> = _usuarioActivo.asStateFlow()

    fun iniciar(usuario: Usuario) { _usuarioActivo.value = usuario
        almacen.guardar(usuario.id) }
    fun cerrarSesion() { _usuarioActivo.value = null
        almacen.borrar()}
}