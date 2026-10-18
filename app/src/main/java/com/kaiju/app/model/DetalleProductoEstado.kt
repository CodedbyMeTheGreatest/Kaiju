package com.kaiju.app.model

data class DetalleProductoEstado(
    val cargando: Boolean = true,
    val producto: Producto? = null,
    val movimientos: List<Movimiento> = emptyList()
)
