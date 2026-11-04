package com.kaiju.app.viewmodel

import com.kaiju.app.remote.PokemonDto

data class FichaUiState(
    val visible: Boolean = false,
    val nombre: String = "",
    val cargando: Boolean = false,
    val pokemon: PokemonDto? = null,
    val error: String? = null
)