package com.example.loopieapp.Model

import android.net.Uri

data class PerfilDeUsuario(
    val id: Int,
    val nombre: String,
    val imagenUri: Uri? = null //referencia a la ubicación de la foto seleccionada o capturada.
)