package com.kaiju.app.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kaiju.app.database.TiendaDatabase
import com.kaiju.app.repository.MovimientoRepository
import com.kaiju.app.repository.ProductoRepository

class FormularioMovimientoViewModelFactory(context: Context): ViewModelProvider.Factory {
    private val db = TiendaDatabase.obtenerInstancia(context)
    private val movimientoRepositorio = MovimientoRepository(db.movimientoDao())
    private val productoRepositorio = ProductoRepository(db.productoDao())

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FormularioMovimientoViewModel(movimientoRepositorio, productoRepositorio) as T
    }
}