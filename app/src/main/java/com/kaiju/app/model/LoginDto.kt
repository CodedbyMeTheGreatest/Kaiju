package com.kaiju.app.model

data class LoginRequest(
    val email: String,
    val password: String
)

data class LoginResponse(
    val mensaje: String,
    val token: String,
    val usuario: UsuarioResumen
)

data class UsuarioResumen(
    val id: Int,
    val nombre: String,
    val tipo: RolUsuario
)