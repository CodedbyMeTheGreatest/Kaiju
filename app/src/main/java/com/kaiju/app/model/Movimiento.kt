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

)