package com.kaiju.app

import android.app.Application
import android.util.Log
import com.kaiju.app.database.MovimientoDao
import com.kaiju.app.database.ProductoDao
import com.kaiju.app.database.TiendaDatabase
import com.kaiju.app.model.CategoriaProducto
import com.kaiju.app.model.Movimiento
import com.kaiju.app.model.Producto
import com.kaiju.app.model.TipoMovimiento
import com.kaiju.app.model.TipoProducto
import com.kaiju.app.ui.components.NotificadorStock
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.util.UUID
import kotlin.getValue

class KaijuApplication : Application() {

    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    val database by lazy { TiendaDatabase.obtenerInstancia(this) }

    override fun onCreate() {
        super.onCreate()
        NotificadorStock.crearCanal(this)
        applicationScope.launch {
            inicializarDatosSiEsNecesario()
        }
    }

    private suspend fun inicializarDatosSiEsNecesario() {
        try {
            val count = database.productoDao().contar()
            Log.d("DB_DEBUG", "Productos existentes al iniciar: $count")

            if (count == 0) {
                poblarBaseDatos(database.productoDao(), database.movimientoDao())
                Log.d("DB_DEBUG", "Poblado completado. Productos ahora: ${database.productoDao().contar()}")
            } else {
                Log.d("DB_DEBUG", "Ya hay datos, no se pobla")
            }
        } catch (e: Exception) {
            Log.e("DB_DEBUG", "ERROR poblando la base de datos", e)
        }
    }

    private suspend fun poblarBaseDatos(productoDao: ProductoDao, movimientoDao: MovimientoDao) {
        val ahora = LocalDateTime.now()

        val p1 = Producto(
            codigo = "KAI-001",
            nombre = "Polera Kaiju Oversized",
            descripcion = "Polera de algodón 100% con estampado frontal estilo anime",
            categoria = CategoriaProducto.UNISEX,
            tipo = TipoProducto.POLERA,
            precio = 19990,
            stockActual = 15,
            stockMinimo = 5,
            detalle = "Colección Urbana 2026 - Talla L"
        )
        productoDao.insertar(p1)

        movimientoDao.insertarMovimiento(
            Movimiento(
                id = UUID.randomUUID().toString(),
                codigoProducto = p1.codigo,
                tipoMovimiento = TipoMovimiento.ENTRADA,
                cantidad = 20,
                motivo = "Recepción inicial de mercadería",
                fecha = ahora.minusDays(5),
                usuario = "admin@kaiju.cl"
            )
        )
        movimientoDao.insertarMovimiento(
            Movimiento(
                id = UUID.randomUUID().toString(),
                codigoProducto = p1.codigo,
                tipoMovimiento = TipoMovimiento.SALIDA_VENTA,
                cantidad = 5,
                motivo = "Venta en tienda presencial boleta #204",
                fecha = ahora.minusDays(2),
                usuario = "vendedor1"
            )
        )

        val p2 = Producto(
            codigo = "KAI-002",
            nombre = "Chaqueta Cyberpunk Tech",
            descripcion = "Chaqueta impermeable reflectante de alta resistencia",
            categoria = CategoriaProducto.HOMBRE,
            tipo = TipoProducto.CHAQUETA,
            precio = 54990,
            stockActual = 8,
            stockMinimo = 3,
            detalle = "Cierre termosellado y capuchón ajustable"
        )
        productoDao.insertar(p2)

        movimientoDao.insertarMovimiento(
            Movimiento(
                id = UUID.randomUUID().toString(),
                codigoProducto = p2.codigo,
                tipoMovimiento = TipoMovimiento.ENTRADA,
                cantidad = 10,
                motivo = "Carga inicial de inventario",
                fecha = ahora.minusDays(4),
                usuario = "admin@kaiju.cl"
            )
        )
        movimientoDao.insertarMovimiento(
            Movimiento(
                id = UUID.randomUUID().toString(),
                codigoProducto = p2.codigo,
                tipoMovimiento = TipoMovimiento.SALIDA_CONSUMO,
                cantidad = 2,
                motivo = "Uso interno para exhibición en vitrina",
                fecha = ahora.minusDays(1),
                usuario = "encargado_inventario"
            )
        )

        val p3 = Producto(
            codigo = "KAI-003",
            nombre = "Zapatillas Street Runner",
            descripcion = "Calzado urbano suela alta ergonómico",
            categoria = CategoriaProducto.MUJER,
            tipo = TipoProducto.CALZADO,
            precio = 39990,
            stockActual = 2,
            stockMinimo = 5,
            detalle = "Edición limitada negra/roja - Talla 38"
        )
        productoDao.insertar(p3)

        movimientoDao.insertarMovimiento(
            Movimiento(
                id = UUID.randomUUID().toString(),
                codigoProducto = p3.codigo,
                tipoMovimiento = TipoMovimiento.ENTRADA,
                cantidad = 5,
                motivo = "Ingreso de lote prueba",
                fecha = ahora.minusDays(6),
                usuario = "admin@kaiju.cl"
            )
        )
        movimientoDao.insertarMovimiento(
            Movimiento(
                id = UUID.randomUUID().toString(),
                codigoProducto = p3.codigo,
                tipoMovimiento = TipoMovimiento.SALIDA_VENTA,
                cantidad = 3,
                motivo = "Venta online pedido #8821",
                fecha = ahora.minusHours(8),
                usuario = "vendedor2"
            )
        )

        val p4 = Producto(
            codigo = "KAI-004",
            nombre = "Polerón Kaiju Hooded",
            descripcion = "Polerón térmico con bolsillo canguro y bordado",
            categoria = CategoriaProducto.COLECCION_ESPECIAL,
            tipo = TipoProducto.POLERON,
            precio = 32990,
            stockActual = 12,
            stockMinimo = 4,
            detalle = "Interior afelpado de alta densidad"
        )
        productoDao.insertar(p4)

        movimientoDao.insertarMovimiento(
            Movimiento(
                id = UUID.randomUUID().toString(),
                codigoProducto = p4.codigo,
                tipoMovimiento = TipoMovimiento.ENTRADA,
                cantidad = 15,
                motivo = "Llegada de pedido de fábrica",
                fecha = ahora.minusDays(3),
                usuario = "admin@kaiju.cl"
            )
        )
        movimientoDao.insertarMovimiento(
            Movimiento(
                id = UUID.randomUUID().toString(),
                codigoProducto = p4.codigo,
                tipoMovimiento = TipoMovimiento.AJUSTE,
                cantidad = 3,
                motivo = "Ajuste por prendas defectuosas en empaque",
                fecha = ahora.minusHours(2),
                usuario = "encargado_inventario"
            )
        )
    }
}
