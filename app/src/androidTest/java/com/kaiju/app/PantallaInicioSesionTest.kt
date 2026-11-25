package com.kaiju.app

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.kaiju.app.model.LoginRequest
import com.kaiju.app.model.LoginResponse
import com.kaiju.app.model.RolUsuario
import com.kaiju.app.model.Usuario
import com.kaiju.app.model.UsuarioResumen
import com.kaiju.app.remote.UsuarioApi
import com.kaiju.app.repository.UsuarioRepository
import com.kaiju.app.ui.screen.PantallaInicioSesionEvento
import com.kaiju.app.viewmodel.InicioSesionViewModel
import junit.framework.TestCase.assertEquals
import org.junit.Rule
import org.junit.Test
import retrofit2.Response

class PantallaInicioSesionTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private fun crearApiFalsa(respuestaLogin: Response<LoginResponse>): UsuarioApi {
        return object : UsuarioApi {
            override suspend fun login(request: LoginRequest): Response<LoginResponse> = respuestaLogin
            override suspend fun crearUsuario(
                token: String,
                usuario: Usuario
            ): Response<Usuario> {
                TODO("Not yet implemented")
            }

            override suspend fun listarUsuarios(token: String): Response<List<Usuario>> {
                TODO("Not yet implemented")
            }

            override suspend fun obtenerUsuario(
                token: String,
                id: Int
            ): Response<Usuario> {
                TODO("Not yet implemented")
            }

            override suspend fun actualizarUsuario(
                token: String,
                id: Int,
                usuario: Usuario
            ): Response<Usuario> {
                TODO("Not yet implemented")
            }

            override suspend fun eliminarUsuario(
                token: String,
                id: Int
            ): Response<Unit> {
                TODO("Not yet implemented")
            }
        }
    }

    @Test
    fun alPresionarIniciarSesionConCamposVacios_muestraMensajesDeErrorEnPantalla() {
        val apiFalsa = crearApiFalsa(
            Response.success(
                LoginResponse("ok", "token", UsuarioResumen(1, "Admin", RolUsuario.ADMINISTRADOR))
            )
        )
        val viewModel = InicioSesionViewModel(UsuarioRepository(apiFalsa))

        composeTestRule.setContent {
            PantallaInicioSesionEvento(viewModel = viewModel,
                onLoginExitoso = {_, _ -> })
        }

        composeTestRule.onNodeWithText("Iniciar Sesión").performClick()

        composeTestRule.onNodeWithText("El correo es obligatorio").assertIsDisplayed()
        composeTestRule.onNodeWithText("La contraseña es obligatoria").assertIsDisplayed()
    }

    @Test
    fun alIngresarCredencialesValidas_ejecutaOnLoginExitoso() {
        val usuarioEsperado = UsuarioResumen(1, "Admin Kaiju", RolUsuario.ADMINISTRADOR)
        val apiFalsa = crearApiFalsa(Response.success(LoginResponse("ok", "token-emulador", usuarioEsperado)))
        val viewModel = InicioSesionViewModel(UsuarioRepository((apiFalsa)))

        var tokenRecibido = ""
        var usuarioRecibido: UsuarioResumen? = null

        composeTestRule.setContent {
            PantallaInicioSesionEvento(
                viewModel = viewModel,
                onLoginExitoso = {
                    token, usuario ->
                    tokenRecibido = token
                    usuarioRecibido = usuario
                }
            )
        }

        composeTestRule.onNodeWithText("Correo").performTextInput("admin@kaiju.cl")
        composeTestRule.onNodeWithText("Contraseña").performTextInput("123456")
        composeTestRule.onNodeWithText("Iniciar Sesión").performClick()

        composeTestRule.waitForIdle()

        assertEquals("token-emulador", tokenRecibido)
        assertEquals(usuarioEsperado, usuarioRecibido)

    }

}