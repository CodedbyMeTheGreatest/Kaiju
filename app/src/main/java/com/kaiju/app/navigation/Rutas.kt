package com.kaiju.app.navigation

sealed class Rutas(val ruta: String) {
    object InicioSesion: Rutas("Inicio Sesión")
    object Catalogo: Rutas("Catálogo")
    object GestionProducto: Rutas("Gestión Producto")
    object GestionMovimiento: Rutas("Gestión Movimiento")

    object DetalleProducto : Rutas("DetalleProducto/{codigoProducto}") {
        fun crearRuta(codigoProducto: String) = "DetalleProducto/$codigoProducto"
    }
    object Alertas : Rutas("alertas")
}
