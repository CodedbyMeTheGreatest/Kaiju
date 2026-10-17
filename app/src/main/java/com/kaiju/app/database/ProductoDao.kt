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

    @Insert
    suspend fun insertar(producto: Producto)

    //@Delete
    //suspend fun eliminar(producto: Producto)
}