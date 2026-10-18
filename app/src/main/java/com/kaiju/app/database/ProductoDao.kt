package com.kaiju.app.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.kaiju.app.model.Producto
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductoDao {
    @Query("SELECT * FROM productos ORDER BY id DESC")
    fun obtenerTodos(): Flow<List<Producto>>

    @Query("SELECT * FROM productos WHERE codigo = :codigo LIMIT 1")
    fun obtenerPorCodigo(codigo: String): Flow<Producto?>

    @Insert
    suspend fun insertar(producto: Producto)

    //@Delete
    //suspend fun eliminar(producto: Producto)
}