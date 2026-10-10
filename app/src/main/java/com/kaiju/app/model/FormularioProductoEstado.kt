package com.kaiju.app.model

/*Producto
Código	Obligatorio y no repetido (RN-03).
Nombre	Obligatorio, al menos 3 caracteres.
Categoría	Obligatorio elegir una de la lista.
Precio	Número entero mayor que cero (RN-02).
Stock mínimo	Número entero mayor o igual a cero (RN-02).
*/

data class FormularioProductoEstado(
    val codigo: String = "",
    val nombre: String = "",
    val descripcion: String = "",
    val categoria: CategoriaProducto = CategoriaProducto.SinCategoria,
    val tipo: TipoProducto = TipoProducto.SinTipo,
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
)