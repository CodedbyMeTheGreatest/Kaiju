package com.kaiju.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kaiju.app.model.*
import com.kaiju.app.repository.MovimientoRepository
import com.kaiju.app.repository.ProductoRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.util.UUID

class FormularioMovimientoViewModel(
    private val movimientoRepository: MovimientoRepository, productoRepository: ProductoRepository
) : ViewModel() {

    private val _estado = MutableStateFlow(FormularioMovimientoEstado())
    val estado: StateFlow<FormularioMovimientoEstado> = _estado.asStateFlow()

    val productos: StateFlow<List<Producto>> = productoRepository.productos.stateIn(viewModelScope,
        SharingStarted.Eagerly, emptyList())

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

        viewModelScope.launch {movimientoRepository.registrarMovimiento(movimiento,nuevoStock)}

        _estado.value = FormularioMovimientoEstado()
        return true
    }
}