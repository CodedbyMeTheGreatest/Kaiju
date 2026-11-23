package com.kaiju.app.model

data class FormularioCuentaEstado(
    val email: String = "",
    val contrasenia: String = "",
    val errores: ErroresCuenta = ErroresCuenta()
)


data class ErroresCuenta(
    val errorEmail: String? = null,
    val errorContrasenia: String? = null
)