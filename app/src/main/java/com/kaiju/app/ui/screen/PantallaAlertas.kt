package com.kaiju.app.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kaiju.app.ui.components.TarjetaAlerta
import com.kaiju.app.viewmodel.AlertasViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaAlertas(viewModel: AlertasViewModel) {
    val productos by viewModel.productosEnAlerta.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Alertas de stock") }) }
    ) { padding ->
        if (productos.isEmpty()) {
            // Estado vacío: se explica en vez de dejar la pantalla en blanco
            Column(
                modifier = Modifier.padding(padding).fillMaxSize().padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(Icons.Default.CheckCircle, contentDescription = null)
                Spacer(Modifier.height(8.dp))
                Text("No hay productos con stock bajo", style = MaterialTheme.typography.titleMedium)
            }
        } else {
            LazyColumn(modifier = Modifier.padding(padding).fillMaxSize()) {
                items(productos, key = { it.codigo }) { producto ->
                    TarjetaAlerta(producto)
                }
            }
        }
    }
}