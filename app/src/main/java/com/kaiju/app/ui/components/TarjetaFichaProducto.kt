package com.kaiju.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults

import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.kaiju.app.model.Producto
import com.kaiju.app.remote.PokemonDto

@Composable
fun TarjetaFichaProducto(
    producto: Producto,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(text = producto.nombre, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Text(text = "Código: ${producto.codigo}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Categoría: ${producto.categoria.name}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Tipo: ${producto.tipo.name}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Precio: $${producto.precio}", style = MaterialTheme.typography.bodyMedium)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Stock Actual: ${producto.stockActual}", fontWeight = FontWeight.SemiBold)
                Text(text = "Stock Mínimo: ${producto.stockMinimo}", color = MaterialTheme.colorScheme.error)
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
            Text(text = "Descripción: ${producto.descripcion}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Detalle: ${producto.detalle}", style = MaterialTheme.typography.bodySmall)
        }
    }
}