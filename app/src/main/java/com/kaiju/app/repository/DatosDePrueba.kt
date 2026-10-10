package com.kaiju.app.repository

import com.kaiju.app.model.CategoriaProducto
import com.kaiju.app.model.Cuenta
import com.kaiju.app.model.FormularioProductoEstado
import com.kaiju.app.model.Producto
import com.kaiju.app.model.TipoProducto

val productosFicticios = mutableListOf(
    Producto(
        codigo = "POL-001",
        nombre = "Polera Oversize Algodón",
        descripcion = "Polera de corte holgado 100% algodón peinado",
        categoria = CategoriaProducto.Hombre,
        tipo = TipoProducto.Polera,
        precio = 15990,
        stockActual = 25,
        stockMinimo = 5,
        detalle = "Color negro, talle L"
    ),
    Producto(
        codigo = "PAN-002",
        nombre = "Jeans Cargo Style",
        descripcion = "Pantalón cargo de mezclilla con bolsillos laterales",
        categoria = CategoriaProducto.Hombre,
        tipo = TipoProducto.Pantalon,
        precio = 29990,
        stockActual = 12,
        stockMinimo = 3,
        detalle = "Talla 42, color azul gastado"
    ),
    Producto(
        codigo = "CHA-003",
        nombre = "Chaqueta Cortaviento",
        descripcion = "Chaqueta liviana impermeable con gorro",
        categoria = CategoriaProducto.Mujer,
        tipo = TipoProducto.Chaqueta,
        precio = 34990,
        stockActual = 8,
        stockMinimo = 2,
        detalle = "Color verde oliva, impermeable"
    )
)

val cuentasFicticias = mutableListOf(
    Cuenta(
        usuario = "admin@kaiju.com",
        contrasenia = "admin123"
    ),
    Cuenta(
        usuario = "vendedor01",
        contrasenia = "vendedor2024"
    ),
    Cuenta(
        usuario = "cliente_prueba",
        contrasenia = "claveSegura1"
    )
)


