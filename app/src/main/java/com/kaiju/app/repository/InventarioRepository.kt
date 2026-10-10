package com.kaiju.app.repository

import com.kaiju.app.model.Movimiento
import com.kaiju.app.model.Producto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class InventarioRepository(
    productosIniciales: List<Producto> = emptyList(),
    movimientosIniciales: List<Movimiento> = emptyList()
) {
    private val _productos = MutableStateFlow(productosIniciales)
    val productos: StateFlow<List<Producto>> = _productos.asStateFlow()

    private val _movimientos = MutableStateFlow(movimientosIniciales)
    val movimientos: StateFlow<List<Movimiento>> = _movimientos.asStateFlow()

    fun obtenerProducto(codigo: String): Producto? =
        _productos.value.find { it.codigo == codigo }

    fun existeCodigo(codigo: String): Boolean =
        _productos.value.any { it.codigo.equals(codigo, ignoreCase = true)}

    fun guardarProducto(producto: Producto){
        _productos.update { lista ->
            if (lista.any {it.codigo == producto.codigo})
                lista.map { if (it.codigo== producto.codigo) producto else it}
            else
                lista+producto
        }
    }

    fun registrarMovimiento(movimiento: Movimiento, nuevoStock: Int) {
        _movimientos.update { it + movimiento }
        _productos.update { lista ->
            lista.map {
                if (it.codigo == movimiento.codigoProducto) it.copy(stockActual = nuevoStock) else it
            }
        }
    }
}