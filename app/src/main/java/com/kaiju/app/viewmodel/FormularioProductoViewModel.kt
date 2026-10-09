package com.kaiju.app.viewmodel

import androidx.lifecycle.ViewModel
import com.kaiju.app.model.CategoriaProducto
import com.kaiju.app.model.ErroresProducto
import com.kaiju.app.model.FormularioProductoEstado
import com.kaiju.app.model.Producto
import com.kaiju.app.model.TipoProducto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class FormularioProductoViewModel: ViewModel(){

    private val _estadoFormulario = MutableStateFlow(FormularioProductoEstado())

    val estadoFormulario: StateFlow<FormularioProductoEstado> = _estadoFormulario.asStateFlow()

    private val _productos = MutableStateFlow<List<Producto>>(emptyList())

    val productos: StateFlow<List<Producto>> = _productos.asStateFlow()

    fun actualizarCodigo(valor: String) = _estadoFormulario.update { it.copy(codigo = valor) }

    fun actualizarNombre(valor: String) = _estadoFormulario.update { it.copy(nombre = valor) }

    fun actualizarDescripcion(valor: String) = _estadoFormulario.update { it.copy(descripcion = valor) }

    fun actualizarCategoria(valor: CategoriaProducto) = _estadoFormulario.update { it.copy(categoria = valor) }

    fun actualizarTipo(valor: TipoProducto) = _estadoFormulario.update { it.copy(tipo = valor) }

    fun actualizarPrecio(valor: Int) = _estadoFormulario.update { it.copy(precio = valor) }

    fun actualizarStockActual(valor: Int) = _estadoFormulario.update { it.copy(stockActual = valor) }

    fun actualizarDescripcion(valor: Int) = _estadoFormulario.update { it.copy(stockMinimo = valor) }

    fun actualizarDetalle(valor: String) = _estadoFormulario.update { it.copy(detalle = valor) }

    // Validaciones
    fun validarFormulario(): Boolean {
        val estado = _estadoFormulario.value

        val errorCodigo =  if(estado.codigo.isBlank()) "El código es obligatorio" else null
        //Validar que el codigo sea unico


        val errorNombre =  if(estado.nombre.isBlank()) "El nombre es obligatorio"  else null

        //val errorDescripcion =  if(estado.descripcion.isBlank()) "El código es obligatorio"  else null

        val errorCategoria =  if(estado.categoria == CategoriaProducto.SinCategoria) "El código es obligatorio"  else null
        //validar que la categoria correspendo a algun enum

        val errorTipo =  if(estado.tipo == TipoProducto.SinTipo) "El código es obligatorio"  else null
        //validar que el tipo correspendo a algun enum

        val errorPrecio =  if(estado.precio == 0) "El código es obligatorio"  else null
        // validar que el tipo sea mayor o igual a cero

        val errorStockActual =  if(estado.stockActual == 0) "El código es obligatorio"  else null
        // validar que el stock sea mayor o igual a cero

        val errorStockMinimo =  if(estado.stockMinimo == 0) "El código es obligatorio"  else null
        //revisar si este es obligatorio o no. Yo creo que si.


        //val errorDetalle =  if(estado.detalle.isBlank()) "El código es obligatorio"  else null

        _estadoFormulario.update{
            it.copy(errores = ErroresProducto(errorCodigo, errorNombre, errorCategoria, errorTipo, errorPrecio, errorStockActual, errorStockMinimo))
        }

        return errorCodigo == null &&  errorNombre == null && errorCategoria  == null && errorTipo  == null && errorPrecio  == null && errorStockActual  == null && errorStockMinimo  == null

    }
}
