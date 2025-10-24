package com.example.loopieapp.Model

data class Producto(
    val idProducto: String = "",
    val nombre: String = "",
    val descripcion: String = "",
    val precio: Double = 0.0,
    val categoria: String = "",
    val stock: Int = 0,
    val imagen: String = ""
)
