package com.kaiju.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kaiju.app.model.RolUsuario
import com.kaiju.app.model.Usuario
import com.kaiju.app.repository.UsuarioRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ListaUsuariosViewModel(private val repository: UsuarioRepository) : ViewModel() {
    private val _usuariosTotales = MutableStateFlow<List<Usuario>>(emptyList())

    private val _cargando = MutableStateFlow(false)
    val cargando: StateFlow<Boolean> = _cargando.asStateFlow()

    private val _mensajeError = MutableStateFlow<String?>(null)
    val mensajeError: StateFlow<String?> = _mensajeError.asStateFlow()

    private val _queryBusqueda = MutableStateFlow("")
    val queryBusqueda: StateFlow<String> = _queryBusqueda.asStateFlow()

    private val _filtroTipo = MutableStateFlow<RolUsuario?>(null)
    val filtroTipo: StateFlow<RolUsuario?> = _filtroTipo.asStateFlow()

    val usuariosFiltrados: StateFlow<List<Usuario>> = combine(
        _usuariosTotales,
        _queryBusqueda,
        _filtroTipo
    ) { lista, query, tipo ->
        lista.filter { usuario ->
            val coincideTexto = query.isBlank() ||
                    usuario.nombre.contains(query, ignoreCase = true) ||
                    usuario.apellidos.contains(query, ignoreCase = true)

            val coincideTipo = tipo == null || usuario.tipo == tipo

            coincideTexto && coincideTipo
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun alCambiarQueryBusqueda(nuevaQuery: String) {
        _queryBusqueda.value = nuevaQuery
    }

    fun alCambiarFiltroTipo(nuevoTipo: RolUsuario?) {
        _filtroTipo.value = nuevoTipo
    }

    fun limpiarError() {
        _mensajeError.value = null
    }

    fun cargarUsuarios(token: String) {
        viewModelScope.launch {
            _cargando.value = true
            _mensajeError.value = null

            val resultado = repository.listarUsuarios(token)

            resultado.onSuccess { listaRecibida ->
                _usuariosTotales.value = listaRecibida.filter { it.activo && it.tipo != null }
            }.onFailure { error ->
                _mensajeError.value = error.message ?: "Error desconocido al cargar usuarios"
            }

            _cargando.value = false
        }
    }
}