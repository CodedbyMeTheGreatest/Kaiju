package com.kaiju.app.model

import com.google.gson.annotations.SerializedName

enum class RolUsuario {
    @SerializedName("Administrador")
    ADMINISTRADOR,
    @SerializedName("Vendedor")
    VENDEDOR,
    @SerializedName("Encargado de inventario")
    ENCARGADO_INVENTARIO
}