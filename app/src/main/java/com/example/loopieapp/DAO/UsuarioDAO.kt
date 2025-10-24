package com.example.loopieapp.DAO

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.loopieapp.Model.Usuario

@Dao
interface UsuarioDAO {
    @Query("SELECT * FROM usuarios ORDER BY id DESC")
    suspend fun obtenerUsuarios(): List<Usuario>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(usuario: Usuario)
    @Delete
    suspend fun eliminar(usuario: Usuario)
}