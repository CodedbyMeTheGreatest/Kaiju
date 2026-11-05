package com.kaiju.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kaiju.app.model.DetalleProductoEstado
import com.kaiju.app.remote.PokemonDto
import com.kaiju.app.repository.MovimientoRepository
import com.kaiju.app.repository.PokemonRepository
import com.kaiju.app.repository.ProductoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DetalleProductoViewModel(
    private val codigoProducto: String,
    productoRepository: ProductoRepository,
    movimientoRepository: MovimientoRepository,
    pokemonRepository: PokemonRepository
): ViewModel() {

    private val _pokemonDto = MutableStateFlow<PokemonDto?>(null)
    val pokemonDto = _pokemonDto.asStateFlow()

    val estadoDetalleProducto: StateFlow<DetalleProductoEstado> = combine(productoRepository.obtenerPorCodigo(codigoProducto),
        movimientoRepository.obtenerPorProducto(codigoProducto))
    { producto, movimientos ->

        val pokemon = producto?.pokemonNombre?.let { nombre ->
            pokemonRepository.buscar(nombre)
        }

        DetalleProductoEstado(
            cargando = false,
            producto = producto,
            movimientos = movimientos,
            pokemon = pokemon
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = DetalleProductoEstado(cargando = true)
    )
}