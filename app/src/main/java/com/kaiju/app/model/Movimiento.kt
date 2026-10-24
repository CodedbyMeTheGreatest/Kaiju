package com.kaiju.app.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "movimientos",foreignKeys = [ForeignKey(entity = Producto::class,
    parentColumns = ["codigo"], childColumns = ["codigoProducto"],
    onDelete = ForeignKey.RESTRICT
)], indices = [Index("codigoProducto")])
data class Movimiento (
    @PrimaryKey val id: String,
    val codigoProducto: String,
    val tipoMovimiento: TipoMovimiento,
    val cantidad: Int,
    val motivo: String = "",
    val fecha: LocalDateTime,
    val usuario: String

)