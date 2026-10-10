package com.kaiju.app.model

data class Producto (
    val codigo:String,
    val nombre:String,
    val descripcion:String,
    val categoria: CategoriaProducto,
    val tipo: TipoProducto,
    val precio:Int,
    val stockActual:Int,
    val stockMinimo: Int,
    val detalle: String
)
