package com.kaiju.app.remote

import com.kaiju.app.model.LoginRequest
import com.kaiju.app.model.LoginResponse
import com.kaiju.app.model.Usuario
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface UsuarioApi {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("auth/registro")
    suspend fun registrarUsuario(@Body usuario: Usuario): Response<Usuario>

    @GET("usuarios")
    suspend fun listarUsuarios(@Header("Authorization") token: String
    ): Response<List<Usuario>>

    @GET("usuarios/{id}")
    suspend fun obtenerUsuario(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<Usuario>

    @PUT("usuarios/{id}")
    suspend fun actualizarUsuario(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Body usuario: Usuario
    ): Response<Usuario>

    @DELETE("usuarios/{id}")
    suspend fun eliminarUsuario(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<Unit>

}
