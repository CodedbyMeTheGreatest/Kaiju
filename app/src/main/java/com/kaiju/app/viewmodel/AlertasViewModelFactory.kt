package com.kaiju.app.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kaiju.app.database.TiendaDatabase
import com.kaiju.app.repository.ProductoRepository

class AlertasViewModelFactory(context: Context) : ViewModelProvider.Factory {
    private val productoRepositorio = ProductoRepository(
        TiendaDatabase.obtenerInstancia(context).productoDao()
    )

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        AlertasViewModel(productoRepositorio) as T
}