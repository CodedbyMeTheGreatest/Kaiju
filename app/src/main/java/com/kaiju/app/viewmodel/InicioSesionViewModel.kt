package com.kaiju.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kaiju.app.model.ErroresCuenta
import com.kaiju.app.model.FormularioCuentaEstado
import com.kaiju.app.model.LoginRequest
import com.kaiju.app.model.UsuarioResumen
import com.kaiju.app.repository.UsuarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class InicioSesionViewModel(
    private val repository: UsuarioRepository
): ViewModel()  {

    private val _estadoFormulario = MutableStateFlow(FormularioCuentaEstado())

    val estadoFormulario: StateFlow<FormularioCuentaEstado> = _estadoFormulario.asStateFlow()

    private val _cargando = MutableStateFlow(false)
    val cargando: StateFlow<Boolean> = _cargando.asStateFlow()

    fun actualizarEmail(valor: String) = _estadoFormulario.update { it.copy(email = valor) }
    fun actualizarContrasenia(valor: String) = _estadoFormulario.update { it.copy(contrasenia = valor) }

    fun validarFormulario(): Boolean {
        val estado = _estadoFormulario.value

        val errorEmail = if (estado.email.isBlank()) "El correo es obligatorio" else null

        val errorContrasenia= if (estado.contrasenia.isBlank()) "La contraseña es obligatoria" else null

        _estadoFormulario.update {
            it.copy(errores = ErroresCuenta(errorEmail, errorContrasenia))
        }

        return errorEmail == null && errorContrasenia == null
    }

    fun InicioSesion(onSuccess: (String, UsuarioResumen) -> Unit) {
        if (!validarFormulario()) return

        _cargando.value = true
        val estado = _estadoFormulario.value

        viewModelScope.launch {
            val request = LoginRequest(email = estado.email, password = estado.contrasenia)
            val resultado = repository.login(request)

            resultado.onSuccess { response ->
                _cargando.value = false
                _estadoFormulario.value = FormularioCuentaEstado()
                onSuccess(response.token, response.usuario)
            }.onFailure { error ->
                _cargando.value = false
                _estadoFormulario.update {
                    it.copy(errores = ErroresCuenta(
                        errorEmail = null,
                        errorContrasenia = error.message ?: "Credenciales incorrectas o Error en el servidor"
                    ))
                }

            }
        }
    }
}