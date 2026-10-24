package com.kaiju.app.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kaiju.app.ui.components.ListaProductos
import com.kaiju.app.viewmodel.ListaProductosViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaListaProductos(
    viewModel: ListaProductosViewModel,
    onAgregarProducto: (()-> Unit)? = null,
    onProductoClick: (String) -> Unit
) {
    val queryBusqueda by viewModel.queryBusqueda.collectAsStateWithLifecycle()
    val productos by viewModel.products.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text("Lista de Productos")
            })
        },
        floatingActionButton = {
            if (onAgregarProducto != null) {
                FloatingActionButton(onClick = onAgregarProducto) {
                    Icon(Icons.Default.Add, contentDescription = "Agregar producto")
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            OutlinedTextField(
                value = queryBusqueda,
                onValueChange = viewModel::alCambiarQueryBusqueda,
                label = { Text("Buscar en el catálogo...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                trailingIcon = {
                    if (queryBusqueda.isNotEmpty()) {
                        IconButton(onClick = { viewModel.alCambiarQueryBusqueda("") }) {
                            Icon(Icons.Default.Close, contentDescription = "Limpiar")
                        }
                    }
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            ListaProductos(
                productos = productos,
                onProductoClick = onProductoClick,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}