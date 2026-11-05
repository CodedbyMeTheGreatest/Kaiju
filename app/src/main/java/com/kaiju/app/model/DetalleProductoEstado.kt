package com.kaiju.app.model

import com.kaiju.app.remote.PokemonDto

data class DetalleProductoEstado(
    val cargando: Boolean = true,
    val producto: Producto? = null,
    val movimientos: List<Movimiento> = emptyList(),
    val pokemon: PokemonDto? = null,
    val cargandoPokemon: Boolean = false,
    val errorConexionPokemon: Boolean = false
)
