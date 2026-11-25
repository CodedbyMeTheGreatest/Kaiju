package com.kaiju.app.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kaiju.app.ui.components.TarjetaDetalleUsuario
import com.kaiju.app.viewmodel.DetalleUsuarioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaDetalleUsuario(
    viewModel: DetalleUsuarioViewModel,
    token: String,
    idUsuario: Int,
    onEditar: (Int) -> Unit,
    onVolver: () -> Unit
) {
    val usuario by viewModel.usuario.collectAsState()
    val cargando by viewModel.cargando.collectAsState()
    val error by viewModel.mensajeError.collectAsState()
    var confirmarEliminar by remember { mutableStateOf(false) }
    val u = usuario

    LaunchedEffect(idUsuario) { viewModel.cargar(token, idUsuario) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle de usuario") },
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
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (cargando) CircularProgressIndicator(modifier = Modifier.padding(32.dp))
            if (u != null) {
                TarjetaDetalleUsuario(
                    usuario = u,
                    onEditar = { onEditar(idUsuario) },
                    onEliminar = { confirmarEliminar = true }
                )
            }
            error?.let {
                Text(it, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(16.dp))
            }
        }
    }

    if (confirmarEliminar && u != null) {
        AlertDialog(
            onDismissRequest = { confirmarEliminar = false },
            title = { Text("Eliminar usuario") },
            text = { Text("¿Eliminar a ${u.nombre} ${u.apellidos}? Ya no podrá iniciar sesión.") },
            confirmButton = {
                TextButton(onClick = {
                    confirmarEliminar = false
                    viewModel.eliminar(token, idUsuario, onExito = onVolver)
                }) { Text("Eliminar") }
            },
            dismissButton = {
                TextButton(onClick = { confirmarEliminar = false }) { Text("Cancelar") }
            }
        )
    }
}