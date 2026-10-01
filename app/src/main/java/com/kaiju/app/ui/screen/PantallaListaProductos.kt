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
import androidx.compose.ui.Modifier
import com.kaiju.app.model.Producto
import com.kaiju.app.ui.components.ListaProductos

private val listaDeEjemplo = listOf(
    Producto(id = "P1", nombre = "Pantalon", descripcion = "Pantalon rojo con rayas azules", precio = 9000, stock = 3),
    Producto(id = "C1", nombre = "Camisa", descripcion = "Una camisa negra", precio = 100000, stock = 1),
    Producto(id = "Z1", nombre = "Zapato", descripcion = "Edicion Limitada", precio = 1000000, stock = 1)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaListaProductos(){
    Scaffold(topBar = {
        TopAppBar(title = {
            Text("Lista de Productos")
        })
    },
        floatingActionButton = {
            FloatingActionButton(onClick = {/*Imaginate que hace algo*/}) {
                Icon(Icons.Default.Add,contentDescription="Agregar producto")
            }
        }
    )
    { padding -> ListaProductos(productos = listaDeEjemplo, modifier = Modifier.padding(padding)) }
}