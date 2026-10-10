package com.kaiju.app.model

import java.time.LocalDateTime

data class FormularioMovimientoEstado (
    val codigoProducto: String = "",
    val tipo: TipoMovimiento? = null,
    val cantidad: String = "",
    val motivo: String = "",
    val usuarioActivo: Usuario? = null,
    val errores: ErroresMovimiento = ErroresMovimiento()
)

data class ErroresMovimiento(
    val errorCodigoProducto: String? = null,
    val errorTipoMovimiento: String? = null,
    val errorCantidad: String? = null,
    val errorMotivo: String? = null,
) {
    val hayErrores: Boolean
        get() = listOf(errorCodigoProducto, errorTipoMovimiento, errorCantidad, errorMotivo).any { it != null}
}