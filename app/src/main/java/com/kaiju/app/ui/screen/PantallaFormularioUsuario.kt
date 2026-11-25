package com.kaiju.app.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenu
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.kaiju.app.model.EstadoBoton
import com.kaiju.app.model.RolUsuario
import com.kaiju.app.ui.components.BotonGuardarAnimado
import com.kaiju.app.viewmodel.FormularioUsuarioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaFormularioUsuario(
    viewModel: FormularioUsuarioViewModel,
    token: String,
    idUsuario: Int,          // 0 = usuario nuevo
    onVolver: () -> Unit
) {
    val estado by viewModel.estadoFormulario.collectAsState()
    val estadoBoton by viewModel.estadoBoton.collectAsState()
    val mensajeError by viewModel.mensajeError.collectAsState()
    var menuRol by remember { mutableStateOf(false) }
    val haptic = LocalHapticFeedback.current

    LaunchedEffect(idUsuario) {
        if (idUsuario != 0) viewModel.cargarUsuario(token, idUsuario)
    }
    LaunchedEffect(estadoBoton) {
        if (estadoBoton == EstadoBoton.EXITOSO) haptic.performHapticFeedback(HapticFeedbackType.Confirm)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (estado.esEdicion) "Editar usuario" else "Agregar usuario") },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CampoUsuario(estado.run, viewModel::actualizarRun, "RUN (sin puntos ni guion)",
                estado.errores.errorRun, enabled = !estado.esEdicion)
            CampoUsuario(estado.nombre, viewModel::actualizarNombre, "Nombre", estado.errores.errorNombre)
            CampoUsuario(estado.apellidos, viewModel::actualizarApellidos, "Apellidos", estado.errores.errorApellidos)
            CampoUsuario(estado.email, viewModel::actualizarEmail, "Correo",
                estado.errores.errorEmail, teclado = KeyboardType.Email)
            if (!estado.esEdicion) {
                CampoUsuario(estado.password, viewModel::actualizarPassword, "Contraseña",
                    estado.errores.errorPassword, visualTransformation = PasswordVisualTransformation()
                )
            }

            ExposedDropdownMenuBox(expanded = menuRol, onExpandedChange = { menuRol = !menuRol }) {
                OutlinedTextField(
                    value = estado.tipo?.name?.replace("_", " ") ?: "",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Rol") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = menuRol) },
                    isError = estado.errores.errorTipo != null,
                    supportingText = { estado.errores.errorTipo?.let { Text(it) } },
                    singleLine = true,
                    modifier = Modifier
                        .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable, true)
                        .fillMaxWidth()
                )
                ExposedDropdownMenu(expanded = menuRol, onDismissRequest = { menuRol = false }) {
                    RolUsuario.entries.forEach { rol ->
                        DropdownMenuItem(
                            text = { Text(rol.name.replace("_", " ")) },
                            onClick = {
                                viewModel.actualizarTipo(rol)
                                menuRol = false
                            }
                        )
                    }
                }
            }

            CampoUsuario(estado.fechaNacimiento, viewModel::actualizarFechaNacimiento,
                "Fecha de nacimiento (AAAA-MM-DD)", estado.errores.errorFechaNacimiento)

            mensajeError?.let {
                Text(it, color = MaterialTheme.colorScheme.error)
            }

            BotonGuardarAnimado(
                estado = estadoBoton,
                onClick = {
                    if (viewModel.validarFormulario()) viewModel.guardarUsuario(token, onVolver)
                    else haptic.performHapticFeedback(HapticFeedbackType.Reject)
                },
                txtEjemplar = "Usuario"
            )
        }
    }
}

@Composable
private fun CampoUsuario(
    valor: String,
    onCambio: (String) -> Unit,
    etiqueta: String,
    error: String?,
    enabled: Boolean = true,
    teclado: KeyboardType = KeyboardType.Text,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    OutlinedTextField(
        value = valor,
        onValueChange = onCambio,
        label = { Text(etiqueta) },
        isError = error != null,
        supportingText = { error?.let { Text(it) } },
        enabled = enabled,
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = teclado),
        visualTransformation = visualTransformation,
        modifier = Modifier.fillMaxWidth()
    )
}