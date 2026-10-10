package com.kaiju.app.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.kaiju.app.model.CategoriaProducto
import com.kaiju.app.model.TipoProducto
import com.kaiju.app.viewmodel.FormularioProductoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaFormularioProductoEvento(viewModel: FormularioProductoViewModel){
    val estado by viewModel.estadoFormulario.collectAsState()
    var menuCategoria by remember { mutableStateOf(false) }
    var menuTipo by remember { mutableStateOf(false) }


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
            ExposedDropdownMenuBox(
                expanded = menuCategoria,
                onExpandedChange = { menuCategoria = !menuCategoria }
            ) {
                OutlinedTextField(
                    value = estado.categoria.name,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Categoría") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = menuCategoria) },
                    isError = estado.errores.errorCategoria != null,
                    supportingText = { estado.errores.errorCategoria?.let { Text(it) } },
                    singleLine = true,
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = menuCategoria,
                    onDismissRequest = { menuCategoria = false }
                ) {
                    CategoriaProducto.entries.forEach { op ->
                        DropdownMenuItem(
                            text = { Text(op.name) },
                            onClick = {
                                viewModel.actualizarCategoria(op)
                                menuCategoria = false
                            }
                        )
                    }
                }
            }

            ExposedDropdownMenuBox(
                expanded = menuTipo,
                onExpandedChange = { menuTipo = !menuTipo }
            ) {
                OutlinedTextField(
                    value = estado.tipo.name, // Asegúrate de que esto sea un String (ej. estado.tipo.name)
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Tipo") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = menuTipo) },
                    isError = estado.errores.errorTipoProducto != null,
                    supportingText = { estado.errores.errorTipoProducto?.let { Text(it) } },
                    singleLine = true,
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = menuTipo,
                    onDismissRequest = { menuTipo = false }
                ) {
                    TipoProducto.entries.forEach { op ->
                        DropdownMenuItem(
                            text = { Text(op.name) },
                            onClick = {
                                viewModel.actualizarTipo(op)
                                menuTipo = false
                            }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = estado.precio.toString(),
                onValueChange = {valor -> viewModel.actualizarPrecio(valor.toInt())},
                label = { Text("Precio") },
                isError = estado.errores.errorPrecio != null,
                supportingText = { estado.errores.errorPrecio?.let { Text(it) } },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = estado.stockActual.toString(),
                onValueChange = {valor -> viewModel.actualizarStockActual(valor.toInt())},
                label = { Text("Stock Actual") },
                isError = estado.errores.errorStockActual != null,
                supportingText = { estado.errores.errorStockActual?.let { Text(it) } },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = estado.stockMinimo.toString(),
                onValueChange = {valor -> viewModel.actualizarStockMinimo(valor.toInt())},
                label = { Text("Stock Mínimo") },
                isError = estado.errores.errorStockMinimo != null,
                supportingText = { estado.errores.errorStockMinimo?.let { Text(it) } },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = estado.detalle,
                onValueChange = viewModel::actualizarDetalle,
                label = { Text("Detalle") },
                isError = estado.errores.errorDetalle != null,
                supportingText = { estado.errores.errorDetalle?.let { Text(it) } },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Button(
                onClick = {viewModel.guardarProducto()},
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar Producto")
            }
        }
    }
}