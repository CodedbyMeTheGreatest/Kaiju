package com.kaiju.app.repository

import android.content.Context
import com.kaiju.app.model.RolUsuario
import com.kaiju.app.model.UsuarioResumen

class AlmacenSesion(context: Context) {

    private val prefs = context.applicationContext
        .getSharedPreferences("sesion", Context.MODE_PRIVATE)

    fun leerToken(): String? = prefs.getString(CLAVE_TOKEN, null)

    fun leerUsuario(): UsuarioResumen? {
        val id = prefs.getInt(CLAVE_ID, -1)
        val nombre = prefs.getString(CLAVE_NOMBRE, null)
        val tipoString = prefs.getString(CLAVE_TIPO, null)

        if (id == -1 || nombre == null || tipoString == null) return null

        return UsuarioResumen(
            id = id,
            nombre = nombre,
            tipo = try {
                RolUsuario.valueOf(tipoString)
            } catch (e: IllegalArgumentException) {
                return null
            }
        )
    }

    fun guardarSesion(token: String, usuario: UsuarioResumen) {
        prefs.edit()
            .putString(CLAVE_TOKEN, token)
            .putInt(CLAVE_ID, usuario.id)
            .putString(CLAVE_NOMBRE, usuario.nombre)
            .putString(CLAVE_TIPO, usuario.tipo.name)
            .apply()
    }

    fun borrar() {
        prefs.edit().clear().apply()
    }

    private companion object {
        const val CLAVE_TOKEN = "jwt_token"
        const val CLAVE_ID = "usuario_id"
        const val CLAVE_NOMBRE = "usuario_nombre"
        const val CLAVE_TIPO = "usuario_tipo"
    }
}