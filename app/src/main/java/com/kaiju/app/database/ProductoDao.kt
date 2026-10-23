package com.kaiju.app.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.kaiju.app.model.Producto
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductoDao {
    @Query("SELECT COUNT(*) FROM products")
    suspend fun contar(): Int

    @Query("SELECT * FROM products ORDER BY id DESC")
    fun obtenerTodos(): Flow<List<Producto>>

    @Query("SELECT * FROM products WHERE codigo = :codigo LIMIT 1")
    fun obtenerPorCodigo(codigo: String): Flow<Producto?>

    @Query("SELECT * FROM products WHERE nombre LIKE '%' || :query || '%' ORDER BY nombre ASC")
    fun buscarProducto(query: String): Flow<List<Producto>>

    @Insert
    suspend fun insertar(producto: Producto)

    //@Delete
    //suspend fun eliminar(producto: Producto)
}