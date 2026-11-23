package com.kaiju.app.repository

import com.kaiju.app.model.LoginRequest
import com.kaiju.app.model.LoginResponse
import com.kaiju.app.model.Usuario
import com.kaiju.app.remote.UsuarioApi

class UsuarioRepository(private val api: UsuarioApi) {

    suspend fun login(request: LoginRequest): Result<LoginResponse> {
        return try {
            val response = api.login(request)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Credenciales incorrectas o error en el servidor"))
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

    suspend fun crearUsuario(usuario: Usuario): Result<Usuario> {
        return try {
            val response = api.registrarUsuario(usuario)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error al crear usuario. Posible correo o RUN duplicado."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun actualizarUsuario(token: String, id: Int, usuario: Usuario): Result<Usuario> {
        return try {
            val response = api.actualizarUsuario(token, id, usuario)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error al actualizar usuario."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }



    suspend fun eliminarUsuario(token: String, id: Int): Result<Unit> {
        return try {
            val response = api.eliminarUsuario("Bearer $token", id)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("No se pudo eliminar el usuario"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}