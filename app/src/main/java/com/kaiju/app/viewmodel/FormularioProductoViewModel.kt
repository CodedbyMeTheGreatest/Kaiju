package com.kaiju.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kaiju.app.model.CategoriaProducto
import com.kaiju.app.model.ErroresProducto
import com.kaiju.app.model.EstadoBoton
import com.kaiju.app.model.FormularioProductoEstado
import com.kaiju.app.model.Producto
import com.kaiju.app.model.TipoProducto
import com.kaiju.app.repository.ProductoRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

class FormularioProductoViewModel(private val repositorioProducto: ProductoRepository): ViewModel(){
    private val _estadoFormulario = MutableStateFlow(FormularioProductoEstado())

    val estadoFormulario: StateFlow<FormularioProductoEstado> = _estadoFormulario.asStateFlow()

    private val productos: StateFlow<List<Producto>> = repositorioProducto.productos.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = emptyList()
    )

    private val _estadoBoton = MutableStateFlow(EstadoBoton.INACTIVO)
    val estadoBoton: StateFlow<EstadoBoton> = _estadoBoton.asStateFlow()

    fun actualizarCodigo(valor: String) = _estadoFormulario.update { it.copy(codigo = valor) }

    fun actualizarNombre(valor: String) = _estadoFormulario.update { it.copy(nombre = valor) }

    fun actualizarDescripcion(valor: String) = _estadoFormulario.update { it.copy(descripcion = valor) }

    fun actualizarCategoria(valor: CategoriaProducto) = _estadoFormulario.update { it.copy(categoria = valor) }

    fun actualizarTipo(valor: TipoProducto) = _estadoFormulario.update { it.copy(tipo = valor) }

    fun actualizarPrecio(valor: String) = _estadoFormulario.update { it.copy(precio = valor.filter { c -> c.isDigit() }) }

    fun actualizarStockActual(valor: String) = _estadoFormulario.update { it.copy(stockActual = valor.filter { c -> c.isDigit() }) }

    fun actualizarStockMinimo(valor: String) = _estadoFormulario.update { it.copy(stockMinimo = valor.filter { c -> c.isDigit() }) }

    fun actualizarDetalle(valor: String) = _estadoFormulario.update { it.copy(detalle = valor) }

    fun validarFormulario(): Boolean {
        val estado = _estadoFormulario.value
        val listaProductos = productos.value
        val precioInt = estado.precio.toIntOrNull()
        val stockActualInt = estado.stockActual.toIntOrNull()
        val stockMinimoInt = estado.stockMinimo.toIntOrNull()


        val errorCodigo =  when {
            estado.codigo.isBlank() -> "El código es obligatorio"
            listaProductos.any { it.codigo == estado.codigo } -> "El código ya existe"
            else -> null
        }

        val errorNombre =  if(estado.nombre.isBlank()) "El nombre es obligatorio"  else null
        val errorDescripcion =  if(estado.descripcion.isBlank()) "La descripción es obligatoria"  else null

        val errorCategoria =  if(estado.categoria == null) "La categoría es obligatoria"  else null
        val errorTipo =  if(estado.tipo == null) "El tipo de producto es obligatorio"  else null


        val errorPrecio = when {
            estado.precio.isBlank() -> "El precio es obligatorio"
            precioInt == null || precioInt <= 0 -> "El precio debe ser un número mayor a cero"
            else -> null
        }
        val errorStockActual = when {
            estado.stockActual.isBlank() -> "El stock actual es obligatorio"
            stockActualInt == null || stockActualInt < 0 -> "El stock actual debe ser un número mayor o igual a cero"
            else -> null
        }
        val errorStockMinimo = when {
            estado.stockMinimo.isBlank() -> "El stock mínimo es obligatorio"
            stockMinimoInt == null || stockMinimoInt < 0 -> "El stock mínimo debe ser un número mayor o igual a cero"
            else -> null
        }

        val errorDetalle =  if(estado.detalle.isBlank()) "El detalle es obligatorio"  else null

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
            precio = estado.precio.toIntOrNull() ?:0,
            stockActual = estado.stockActual.toIntOrNull() ?:0,
            stockMinimo = estado.stockMinimo.toIntOrNull() ?:0,
            detalle = estado.detalle.trim()
        )

        viewModelScope.launch {
            _estadoBoton.value = EstadoBoton.CARGANDO
            try{
                delay(800.milliseconds)
                repositorioProducto.guardar(nuevoProducto)
                _estadoBoton.value = EstadoBoton.EXITOSO
                delay(1200.milliseconds)
                _estadoFormulario.value = FormularioProductoEstado()
                _estadoBoton.value = EstadoBoton.INACTIVO
            }catch(e: Exception) {
                _estadoBoton.value = EstadoBoton.INACTIVO
            }
        }

        return true
    }
}
