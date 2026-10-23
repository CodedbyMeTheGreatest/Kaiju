package com.kaiju.app.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kaiju.app.model.Movimiento
import com.kaiju.app.model.Producto

@Database(entities = [Producto::class, Movimiento::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class TiendaDatabase : RoomDatabase() {

    abstract fun productoDao(): ProductoDao
    abstract fun movimientoDao(): MovimientoDao


    companion object{
        @Volatile private var instancia: TiendaDatabase? = null
            fun obtenerInstancia(contexto: Context): TiendaDatabase {
                return instancia ?: synchronized(this) {
                    val instance = Room.databaseBuilder(
                        contexto.applicationContext,
                        TiendaDatabase::class.java,
                        "tienda.db"
                    )
                    .fallbackToDestructiveMigration(true)
                    .build()
                    instancia = instance
                    instance
            }
        }
    }
}
