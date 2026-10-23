package com.kaiju.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kaiju.app.model.Producto
import com.kaiju.app.repository.ProductoRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class AlertasViewModel (productoRepositorio: ProductoRepository): ViewModel() {
    val productosEnAlerta: StateFlow<List<Producto>> = productoRepositorio.productos
        .map { lista ->
            lista
                .filter { it.stockActual <= it.stockMinimo }
                .sortedBy { it.stockActual - it.stockMinimo }
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
}