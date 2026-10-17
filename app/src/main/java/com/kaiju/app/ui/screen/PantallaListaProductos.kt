package com.kaiju.app.ui.screen

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.kaiju.app.ui.components.ListaProductos
import com.kaiju.app.viewmodel.ListaProductosViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaListaProductos(
    viewModel: ListaProductosViewModel,
    onAgregarProducto: () -> Unit ){

    val listaProductos by viewModel.productos.collectAsState()

    Scaffold(topBar = {
        TopAppBar(title = {
            Text("Lista de Productos")
        })
    },
        floatingActionButton = {
            FloatingActionButton(onClick = onAgregarProducto) {
                Icon(Icons.Default.Add,contentDescription="Agregar producto")
            }
        }
    )
    { padding -> ListaProductos(productos = listaProductos, modifier = Modifier.padding(padding)) }
}