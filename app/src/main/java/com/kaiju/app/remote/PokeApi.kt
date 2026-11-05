package com.kaiju.app.remote

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokeApi {
    @GET("pokemon")
    suspend fun listarPokemon(@Query("limit") limite: Int = 2000, @Query("offset") desfase: Int = 0): PokemonListaDto
    @GET("pokemon/{nombre}")
    suspend fun obtenerPokemon(@Path("nombre") nombre: String): Response<PokemonDto>
}