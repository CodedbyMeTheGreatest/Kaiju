package com.kaiju.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kaiju.app.model.*
import com.kaiju.app.repository.MovimientoRepository
import com.kaiju.app.repository.ProductoRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.util.UUID
import kotlin.time.Duration.Companion.milliseconds

class FormularioMovimientoViewModel(
    private val movimientoRepository: MovimientoRepository, productoRepository: ProductoRepository
) : ViewModel() {

    private val _alertasGeneradas = MutableSharedFlow<Producto>(extraBufferCapacity = 1)
    val alertasGeneradas: SharedFlow<Producto> = _alertasGeneradas.asSharedFlow()
    private val _estado = MutableStateFlow(FormularioMovimientoEstado())
    val estado: StateFlow<FormularioMovimientoEstado> = _estado.asStateFlow()

    val productos: StateFlow<List<Producto>> = productoRepository.productos.stateIn(viewModelScope,
        SharingStarted.Eagerly, emptyList())

    private val _estadoBoton = MutableStateFlow(EstadoBoton.INACTIVO)
    val estadoBoton: StateFlow<EstadoBoton> = _estadoBoton.asStateFlow()

    private fun buscarProducto(codigo: String): Producto? = productos.value.find { it.codigo == codigo }
    fun actualizarProducto(codigo: String) = _estado.update { it.copy(codigoProducto = codigo) }
    fun actualizarTipo(tipo: TipoMovimiento) = _estado.update { it.copy(tipo = tipo) }
    fun actualizarCantidad(valor: String) = _estado.update { it.copy(cantidad = valor) }
    fun actualizarMotivo(valor: String) = _estado.update { it.copy(motivo = valor) }

    private fun calcularNuevoStock(actual: Int, tipo: TipoMovimiento, cantidad: Int) = when (tipo) {
        TipoMovimiento.ENTRADA -> actual + cantidad
        TipoMovimiento.SALIDA_VENTA, TipoMovimiento.SALIDA_CONSUMO -> actual - cantidad
        TipoMovimiento.AJUSTE -> actual + cantidad
    }

    fun validarFormulario(): Boolean {
        val estado = _estado.value
        val tipo = estado.tipo
        val producto = buscarProducto(estado.codigoProducto)
        val cantidad = estado.cantidad.trim().toIntOrNull()
        val esAjuste = tipo == TipoMovimiento.AJUSTE

        val errores = ErroresMovimiento(
            errorCodigoProducto = if (producto == null) "Elige un producto" else null,
            errorTipoMovimiento = if (tipo == null) "Elige un tipo de movimiento" else null,
            errorCantidad = when {
                cantidad == null -> "Ingresa un número entero"
                esAjuste && cantidad == 0 -> "El ajuste no puede ser 0"
                !esAjuste && cantidad <= 0 -> "La cantidad debe ser mayor que cero"
                producto != null && tipo != null &&
                        calcularNuevoStock(producto.stockActual, tipo, cantidad) < 0 ->
                    "El stock no puede quedar bajo cero (disponible: ${producto.stockActual})"
                else -> null
            },
            errorMotivo = if (esAjuste && estado.motivo.isBlank()) "Todo ajuste exige un motivo" else null
        )
        _estado.update { it.copy(errores = errores) }
        return !errores.hayErrores
    }

    fun reiniciarFormulario() { _estado.value = FormularioMovimientoEstado() }
    fun guardarMovimiento(usuario: String): Boolean {
        if (!validarFormulario()) return false
        val estado = _estado.value
        val tipo = estado.tipo ?: return false
        val producto = buscarProducto(estado.codigoProducto) ?: return false
        val cantidad = estado.cantidad.trim().toIntOrNull() ?: return false


        val movimiento = Movimiento(
            id = UUID.randomUUID().toString(),
            codigoProducto = producto.codigo,
            tipoMovimiento = tipo,
            cantidad = cantidad,
            motivo = estado.motivo.trim(),
            fecha = LocalDateTime.now(),
            usuario = usuario
         )
        val nuevoStock = calcularNuevoStock(producto.stockActual, tipo, cantidad)

        viewModelScope.launch {
            _estadoBoton.value = EstadoBoton.CARGANDO
            try{
                delay(800.milliseconds)
                movimientoRepository.registrarMovimiento(movimiento,nuevoStock)
                if (nuevoStock <= producto.stockMinimo && nuevoStock < producto.stockActual) {
                    _alertasGeneradas.tryEmit(producto.copy(stockActual = nuevoStock))
                }
                _estadoBoton.value = EstadoBoton.EXITOSO
                delay(1200.milliseconds)
                _estado.value = FormularioMovimientoEstado()
                _estadoBoton.value = EstadoBoton.INACTIVO
            }catch (e: Exception){
                _estadoBoton.value = EstadoBoton.INACTIVO
            }
        }

        return true
    }
}