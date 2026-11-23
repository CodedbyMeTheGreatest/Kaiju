package com.kaiju.app.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import com.kaiju.app.model.RolUsuario
import com.kaiju.app.ui.components.TarjetaUsuario
import com.kaiju.app.viewmodel.ListaUsuariosViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaListaUsuarios(
    viewModel: ListaUsuariosViewModel,
    token: String,
    onNavegarAAgregar: () -> Unit,
    onNavegarADetalle: (Int) -> Unit
) {
    val usuarios by viewModel.usuariosFiltrados.collectAsState()
    val query by viewModel.queryBusqueda.collectAsState()
    val cargando by viewModel.cargando.collectAsState()
    val filtroActual by viewModel.filtroTipo.collectAsState()

    var expandirMenuFiltro by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.cargarUsuarios(token)
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onNavegarAAgregar() },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar Usuario")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = query,
                    onValueChange = { viewModel.alCambiarQueryBusqueda(it) },
                    label = { Text("Buscar por nombre") },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )

                Box {
                    IconButton(onClick = { expandirMenuFiltro = true }) {
                        Icon(
                            imageVector = Icons.Default.FilterList,
                            contentDescription = "Filtrar por rol",
                            tint = if (filtroActual != null) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                        )
                    }

                    DropdownMenu(
                        expanded = expandirMenuFiltro,
                        onDismissRequest = { expandirMenuFiltro = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Todos") },
                            onClick = {
                                viewModel.alCambiarFiltroTipo(null)
                                expandirMenuFiltro = false
                            }
                        )
                        RolUsuario.entries.forEach { rol ->
                            DropdownMenuItem(
                                text = { Text(rol.name.replace("_", " ")) },
                                onClick = {
                                    viewModel.alCambiarFiltroTipo(rol)
                                    expandirMenuFiltro = false
                                }
                            )
                        }
                    }
                }
            }

            if (cargando) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else if (usuarios.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No se encontraron usuarios.")
                }
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(usuarios) { usuario ->
                        TarjetaUsuario(
                            usuario = usuario,
                            onClick = { onNavegarADetalle(usuario.id) }
                        )
                    }
                }
            }
        }
    }
}