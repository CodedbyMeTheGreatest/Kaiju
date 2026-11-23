package com.kaiju.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kaiju.app.model.ErroresUsuario
import com.kaiju.app.model.EstadoBoton
import com.kaiju.app.model.FormularioUsuarioEstado
import com.kaiju.app.model.RolUsuario
import com.kaiju.app.model.Usuario
import com.kaiju.app.repository.UsuarioRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

class FormularioUsuarioViewModel(
    private val repository: UsuarioRepository
) : ViewModel() {

    private val _estadoFormulario = MutableStateFlow(FormularioUsuarioEstado())
    val estadoFormulario: StateFlow<FormularioUsuarioEstado> = _estadoFormulario.asStateFlow()

    private val _estadoBoton = MutableStateFlow(EstadoBoton.INACTIVO)
    val estadoBoton: StateFlow<EstadoBoton> = _estadoBoton.asStateFlow()

    private val _mensajeError = MutableStateFlow<String?>(null)
    val mensajeError: StateFlow<String?> = _mensajeError.asStateFlow()

    fun actualizarRun(valor: String) = _estadoFormulario.update { it.copy(run = valor) }
    fun actualizarNombre(valor: String) = _estadoFormulario.update { it.copy(nombre = valor) }
    fun actualizarApellidos(valor: String) = _estadoFormulario.update { it.copy(apellidos = valor) }
    fun actualizarEmail(valor: String) = _estadoFormulario.update { it.copy(email = valor) }
    fun actualizarPassword(valor: String) = _estadoFormulario.update { it.copy(password = valor) }
    fun actualizarTipo(valor: RolUsuario) = _estadoFormulario.update { it.copy(tipo = valor) }
    fun actualizarFechaNacimiento(valor: String) = _estadoFormulario.update { it.copy(fechaNacimiento = valor) }
    fun actualizarActivo(valor: Boolean) = _estadoFormulario.update { it.copy(activo = valor) }

    fun limpiarError() {
        _mensajeError.value = null
    }

    fun cargarUsuario(token: String, id: Int) {
        viewModelScope.launch {
            repository.obtenerUsuario(token, id).onSuccess { usuario ->
                _estadoFormulario.value = FormularioUsuarioEstado(
                    id = usuario.id,
                    run = usuario.run,
                    nombre = usuario.nombre,
                    apellidos = usuario.apellidos,
                    email = usuario.email,
                    password = "",
                    tipo = usuario.tipo,
                    fechaNacimiento = usuario.fechaNacimiento,
                    activo = usuario.activo
                )
            }.onFailure { error ->
                _mensajeError.value = error.message ?: "No se pudo cargar el usuario"
            }
        }
    }

    fun validarFormulario(): Boolean {
        val estado = _estadoFormulario.value

        val errorRun = if (estado.run.isBlank()) "El RUN es obligatorio" else null
        val errorNombre = if (estado.nombre.isBlank()) "El nombre es obligatorio" else null
        val errorApellidos = if (estado.apellidos.isBlank()) "Los apellidos son obligatorios" else null

        val errorEmail = when {
            estado.email.isBlank() -> "El correo es obligatorio"
            !estado.email.contains("@") -> "Formato de correo inválido"
            else -> null
        }

        val errorPassword = when {
            !estado.esEdicion && estado.password.isBlank() -> "La contraseña es obligatoria"
            estado.password.isNotBlank() && estado.password.length > 64 -> "Máximo 64 caracteres"
            else -> null
        }

        val errorTipo = if (estado.tipo == null) "El rol es obligatorio" else null
        val errorFecha = if (estado.fechaNacimiento.isBlank()) "La fecha de nacimiento es obligatoria" else null

        val nuevosErrores = ErroresUsuario(
            errorRun = errorRun,
            errorNombre = errorNombre,
            errorApellidos = errorApellidos,
            errorEmail = errorEmail,
            errorPassword = errorPassword,
            errorTipo = errorTipo,
            errorFechaNacimiento = errorFecha
        )

        _estadoFormulario.update { it.copy(errores = nuevosErrores) }
        return !nuevosErrores.hayErrores
    }

    fun guardarUsuario(token: String, onExito: () -> Unit) {
        if (!validarFormulario()) return

        val estado = _estadoFormulario.value
        val usuario = Usuario(
            id = estado.id,
            run = estado.run.trim(),
            nombre = estado.nombre.trim(),
            apellidos = estado.apellidos.trim(),
            email = estado.email.trim(),
            password = estado.password.ifBlank { null },
            tipo = estado.tipo!!,
            fechaNacimiento = estado.fechaNacimiento.trim(),
            activo = estado.activo
        )

        viewModelScope.launch {
            _estadoBoton.value = EstadoBoton.CARGANDO
            _mensajeError.value = null

            val resultado = if (estado.esEdicion) {
                repository.actualizarUsuario(token, estado.id, usuario)
            } else {
                repository.crearUsuario(usuario)
            }

            resultado.onSuccess {
                _estadoBoton.value = EstadoBoton.EXITOSO
                delay(1000.milliseconds)
                _estadoBoton.value = EstadoBoton.INACTIVO
                _estadoFormulario.value = FormularioUsuarioEstado()
                onExito()
            }.onFailure { error ->
                _estadoBoton.value = EstadoBoton.INACTIVO
                _mensajeError.value = error.message ?: "Error al guardar el usuario"
            }
        }
    }
}