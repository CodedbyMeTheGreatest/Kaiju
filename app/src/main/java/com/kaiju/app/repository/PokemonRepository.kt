package com.kaiju.app.repository

import com.kaiju.app.model.PokemonCoincidencia
import com.kaiju.app.remote.PokeApi
import com.kaiju.app.remote.PokemonDto

class PokemonRepository(private val api: PokeApi) {



    suspend fun buscar(nombre: String): PokemonDto? {
        return try {
            val respuesta = api.obtenerPokemon(nombre.lowercase().trim())
            if (respuesta.isSuccessful) respuesta.body() else null
        } catch (e: Exception) {
            null
        }
    }
    suspend fun detectarEnTexto(texto: String): PokemonCoincidencia? {
        val palabras = texto.lowercase().replace(Regex("[^a-z0-9\\s]"), "").split("\\s+".toRegex())
        for (palabra in palabras) {
            if (palabra.isBlank() || palabra.length < 3) continue
            try {
                val respuesta = api.obtenerPokemon(palabra)
                if (respuesta.isSuccessful && respuesta.body() != null) {
                    val dto = respuesta.body()!!
                    return PokemonCoincidencia(nombre = dto.name, id = dto.id)
                }
            } catch (e: Exception) {
                // Sigue intentando con la siguiente palabra
            }
        }
        return null
    }
}