package com.kaiju.app.model

fun RolUsuario.puedeVerAlertas() =
    this == RolUsuario.ADMINISTRADOR || this == RolUsuario.ENCARGADO_INVENTARIO

fun RolUsuario.puedeGestionarProductos() = this == RolUsuario.ADMINISTRADOR

fun RolUsuario.tiposMovimientoPermitidos(): List<TipoMovimiento> = when (this) {
    RolUsuario.VENDEDOR -> listOf(TipoMovimiento.SALIDA_VENTA)
    RolUsuario.ENCARGADO_INVENTARIO -> TipoMovimiento.values().toList()
    RolUsuario.ADMINISTRADOR -> listOf(TipoMovimiento.ENTRADA, TipoMovimiento.AJUSTE)
}