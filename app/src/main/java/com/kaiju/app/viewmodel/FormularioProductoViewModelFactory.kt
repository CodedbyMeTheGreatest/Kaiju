package com.kaiju.app.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kaiju.app.database.TiendaDatabase
import com.kaiju.app.repository.ProductoRepository

class FormularioProductoViewModelFactory(context: Context): ViewModelProvider.Factory {
    private val repository = ProductoRepository(
        TiendaDatabase.obtenerInstancia(context).productoDao()
    )

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FormularioProductoViewModel(repository) as T
    }
}