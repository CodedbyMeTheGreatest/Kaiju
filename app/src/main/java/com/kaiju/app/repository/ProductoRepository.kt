package com.kaiju.app.repository

import com.kaiju.app.database.ProductoDao
import com.kaiju.app.model.Producto
import kotlinx.coroutines.flow.Flow

class ProductoRepository(private val dao: ProductoDao) {
    val productos: Flow<List<Producto>> = dao.obtenerTodos()

    suspend fun guardar(producto: Producto) = dao.insertar(producto)

    //suspend fun eliminar(producto: Producto) = dao.eliminar(producto)


}