package com.example.loopieapp.DAO

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.loopieapp.Model.Producto

@Dao
interface ProductoDao {

    // Obtiene todos los productos de la tabla, ordenados por ID
    @Query("SELECT * FROM products ORDER BY idProducto ASC")
    suspend fun obtenerProductos(): List<Producto>

    // Inserta un nuevo producto. Si ya existe, lo reemplaza.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(producto: Producto)

    // Actualiza un producto existente
    @Update
    suspend fun actualizar(producto: Producto)

    // Elimina un producto
    @Delete
    suspend fun eliminar(producto: Producto)
}