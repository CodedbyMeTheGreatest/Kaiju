package com.kaiju.app.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kaiju.app.viewmodel.InicioSesionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaInicioSesionEvento(viewModel: InicioSesionViewModel){
    val estado by viewModel.estadoFormulario.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Inicio Sesión") }) }
    ) {
            padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = estado.usuario,
                onValueChange = viewModel::actualizarUsuario,
                label = { Text("Usuario") },
                isError = estado.errores.errorUsuario != null,
                supportingText = { estado.errores.errorUsuario?.let { Text(it) } },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = estado.contrasenia,
                onValueChange = viewModel::actualizarContrasenia,
                label = { Text("Contraseña") },
                isError = estado.errores.errorContrasenia != null,
                supportingText = { estado.errores.errorContrasenia?.let { Text(it) } },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Button(
                onClick = { viewModel.InicioSesion() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Iniciar Sesión")
            }

        }
    }
}