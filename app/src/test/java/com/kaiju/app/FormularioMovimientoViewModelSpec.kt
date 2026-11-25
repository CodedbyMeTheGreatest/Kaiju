package com.kaiju.app

import com.kaiju.app.model.CategoriaProducto
import com.kaiju.app.model.Producto
import com.kaiju.app.model.TipoMovimiento
import com.kaiju.app.model.TipoProducto
import com.kaiju.app.repository.MovimientoRepository
import com.kaiju.app.repository.ProductoRepository
import com.kaiju.app.viewmodel.FormularioMovimientoViewModel
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain

@OptIn(ExperimentalCoroutinesApi::class)
class FormularioMovimientoViewModelSpec : FunSpec({

    val dispatcher = StandardTestDispatcher()

    beforeTest { Dispatchers.setMain(dispatcher) }
    afterTest { Dispatchers.resetMain() }

    val productoEjemplo = Producto(
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

    test("una salida mayor al stock disponible falla la validación y no registra el movimiento") {
        val movimientoRepo = mockk<MovimientoRepository>()
        val productoRepo = mockk<ProductoRepository>()
        every { productoRepo.productos } returns flowOf(listOf(productoEjemplo))

        runTest(dispatcher) {
            val vm = FormularioMovimientoViewModel(movimientoRepo, productoRepo)
            advanceUntilIdle()

            vm.actualizarProducto("P001")
            vm.actualizarTipo(TipoMovimiento.SALIDA_VENTA)
            vm.actualizarCantidad("15")

            val guardado = vm.guardarMovimiento("Encargado")

            guardado shouldBe false
            vm.estado.value.errores.errorCantidad shouldBe
                    "El stock no puede quedar bajo cero (disponible: 10)"
            coVerify(exactly = 0) { movimientoRepo.registrarMovimiento(any(), any()) }
        }
    }

    test("un ajuste de inventario sin motivo muestra error y no registra el movimiento") {
        val movimientoRepo = mockk<MovimientoRepository>()
        val productoRepo = mockk<ProductoRepository>()
        every { productoRepo.productos } returns flowOf(listOf(productoEjemplo))

        runTest(dispatcher) {
            val vm = FormularioMovimientoViewModel(movimientoRepo, productoRepo)
            advanceUntilIdle()

            vm.actualizarProducto("P001")
            vm.actualizarTipo(TipoMovimiento.AJUSTE)
            vm.actualizarCantidad("2")
            vm.actualizarMotivo("   ")

            val guardado = vm.guardarMovimiento("Encargado")

            guardado shouldBe false
            vm.estado.value.errores.errorMotivo shouldBe "Todo ajuste exige un motivo"
            coVerify(exactly = 0) { movimientoRepo.registrarMovimiento(any(), any()) }
        }
    }

    test("una salida válida calcula el nuevo stock, registra en el repositorio y limpia el formulario") {
        val movimientoRepo = mockk<MovimientoRepository>()
        val productoRepo = mockk<ProductoRepository>()
        every { productoRepo.productos } returns flowOf(listOf(productoEjemplo))
        coEvery { movimientoRepo.registrarMovimiento(any(), any()) } returns Unit

        runTest(dispatcher) {
            val vm = FormularioMovimientoViewModel(movimientoRepo, productoRepo)
            advanceUntilIdle()

            vm.actualizarProducto("P001")
            vm.actualizarTipo(TipoMovimiento.SALIDA_VENTA)
            vm.actualizarCantidad("3") // 10 - 3 = 7

            val guardado = vm.guardarMovimiento("Vendedor")
            advanceUntilIdle()

            guardado shouldBe true
            coVerify(exactly = 1) { movimientoRepo.registrarMovimiento(any(), 7) }
            vm.estado.value.codigoProducto shouldBe ""
            vm.estado.value.cantidad shouldBe ""
        }
    }
})