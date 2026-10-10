package com.kaiju.app.navigation

sealed class Rutas(val ruta: String) {
    object InicioSesion: Rutas("Inicio Sesión")
    object Catalogo: Rutas("Catálogo")
    object GestionProducto: Rutas("Gestión Producto")
}
