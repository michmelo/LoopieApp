package com.example.loopieapp.ViewModel

data class ProductoUIState(
    val nombre: String = "",
    val descripcion: String = "",
    val precio: String = "",
    val categoria: String = "",
    val stock: String = "",
    val imagen: String = "",
    val errores: ProductoErrores = ProductoErrores(),
    val isLoading: Boolean = false,
    val isEditing: Boolean = false
)
