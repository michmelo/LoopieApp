package com.example.loopieapp.Model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class Producto(
    @PrimaryKey(autoGenerate = true)
    val idProducto: Int = 0,
    val nombre: String,
    val descripcion: String,
    val precio: Double = 0.0,
    val categoria: String,
    val stock: Int = 0,
    val rating: Float,
    val imagen: String
)
