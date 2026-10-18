package com.kaiju.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kaiju.app.model.DetalleProductoEstado
import com.kaiju.app.repository.MovimientoRepository
import com.kaiju.app.repository.ProductoRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class DetalleProductoViewModel(
    private val codigoProducto: String,
    productoRepository: ProductoRepository,
    movimientoRepository: MovimientoRepository
): ViewModel() {

    val estadoDetalleProducto: StateFlow<DetalleProductoEstado> = combine(productoRepository.obtenerPorCodigo(codigoProducto),
        movimientoRepository.obtenerPorProducto(codigoProducto))
    { producto, movimientos ->
        DetalleProductoEstado(
            cargando = false,
            producto = producto,
            movimientos = movimientos
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = DetalleProductoEstado(cargando = true)
    )
}