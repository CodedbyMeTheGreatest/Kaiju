package com.kaiju.app.model

import java.util.Date

data class Usuario (
    val id: Int = 0,
    val run: String,
    val nombre: String,
    val apellidos: String,
    val email: String,
    val password: String? = null,
    val tipo: RolUsuario,
    val fechaNacimiento: String,
    val activo: Boolean = true
)