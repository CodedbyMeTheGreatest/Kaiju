package com.kaiju.app.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun TarjetaProducto(nombre:String, descripcion:String, precio:Int, stock:Int){
    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)){
        Column(modifier = Modifier.padding(12.dp)) {
            Text(text = nombre, fontWeight = FontWeight.Bold)
            Text(text = descripcion)
            Text(text = precio.toString())
            Text(text = stock.toString())
        }
    }
}