package com.kaiju.app.model

data class FormularioUsuarioEstado(
    val id: Int = 0,
    val run: String = "",
    val nombre: String = "",
    val apellidos: String = "",
    val email: String = "",
    val password: String = "",
    val tipo: RolUsuario? = null,
    val fechaNacimiento: String = "",
    val activo: Boolean = true,
    val errores: ErroresUsuario = ErroresUsuario()
) {
    val esEdicion: Boolean
        get() = id != 0
}

data class ErroresUsuario(
    val errorRun: String? = null,
    val errorNombre: String? = null,
    val errorApellidos: String? = null,
    val errorEmail: String? = null,
    val errorPassword: String? = null,
    val errorTipo: String? = null,
    val errorFechaNacimiento: String? = null
) {
    val hayErrores: Boolean
        get() = errorRun != null || errorNombre != null || errorApellidos != null ||
                errorEmail != null || errorPassword != null || errorTipo != null ||
                errorFechaNacimiento != null
}