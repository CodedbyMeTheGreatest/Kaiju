package com.kaiju.app.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kaiju.app.model.Producto

@Composable
fun TarjetaProducto(producto: Producto, onClick: () -> Unit){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable { onClick() }
    ){
        Column(modifier = Modifier.padding(12.dp)) {
            Text(text = producto.nombre, fontWeight = FontWeight.Bold)
            Text(text = producto.descripcion)
            Text(text = "Precio: $${producto.precio}")
            Text(text = "Stock: ${producto.stockActual}")
        }
    }
}