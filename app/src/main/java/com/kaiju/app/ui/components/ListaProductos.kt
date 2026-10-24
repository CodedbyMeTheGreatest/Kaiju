package com.kaiju.app.ui.components


import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.kaiju.app.R
import com.kaiju.app.model.Producto
import com.kaiju.app.model.TipoProducto

fun TipoProducto.obtenerImagenRes(): Int {
    return when (this) {
        TipoProducto.POLERA -> R.drawable.polera_negra
        TipoProducto.POLERON -> R.drawable.poleron_negro
        TipoProducto.CHAQUETA -> R.drawable.chaqueta_beige
        TipoProducto.PANTALON -> R.drawable.pantalon_gris
        TipoProducto.CALZADO -> R.drawable.calzado_negro
        TipoProducto.ACCESORIO -> R.drawable.accesorio_negro
        TipoProducto.OTRO -> R.drawable.otro
    }
}

@Composable
fun ListaProductos(productos: List<Producto>,
                   onProductoClick: (String) -> Unit,
                   modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier.fillMaxSize()) {
        items(productos) {
                producto ->
            TarjetaProducto(nombre = producto.nombre,
                descripcion = producto.descripcion,
                stock = producto.stockActual,
                precio = producto.precio,
                imagenResId = producto.tipo.obtenerImagenRes(),
                onClick = {onProductoClick(producto.codigo)})
        }
    }
}