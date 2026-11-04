package com.kaiju.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonParseException
import com.kaiju.app.model.PokemonCoincidencia
import com.kaiju.app.repository.PokemonRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import kotlin.coroutines.cancellation.CancellationException

class PokemonViewModel(private val repositorio: PokemonRepository) : ViewModel() {

    //sugerencia. se utiliza esta parte para la deteccion de productos con nombre de pokemon
    private val _coincidencias = MutableStateFlow<Map<String, PokemonCoincidencia>>(emptyMap())
    val coincidencias = _coincidencias.asStateFlow()
    private val revisados = mutableSetOf<String>()

    fun detectar(nombres: List<String>) {
        val pendientes = nombres.distinct().filter { it !in revisados }
        if (pendientes.isEmpty()) return
        viewModelScope.launch {
            try {
                for (nombre in pendientes) {
                    repositorio.detectar(nombre)?.let { c ->
                        _coincidencias.update { it + (nombre to c) }
                    }
                    revisados += nombre
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: IOException) {
            } catch (e: HttpException) {
            } catch (e: JsonParseException) {
            }
        }
    }

    //sugerencia. Se utiliza esta parte para la ficha del pokemon

    private val _ficha = MutableStateFlow(FichaUiState())
    val ficha = _ficha.asStateFlow()

    private var trabajoFicha: Job? = null

    fun abrirFicha(nombrePokemon: String) {
        _ficha.value = FichaUiState(visible = true, nombre = nombrePokemon)
        cargarFicha(nombrePokemon)
    }

    fun reintentar() {
        if (!_ficha.value.cargando) cargarFicha(_ficha.value.nombre)
    }

    fun cerrarFicha() {
        trabajoFicha?.cancel()
        _ficha.value = FichaUiState()
    }

    private fun cargarFicha(nombre: String) {
        trabajoFicha?.cancel()
        _ficha.update { it.copy(cargando = true, error = null) }
        trabajoFicha = viewModelScope.launch {
            try {
                val p = repositorio.buscar(nombre)
                _ficha.update { it.copy(cargando = false, pokemon = p) }
            } catch (e: CancellationException) {
                throw e
            } catch (e: HttpException) {
                mostrarError(
                    if (e.code() == 404) "No encontré ese Pokémon."
                    else "El servidor respondió con error ${e.code()}."
                )
            } catch (e: IOException) {
                mostrarError("No pude conectar. Revisa internet e intenta nuevamente.")
            } catch (e: JsonParseException) {
                mostrarError("La respuesta no tiene el formato esperado.")
            }
        }
    }

    private fun mostrarError(mensaje: String) =
        _ficha.update { it.copy(cargando = false, error = mensaje) }
}