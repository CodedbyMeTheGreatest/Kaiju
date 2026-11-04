package com.kaiju.app.repository
import com.kaiju.app.model.PokemonCoincidencia
import com.kaiju.app.remote.PokeApi
import com.kaiju.app.remote.PokemonDto
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.text.Normalizer
import java.util.Locale

class PokemonRepository(private val api: PokeApi) {
    private val fichas = mutableMapOf<String, PokemonDto>()

    //sugerencias, indice solo se usa para volver los nombres de los pokemons a sus numeros (ej: pikachu ->25)
    //mutex es de candado por si varias pantallas piden el mismo indice, evita pedir la lista dos veces.
    //todos son sugerencias, se asume la existencia de un componente de tarjeta de ficha de pokemon
    //y un componente de icono para poder cambiarlo facil en el catalogo dsp
    // si ves una mejor opcion cambiala sin problemas


    private var indice: Map<String, Int>? = null
    private val mutex = Mutex()

    //sugerencia de funcion del easter egg, recibe el nombre de un producto y devuelve el Pokémon si el nombre
    //completo coincide con uno
    suspend fun detectar(nombreProducto: String): PokemonCoincidencia? {
        val nombre = normalizar(nombreProducto)
        if (nombre.isBlank()) return null
        val id = indiceCargado()[nombre] ?: return null
        return PokemonCoincidencia(nombre, id)
    }

    suspend fun buscar(nombre: String): PokemonDto {
        fichas[nombre]?.let { return it }
        return api.obtenerPokemon(nombre).also { fichas[nombre] = it }
    }


    //sugerencias. Devuelven la lista del indice, la primera lo hace rapido por estar cargado,
    //la segunda dentro del candado por si otra pantalla lo esta cargando mientras se esperaba.
    private suspend fun cargarIndice(): Map<String, Int> =
        api.listarPokemon().results
            .mapNotNull { r -> idDe(r.url)?.let { id -> r.name to id } }
            .toMap()

    private suspend fun indiceCargado(): Map<String, Int> =
        indice ?: mutex.withLock {
            indice ?: cargarIndice().also { indice = it }
        }

    private fun idDe(url: String): Int? =
        url.trimEnd('/').substringAfterLast('/').toIntOrNull()

    private fun normalizar(texto: String): String =
        Normalizer.normalize(texto.trim().lowercase(Locale.ROOT), Normalizer.Form.NFD)
            .replace(Regex("\\p{InCombiningDiacriticalMarks}+"), "")



}