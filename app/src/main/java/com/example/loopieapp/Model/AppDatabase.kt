package com.example.loopieapp.Model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.loopieapp.DAO.ProductoDao
import com.example.loopieapp.DAO.UsuarioDAO

@Database(entities = [Usuario::class, Producto::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun usuarioDao(): UsuarioDAO
    abstract fun productoDao(): ProductoDao

    companion object{
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "db_loopie"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}