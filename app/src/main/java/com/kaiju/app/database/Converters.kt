package com.kaiju.app.database

import androidx.room.TypeConverter
import com.kaiju.app.model.CategoriaProducto
import com.kaiju.app.model.TipoMovimiento
import com.kaiju.app.model.TipoProducto

class Converters {
    @TypeConverter
    fun fromCategoria(value: CategoriaProducto?): String? = value?.name

    @TypeConverter
    fun toCategoria(value: String?): CategoriaProducto? = value?.let { enumValueOf<CategoriaProducto>(it) }

    @TypeConverter
    fun fromTipoProducto(value: TipoProducto?): String? = value?.name

    @TypeConverter
    fun toTipoProducto(value: String?): TipoProducto? = value?.let { enumValueOf<TipoProducto>(it) }

    @TypeConverter
    fun fromTipoMovimiento(value: TipoMovimiento?): String? = value?.name

    @TypeConverter
    fun toTipoMovimiento(value: String?): TipoMovimiento? = value?.let { enumValueOf<TipoMovimiento>(it) }
}