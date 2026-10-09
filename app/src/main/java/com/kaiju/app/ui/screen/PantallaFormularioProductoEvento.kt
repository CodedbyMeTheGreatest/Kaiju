package com.kaiju.app.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kaiju.app.viewmodel.FormularioProductoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaFormularioProductoEvento(viewModel: FormularioProductoViewModel){
    val estado by viewModel.estadoFormulario.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Agregar Producto") }) }
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
                value = estado.codigo,
                onValueChange = viewModel::actualizarCodigo,
                label = { Text("Código") },
                isError = estado.errores.errorCodigo != null,
                supportingText = { estado.errores.errorCodigo?.let { Text(it) } },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = estado.nombre,
                onValueChange = viewModel::actualizarNombre,
                label = { Text("Nombre") },
                isError = estado.errores.errorNombre != null,
                supportingText = { estado.errores.errorNombre?.let { Text(it) } },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = estado.descripcion,
                onValueChange = viewModel::actualizarDescripcion,
                label = { Text("Descripción") },
                isError = estado.errores.errorDescripcion != null,
                supportingText = { estado.errores.errorDescripcion?.let { Text(it) } },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = estado.categoria,
                onValueChange = viewModel::actualizarCategoria,
                label = { Text("Categoría") },
                isError = estado.errores.errorCategoria != null,
                supportingText = { estado.errores.errorCategoria?.let { Text(it) } },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = estado.tipo,
                onValueChange = viewModel::actualizarTipo,
                label = { Text("Tipo") },
                isError = estado.errores.errorTipo != null,
                supportingText = { estado.errores.errorTipo?.let { Text(it) } },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            //Precio
            //StockActual
            //StockMinimo
            //Detalle
        }
    }
}