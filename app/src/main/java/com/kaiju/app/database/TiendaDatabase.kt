package com.kaiju.app.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.kaiju.app.model.Producto

@Database(entities = [Producto::class], version = 1, exportSchema = false)
abstract class TiendaDatabase : RoomDatabase() {

    abstract fun productoDao(): ProductoDao


    companion object{
        @Volatile private var instancia: TiendaDatabase? = null

        fun obtenerInstancia(contexto: Context): TiendaDatabase =
            instancia ?: synchronized(this) {
                instancia ?: Room.databaseBuilder(
                    contexto.applicationContext,
                    TiendaDatabase::class.java,
                    "tienda.db"
                ).build().also { instancia = it }
            }
    }
}