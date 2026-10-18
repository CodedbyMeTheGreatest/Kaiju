package com.kaiju.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kaiju.app.model.CategoriaProducto
import com.kaiju.app.model.ErroresProducto
import com.kaiju.app.model.FormularioProductoEstado
import com.kaiju.app.model.Producto
import com.kaiju.app.model.TipoProducto
import com.kaiju.app.repository.ProductoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FormularioProductoViewModel(private val repositorioProducto: ProductoRepository): ViewModel(){
    private val _estadoFormulario = MutableStateFlow(FormularioProductoEstado())

    val estadoFormulario: StateFlow<FormularioProductoEstado> = _estadoFormulario.asStateFlow()

    private val productos: StateFlow<List<Producto>> = repositorioProducto.productos.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = emptyList()
    )

    fun actualizarCodigo(valor: String) = _estadoFormulario.update { it.copy(codigo = valor) }

    fun actualizarNombre(valor: String) = _estadoFormulario.update { it.copy(nombre = valor) }

    fun actualizarDescripcion(valor: String) = _estadoFormulario.update { it.copy(descripcion = valor) }

    fun actualizarCategoria(valor: CategoriaProducto) = _estadoFormulario.update { it.copy(categoria = valor) }

    fun actualizarTipo(valor: TipoProducto) = _estadoFormulario.update { it.copy(tipo = valor) }

    fun actualizarPrecio(valor: Int) = _estadoFormulario.update { it.copy(precio = valor) }

    fun actualizarStockActual(valor: Int) = _estadoFormulario.update { it.copy(stockActual = valor) }

    fun actualizarStockMinimo(valor: Int) = _estadoFormulario.update { it.copy(stockMinimo = valor) }

    fun actualizarDetalle(valor: String) = _estadoFormulario.update { it.copy(detalle = valor) }

    // Validaciones
    fun validarFormulario(): Boolean {
        val estado = _estadoFormulario.value
        val listaProductos = productos.value


        val errorCodigo =  when {
            estado.codigo.isBlank() -> "El código es obligatorio"
            listaProductos.any { it.codigo == estado.codigo } -> "El código ya existe"
            else -> null
        }

        val errorNombre =  if(estado.nombre.isBlank()) "El nombre es obligatorio"  else null
        val errorDescripcion =  if(estado.descripcion.isBlank()) "La descripción es obligatoria"  else null

        val errorCategoria =  if(estado.categoria == null) "La categoría es obligatoria"  else null
        val errorTipo =  if(estado.tipo == null) "El tipo de producto es obligatorio"  else null

        val errorPrecio =  if(estado.precio <= 0) "El precio debe ser mayor a cero"  else null
        val errorStockActual =  if(estado.stockActual < 0) "El stock actual no puede ser negativo ni 0"  else null
        val errorStockMinimo =  if(estado.stockMinimo < 0) "El stock minimo no puede ser negativo ni 0"  else null

        val errorDetalle =  if(estado.detalle.isBlank()) "El código es obligatorio"  else null

        val nuevosErrores = ErroresProducto(
            errorCodigo = errorCodigo,
            errorNombre = errorNombre,
            errorDescripcion = errorDescripcion,
            errorCategoria = errorCategoria,
            errorTipoProducto = errorTipo,
            errorPrecio = errorPrecio,
            errorStockActual = errorStockActual,
            errorStockMinimo = errorStockMinimo,
            errorDetalle = errorDetalle
        )

        _estadoFormulario.update{
            it.copy(errores = nuevosErrores)
        }

        return !nuevosErrores.hayErrores
    }

    fun guardarProducto(): Boolean {
        if (!validarFormulario()) return false

        val estado = _estadoFormulario.value

        val nuevoProducto = Producto(
            codigo = estado.codigo.trim(),
            nombre = estado.nombre.trim(),
            descripcion = estado.descripcion.trim(),
            categoria = estado.categoria!!,
            tipo = estado.tipo!!,
            precio = estado.precio,
            stockActual = estado.stockActual,
            stockMinimo = estado.stockMinimo,
            detalle = estado.detalle.trim()
        )

        viewModelScope.launch {
            repositorioProducto.guardar(nuevoProducto)
            _estadoFormulario.value = FormularioProductoEstado()
        }

        return true
    }
}
