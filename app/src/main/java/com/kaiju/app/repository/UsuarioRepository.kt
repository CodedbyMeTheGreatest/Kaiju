package com.kaiju.app.repository

import com.google.gson.JsonParser
import com.kaiju.app.model.LoginRequest
import com.kaiju.app.model.LoginResponse
import com.kaiju.app.model.Usuario
import com.kaiju.app.remote.UsuarioApi
import retrofit2.Response

class UsuarioRepository(private val api: UsuarioApi) {

    suspend fun login(request: LoginRequest): Result<LoginResponse> {
        return try {
            val response = api.login(request)
            val cuerpo = response.body()
            if (response.isSuccessful && cuerpo!= null) {
                if (cuerpo.usuario.tipo == null){
                    return Result.failure(Exception("Este perfil no tiene acceso a Kaiju"))
                }
                Result.success(cuerpo)
            } else {
                Result.failure(Exception(mensajeDeError(response,"Credenciales incorrectas o error en el servidor")))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun listarUsuarios(token: String): Result<List<Usuario>> {
        return try {
            val response = api.listarUsuarios("Bearer $token")
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("No se pudieron cargar los usuarios: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun obtenerUsuario(token: String, id: Int): Result<Usuario> {
        return try {
            val response = api.obtenerUsuario("Bearer $token", id)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("No se encontro al usuario con ID: ${id}: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun crearUsuario(token: String, usuario: Usuario): Result<Usuario> {
        return try {
            val response = api.crearUsuario("Bearer $token", usuario)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error al crear usuario."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun actualizarUsuario(token: String, id: Int, usuario: Usuario): Result<Usuario> {
        return try {
            val response = api.actualizarUsuario("Bearer $token", id, usuario)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception(mensajeDeError(response,"Error al actualizar usuario.")))
            }
        } catch (e: Exception) {
            Result.failure(Exception("No se pudo conectar con el servidor."))
        }
    }

    suspend fun eliminarUsuario(token: String, id: Int): Result<Unit> {
        return try {
            val response = api.eliminarUsuario("Bearer $token", id)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception(mensajeDeError(response,"No se pudo eliminar el usuario")))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun mensajeDeError(response: Response<*>, porDefecto: String): String =
        try {
            JsonParser.parseString(response.errorBody()?.string())
                .asJsonObject.get("error").asString
        } catch (e: Exception) { porDefecto }
}