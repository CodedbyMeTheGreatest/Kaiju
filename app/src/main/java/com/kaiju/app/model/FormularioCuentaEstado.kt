package com.kaiju.app.model

data class FormularioCuentaEstado(
    val usuario: String = "",
    val contrasenia: String = "",
    val errores: ErroresCuenta = ErroresCuenta()
)


data class ErroresCuenta(
    val errorUsuario: String? = null,
    val errorContrasenia: String? = null
)