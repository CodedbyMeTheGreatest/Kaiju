package com.kaiju.app.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.kaiju.app.model.Movimiento
import kotlinx.coroutines.flow.Flow

@Dao
abstract class MovimientoDao {
    @Query("SELECT * FROM movimientos ORDER BY fecha DESC")
    abstract fun obtenerMovimientos(): Flow<List<Movimiento>>

    @Query("SELECT * FROM movimientos WHERE codigoProducto = :codigo ORDER BY fecha DESC")
    abstract fun obtenerHistorial(codigo: String): Flow<List<Movimiento>>

    @Insert
    abstract suspend fun insertarMovimiento(movimiento: Movimiento)

    @Query("UPDATE products SET stockActual = :nuevoStock WHERE codigo = :codigo")
    abstract suspend fun actualizarStock(codigo: String, nuevoStock: Int)

    @Transaction
    open suspend fun registrarMovimiento(movimiento: Movimiento, nuevoStock: Int) {
        insertarMovimiento(movimiento)
        actualizarStock(movimiento.codigoProducto, nuevoStock)
    }
}