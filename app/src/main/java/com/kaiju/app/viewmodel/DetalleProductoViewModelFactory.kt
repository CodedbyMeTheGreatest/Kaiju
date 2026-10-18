package com.kaiju.app.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kaiju.app.database.TiendaDatabase
import com.kaiju.app.repository.MovimientoRepository
import com.kaiju.app.repository.ProductoRepository

class DetalleProductoViewModelFactory(
    private val context: Context,
    private val codigoProducto: String
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val db = TiendaDatabase.obtenerInstancia(context)
        val productoRepository = ProductoRepository(db.productoDao())
        val movimientoRepository = MovimientoRepository(db.movimientoDao())
        return DetalleProductoViewModel(codigoProducto, productoRepository, movimientoRepository) as T
    }
}