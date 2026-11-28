package com.kaiju.app

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.kaiju.app.database.MovimientoDao
import com.kaiju.app.database.ProductoDao
import com.kaiju.app.model.CategoriaProducto
import com.kaiju.app.model.Movimiento
import com.kaiju.app.model.Producto
import com.kaiju.app.model.TipoMovimiento
import com.kaiju.app.model.TipoProducto
import com.kaiju.app.repository.MovimientoRepository
import com.kaiju.app.repository.ProductoRepository
import com.kaiju.app.ui.screen.PantallaMovimiento
import com.kaiju.app.viewmodel.FormularioMovimientoViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.junit.Rule
import org.junit.Test

class PantallaMovimientoTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val productoEjemplo = Producto(
        id = 1,
        codigo = "P001",
        nombre = "Polera Pikachu",
        descripcion = "Polera algodón",
        categoria = CategoriaProducto.UNISEX,
        tipo = TipoProducto.POLERA,
        precio = 15990,
        stockActual = 10,
        stockMinimo = 3,
        detalle = "Talla M"
    )

    private fun crearViewModelFalso(): FormularioMovimientoViewModel {
        val movimientoDaoFalso = object : MovimientoDao() {
            override fun obtenerMovimientos(): Flow<List<Movimiento>> = flowOf(emptyList())
            override fun obtenerHistorial(codigo: String): Flow<List<Movimiento>> = flowOf(emptyList())
            override suspend fun insertarMovimiento(movimiento: Movimiento) {
                TODO("Not yet implemented")
            }

            override suspend fun actualizarStock(codigo: String, nuevoStock: Int) {
                TODO("Not yet implemented")
            }

            override suspend fun registrarMovimiento(movimiento: Movimiento, nuevoStock: Int) {}
        }

        val productoDaoFalso = object : ProductoDao {
            fun obtenerProductos(): Flow<List<Producto>> = flowOf(listOf(productoEjemplo))
            override suspend fun contar(): Int {
                TODO("Not yet implemented")
            }

            override fun obtenerTodos(): Flow<List<Producto>> = flowOf(emptyList())

            override fun obtenerPorCodigo(codigo: String): Flow<Producto?> {
                TODO("Not yet implemented")
            }

            override fun buscarProducto(query: String): Flow<List<Producto>> {
                TODO("Not yet implemented")
            }

            override suspend fun insertar(producto: Producto) {
                TODO("Not yet implemented")
            }
        }

        return FormularioMovimientoViewModel(
            movimientoRepository = MovimientoRepository(movimientoDaoFalso),
            productoRepository = ProductoRepository(productoDaoFalso)
        )
    }

    @Test
    fun alGuardarMovimientoVacio_muestraErroresDeValidacionEnPantalla() {
        val viewModel = crearViewModelFalso()

        composeTestRule.setContent {
            PantallaMovimiento(
                viewModel = viewModel,
                nombreUsuario = "Encargado"
            )
        }

        composeTestRule.onNodeWithText("Registrar Movimiento").performClick()

        composeTestRule.onNodeWithText("Elige un producto").assertIsDisplayed()
        composeTestRule.onNodeWithText("Elige un tipo de movimiento").assertIsDisplayed()
        composeTestRule.onNodeWithText("Ingresa un número entero").assertIsDisplayed()
    }

    @Test
    fun siSoloTieneUnTipoPermitido_loAutoseleccionaEnPantalla() {
        val viewModel = crearViewModelFalso()

        composeTestRule.setContent {
            PantallaMovimiento(
                viewModel = viewModel,
                nombreUsuario = "Vendedor",
                tiposPermitidos = listOf(TipoMovimiento.SALIDA_VENTA)
            )
        }

        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("Salida por venta").assertIsDisplayed()
    }
}