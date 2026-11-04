package com.kaiju.app.model

data class PokemonCoincidencia(val nombre: String, val id: Int) {
    val spriteUrl: String
        get() = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$id.png"
}