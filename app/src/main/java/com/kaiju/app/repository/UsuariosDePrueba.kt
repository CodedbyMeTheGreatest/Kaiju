package com.kaiju.app.repository

import com.kaiju.app.model.Cuenta
import com.kaiju.app.model.RolUsuario
import com.kaiju.app.model.Usuario

private val usuariosDePrueba = listOf(
    Cuenta("admin@kaiju.cl", "admin123") to
            Usuario("U-001", "admin@kaiju.cl", RolUsuario.ADMINISTRADOR),
    Cuenta("vendedor1", "venta123") to
            Usuario("U-002", "vendedor1", RolUsuario.VENDEDOR),
    Cuenta("encargado_inventario", "inventario123") to
            Usuario("U-003", "encargado_inventario", RolUsuario.ENCARGADO_INVENTARIO)
)


fun autenticar(usuario: String, contrasenia: String): Usuario? =
    usuariosDePrueba.firstOrNull { (cuenta, _) ->
        cuenta.usuario.equals(usuario, ignoreCase = true) && cuenta.contrasenia == contrasenia
    }?.second