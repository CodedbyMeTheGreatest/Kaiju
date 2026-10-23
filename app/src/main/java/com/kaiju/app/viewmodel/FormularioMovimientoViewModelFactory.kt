package com.kaiju.app.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kaiju.app.database.TiendaDatabase
import com.kaiju.app.repository.MovimientoRepository
import com.kaiju.app.repository.ProductoRepository

class FormularioMovimientoViewModelFactory(private val context: Context): ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val db = TiendaDatabase.obtenerInstancia(context)
        val movimientoRepositorio = MovimientoRepository(db.movimientoDao())
        val productoRepositorio = ProductoRepository(db.productoDao())
        return FormularioMovimientoViewModel(movimientoRepositorio, productoRepositorio) as T
    }
}