package com.kaiju.app.model

import com.google.gson.annotations.SerializedName
import java.util.Date

data class Usuario (
    val id: Int = 0,
    val run: String,
    val nombre: String,
    val apellidos: String,
    val email: String,
    val password: String? = null,
    val tipo: RolUsuario,
    @SerializedName(value = "fechaNacimiento",alternate = ["fecha_nacimiento"])
    val fechaNacimiento: String? = null,
    val activo: Boolean = true
)