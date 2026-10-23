package com.kaiju.app.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kaiju.app.model.Producto

@Composable
fun TarjetaAlerta(producto: Producto, modifier: Modifier = Modifier) {
    val agotado = producto.stockActual == 0
    val color = if (agotado) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.tertiary
    val estado = if (agotado) "Agotado" else "Stock bajo"

    Card(modifier = modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp)) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Warning, contentDescription = estado, tint = color)
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(producto.nombre, style = MaterialTheme.typography.titleMedium)
                Text("Código: ${producto.codigo}", style = MaterialTheme.typography.bodySmall)
                Text(
                    "Stock: ${producto.stockActual} · Mínimo: ${producto.stockMinimo}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Text(estado, style = MaterialTheme.typography.labelLarge, color = color)
        }
    }
}