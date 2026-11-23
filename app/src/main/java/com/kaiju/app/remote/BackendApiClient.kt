package com.kaiju.app.remote

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object BackendApiClient {
    //Cambiar 10.0.2.2 -> IP PUBLICA INSTANCIA BACKEND
    private const val baseUrl = "http://10.0.2.2:3000/api/v1/"

    val api: UsuarioApi by lazy {
        val clienteHttp = OkHttpClient.Builder()
            .callTimeout(20, TimeUnit.SECONDS)
            .build()

        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(clienteHttp)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UsuarioApi::class.java)
    }
}