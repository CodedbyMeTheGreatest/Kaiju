package com.kaiju.app.repository

import android.content.Context

class AlmacenSesion(context: Context) {

    private val prefs = context.applicationContext
        .getSharedPreferences("sesion", Context.MODE_PRIVATE)

    fun leerIdUsuario(): String? =
        prefs.getString(CLAVE_USUARIO, null)

    fun guardar(idUsuario: String) {
        prefs.edit().putString(CLAVE_USUARIO, idUsuario).apply()
    }

    fun borrar() {
        prefs.edit().remove(CLAVE_USUARIO).apply()
    }

    private companion object {
        const val CLAVE_USUARIO = "id_usuario"
    }
}