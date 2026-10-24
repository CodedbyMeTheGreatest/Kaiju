package com.kaiju.app.viewmodel

import androidx.lifecycle.ViewModel
import com.kaiju.app.model.Cuenta
import com.kaiju.app.model.ErroresCuenta
import com.kaiju.app.model.FormularioCuentaEstado
import com.kaiju.app.model.Usuario
import com.kaiju.app.repository.autenticar
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class InicioSesionViewModel: ViewModel() {

    private val _estadoFormulario = MutableStateFlow(FormularioCuentaEstado())

    val estadoFormulario: StateFlow<FormularioCuentaEstado> = _estadoFormulario.asStateFlow()
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

    fun InicioSesion(): Usuario? {
        if (!validarFormulario()) return null

        val estado = _estadoFormulario.value
        val usuario = autenticar(estado.usuario, estado.contrasenia)

        if(usuario == null) {
            _estadoFormulario.update {
                it.copy(errores = ErroresCuenta(
                    errorUsuario = null,
                    errorContrasenia = "Usuario o contraseña incorrectos"
                ))
            }
            return null
        }

        _estadoFormulario.value = FormularioCuentaEstado()
        return usuario
    }
}