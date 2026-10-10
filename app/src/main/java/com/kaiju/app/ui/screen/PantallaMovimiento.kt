package com.kaiju.app.ui.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.kaiju.app.model.Producto
import com.kaiju.app.model.TipoMovimiento
import com.kaiju.app.viewmodel.FormularioMovimientoViewModel
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PantallaMovimiento(
    viewModel: FormularioMovimientoViewModel,
    nombreUsuario: String,
    tiposPermitidos: List<TipoMovimiento> = TipoMovimiento.values().toList(),
    modifier: Modifier = Modifier
) {
    val estado by viewModel.estado.collectAsState()
    val productos by viewModel.productos.collectAsState()
    val errores = estado.errores

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val productoSeleccionado = productos.find { it.codigo == estado.codigoProducto }

    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Registrar movimiento", style = MaterialTheme.typography.headlineSmall)

            CampoSelector(
                etiqueta = "Producto",
                valorMostrado = productoSeleccionado?.let { "${it.codigo} · ${it.nombre}" } ?: "",
                opciones = productos,
                textoOpcion = { "${it.codigo} · ${it.nombre}" },
                onSeleccion = { viewModel.actualizarProducto(it.codigo) },
                icono = Icons.Default.ShoppingCart,
                error = errores.errorCodigoProducto
            )
            productoSeleccionado?.let {
                Text(
                    "Disponible: ${it.stockActual} unidades",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            CampoSelector(
                etiqueta = "Tipo de movimiento",
                valorMostrado = estado.tipo?.etiqueta() ?: "",
                opciones = tiposPermitidos,
                textoOpcion = { it.etiqueta() },
                onSeleccion = { viewModel.actualizarTipo(it) },
                icono = Icons.Default.List,
                error = errores.errorTipoMovimiento
            )

            OutlinedTextField(
                value = estado.cantidad,
                onValueChange = viewModel::actualizarCantidad,
                label = { Text("Cantidad") },
                leadingIcon = { Icon(Icons.Default.Add, contentDescription = null) },
                trailingIcon = {
                    if (errores.errorCantidad != null)
                        Icon(Icons.Default.Warning, co  ntentDescription = "Error")
                },
                isError = errores.errorCantidad != null,
                supportingText = { errores.errorCantidad?.let { Text(it) } },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = estado.motivo,
                onValueChange = viewModel::actualizarMotivo,
                label = { Text("Motivo (obligatorio en ajustes)") },
                leadingIcon = { Icon(Icons.Default.Edit, contentDescription = null) },
                trailingIcon = {
                    if (errores.errorMotivo != null)
                        Icon(Icons.Default.Warning, contentDescription = "Error")
                },
                isError = errores.errorMotivo != null,
                supportingText = { errores.errorMotivo?.let { Text(it) } },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(8.dp))

            Button(
                onClick = {
                    val guardado = viewModel.guardarMovimiento(nombreUsuario)
                    if (guardado) {
                        scope.launch { snackbarHostState.showSnackbar("Movimiento registrado") }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Check, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Registrar movimiento")
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun <T> CampoSelector(
    etiqueta: String,
    valorMostrado: String,
    opciones: List<T>,
    textoOpcion: (T) -> String,
    onSeleccion: (T) -> Unit,
    icono: ImageVector,
    error: String?
) {
    var expandido by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expandido,
        onExpandedChange = { expandido = it }
    ) {
        OutlinedTextField(
            value = valorMostrado,
            onValueChange = {},
            readOnly = true,
            label = { Text(etiqueta) },
            leadingIcon = { Icon(icono, contentDescription = null) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandido) },
            isError = error != null,
            supportingText = { error?.let { Text(it) } },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = expandido,
            onDismissRequest = { expandido = false }
        ) {
            opciones.forEach { opcion ->
                DropdownMenuItem(
                    text = { Text(textoOpcion(opcion)) },
                    onClick = {
                        onSeleccion(opcion)
                        expandido = false
                    }
                )
            }
        }
    }
}

private fun TipoMovimiento.etiqueta(): String = when (this) {
    TipoMovimiento.ENTRADA -> "Entrada de producto"
    TipoMovimiento.SALIDA_VENTA -> "Salida por venta"
    TipoMovimiento.SALIDA_CONSUMO -> "Salida por consumo"
    TipoMovimiento.AJUSTE -> "Ajuste de inventario"
}