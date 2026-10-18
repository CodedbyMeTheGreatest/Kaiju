package com.kaiju.app.model

data class FormularioProductoEstado(
    val codigo: String = "",
    val nombre: String = "",
    val descripcion: String = "",
    val categoria: CategoriaProducto? = null,
    val tipo: TipoProducto? = null,
    val precio: Int = 0,
    val stockActual: Int = 0,
    val stockMinimo: Int = 0,
    val detalle: String = "",
    val errores: ErroresProducto = ErroresProducto()
)

data class ErroresProducto(
    val errorCodigo: String? = null,
    val errorNombre: String? = null,
    val errorDescripcion: String? = null,
    val errorCategoria: String? = null,
    val errorTipoProducto: String? = null,
    val errorPrecio: String? = null,
    val errorStockActual: String? = null,
    val errorStockMinimo: String? = null,
    val errorDetalle: String? = null
){
    val hayErrores: Boolean
        get() = errorCodigo != null || errorNombre != null || errorDescripcion != null ||
                errorCategoria != null || errorTipoProducto != null || errorPrecio != null ||
                errorStockActual != null || errorStockMinimo != null || errorDetalle != null
}