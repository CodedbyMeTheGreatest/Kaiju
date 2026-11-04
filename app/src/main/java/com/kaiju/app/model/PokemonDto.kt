package com.kaiju.app.remote

import com.google.gson.annotations.SerializedName

data class PokemonDto(
    val id: Int,
    val name: String,
    val height: Int,   // decímetros
    val weight: Int,   // hectogramos
    val sprites: SpritesDto,
    val types: List<PokemonTypeDto>
)
data class SpritesDto(
    @SerializedName("front_default") val frontDefault: String?)

data class PokemonTypeDto(val slot: Int, val type: TypeDto)
data class TypeDto(val name: String)

//
data class PokemonListaDto(val results: List<PokemonResumenDto>)
data class PokemonResumenDto(val name: String, val url: String)