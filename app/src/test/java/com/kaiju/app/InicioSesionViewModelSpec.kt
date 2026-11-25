package com.kaiju.app

import com.kaiju.app.model.LoginRequest
import com.kaiju.app.model.LoginResponse
import com.kaiju.app.model.RolUsuario
import com.kaiju.app.model.UsuarioResumen
import com.kaiju.app.repository.UsuarioRepository
import com.kaiju.app.viewmodel.InicioSesionViewModel
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain

@OptIn(ExperimentalCoroutinesApi::class)
class InicioSesionViewModelSpec : FunSpec(
    {

        val dispatcher = StandardTestDispatcher()

        beforeTest { Dispatchers.setMain(dispatcher) }
        afterTest { Dispatchers.resetMain() }

        val usuarioEjemplo = UsuarioResumen(
            id = 1,
            nombre = "Admin Kaiju",
            tipo = RolUsuario.ADMINISTRADOR,
        )
        val respuestaExitosa = LoginResponse(
            mensaje = "Login exitoso",
            token = "jwt-token-falso-123",
            usuario = usuarioEjemplo,
        )

        test("si correo y contraseña están vacíos, muestra errores y no llama al repositorio") {
            val repo = mockk<UsuarioRepository>()
            val vm = InicioSesionViewModel(repo)
            var onSuccessLlamado = false

            vm.InicioSesion { _, _ -> onSuccessLlamado = true }

            val estado = vm.estadoFormulario.value
            estado.errores.errorEmail shouldBe "El correo es obligatorio"
            estado.errores.errorContrasenia shouldBe "La contraseña es obligatoria"
            onSuccessLlamado shouldBe false
            coVerify(exactly = 0) { repo.login(any()) }
        }

        test("con credenciales válidas, entrega token y usuario, limpia el formulario y detiene la carga") {
            val repo = mockk<UsuarioRepository>()
            val requestEsperado = LoginRequest("admin@kaiju.cl", "123456")
            coEvery { repo.login(requestEsperado) } returns Result.success(respuestaExitosa)

            val vm = InicioSesionViewModel(repo)
            var tokenRecibido: String? = null
            var usuarioRecibido: UsuarioResumen? = null

            runTest(dispatcher) {
                vm.actualizarEmail("admin@kaiju.cl")
                vm.actualizarContrasenia("123456")
                vm.InicioSesion { token, usuario ->
                    tokenRecibido = token
                    usuarioRecibido = usuario
                }

                vm.cargando.value shouldBe true

                advanceUntilIdle()
            }

            vm.cargando.value shouldBe false
            tokenRecibido shouldBe "jwt-token-falso-123"
            usuarioRecibido shouldBe usuarioEjemplo
            vm.estadoFormulario.value.email shouldBe ""
            vm.estadoFormulario.value.contrasenia shouldBe ""
            coVerify(exactly = 1) { repo.login(requestEsperado) }
        }

        test("si el repositorio falla, detiene la carga y muestra el error en contraseña") {
            val repo = mockk<UsuarioRepository>()
            coEvery { repo.login(any()) } returns Result.failure(Exception("Credenciales inválidas"))

            val vm = InicioSesionViewModel(repo)
            var onSuccessLlamado = false

            runTest(dispatcher) {
                vm.actualizarEmail("admin@kaiju.cl")
                vm.actualizarContrasenia("clave-mal")
                vm.InicioSesion { _, _ -> onSuccessLlamado = true }
                advanceUntilIdle()
            }

            vm.cargando.value shouldBe false
            onSuccessLlamado shouldBe false
            vm.estadoFormulario.value.errores.errorEmail.shouldBeNull()
            vm.estadoFormulario.value.errores.errorContrasenia shouldBe "Credenciales inválidas"
        }
    },
)