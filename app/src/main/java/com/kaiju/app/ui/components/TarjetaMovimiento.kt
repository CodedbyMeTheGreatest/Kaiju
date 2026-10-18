package com.kaiju.app.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kaiju.app.model.Movimiento
import com.kaiju.app.model.TipoMovimiento
import java.time.format.DateTimeFormatter

private val formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")

@Composable
fun TarjetaMovimiento(movimiento: Movimiento, modifier: Modifier = Modifier) {
    val esEntrada = movimiento.tipoMovimiento == TipoMovimiento.ENTRADA ||
            (movimiento.tipoMovimiento == TipoMovimiento.AJUSTE && movimiento.cantidad > 0)

    val icono = when (movimiento.tipoMovimiento) {
        TipoMovimiento.ENTRADA -> Icons.Default.Add
        TipoMovimiento.SALIDA_VENTA -> Icons.Default.ShoppingCart
        TipoMovimiento.SALIDA_CONSUMO -> Icons.Default.Build
        TipoMovimiento.AJUSTE -> Icons.Default.Edit
    }
    val titulo = when (movimiento.tipoMovimiento) {
        TipoMovimiento.ENTRADA -> "Entrada"
        TipoMovimiento.SALIDA_VENTA -> "Salida por venta"
        TipoMovimiento.SALIDA_CONSUMO -> "Salida por consumo"
        TipoMovimiento.AJUSTE -> "Ajuste"
    }
    val color = if (esEntrada) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
    val cantidadTexto = if (esEntrada) "+${movimiento.cantidad}" else "-${kotlin.math.abs(movimiento.cantidad)}"

    Card(modifier = modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp)) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icono, contentDescription = null, tint = color)
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(titulo, style = MaterialTheme.typography.titleMedium)
                if (movimiento.motivo.isNotBlank()) {
                    Text(movimiento.motivo, style = MaterialTheme.typography.bodyMedium)
                }
                Text(
                    "${movimiento.fecha.format(formatoFecha)} · ${movimiento.usuario}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Text(cantidadTexto, style = MaterialTheme.typography.titleLarge, color = color)
        }
    }
}