package com.kaiju.app.model

import java.time.LocalDateTime

data class Movimiento (
    val id: String,
    val codigoProducto: String,
    val tipoMovimiento: TipoMovimiento,
    val cantidad: Int,
    val motivo: String = "",
    val fecha: LocalDateTime,
    val usuario: String

    //Pone el codigoProducto como llave foranea, y haz un Movimiento Dao que obtenga todos los movimientos de un producto. :)
    //Tambien un movimiento repository que tenga obtenermovimientosporproducto por el codigo del producto.
    //Hace una tarjetamovimiento item (como la tarjetafichaproducto)
    //Pa usarlo en el detalle producto.
)