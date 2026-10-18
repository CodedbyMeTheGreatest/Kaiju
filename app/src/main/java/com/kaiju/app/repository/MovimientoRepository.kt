package com.kaiju.app.repository

import com.kaiju.app.model.Movimiento
import kotlinx.coroutines.flow.Flow
import com.kaiju.app.database.MovimientoDao
class MovimientoRepository(private val dao: MovimientoDao) {

    val movimientos: Flow<List<Movimiento>> = dao.obtenerMovimientos()
    fun obtenerPorProducto(codigo: String): Flow<List<Movimiento>> = dao.obtenerHistorial(codigo)
    suspend fun registrarMovimiento(movimiento: Movimiento, nuevoStock: Int) =
        dao.registrarMovimiento(movimiento, nuevoStock)

}