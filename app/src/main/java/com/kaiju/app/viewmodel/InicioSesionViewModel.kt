package com.kaiju.app.viewmodel

import androidx.lifecycle.ViewModel
import com.kaiju.app.model.Cuenta
import com.kaiju.app.model.ErroresCuenta
import com.kaiju.app.model.FormularioCuentaEstado
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class InicioSesionViewModel: ViewModel() {

    private val _estadoFormulario = MutableStateFlow(FormularioCuentaEstado())

    val estadoFormulario: StateFlow<FormularioCuentaEstado> = _estadoFormulario.asStateFlow()

    private val _cuentas = MutableStateFlow<List<Cuenta>>(emptyList())

    val cuentas: StateFlow<List<Cuenta>> = _cuentas.asStateFlow()

    fun actualizarUsuario(valor: String) = _estadoFormulario.update { it.copy(usuario = valor) }
    fun actualizarContrasenia(valor: String) = _estadoFormulario.update { it.copy(contrasenia = valor) }

    fun validarFormulario(): Boolean {
        val estado = _estadoFormulario.value

        val errorUsuario = if (estado.usuario.isBlank()) "El nombre de usuario es obligatorio" else null

        val errorContrasenia= if (estado.contrasenia.isBlank()) "La contraseña es obligatoria" else null

        _estadoFormulario.update {
            it.copy(errores = ErroresCuenta(errorUsuario, errorContrasenia))
        }

        return errorUsuario == null && errorContrasenia == null
    }

    fun InicioSesion(): Boolean {
        if (!validarFormulario()) return false

        val estado = _estadoFormulario.value
        val cuenta = Cuenta(
            usuario = estado.usuario.trim(),
            contrasenia = estado.contrasenia.trim()
        )

        _cuentas.update { it + cuenta }
        _estadoFormulario.value = FormularioCuentaEstado()
        return true
    }
}