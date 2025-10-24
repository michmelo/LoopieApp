package com.example.loopieapp.Repository

import android.net.Uri
import com.example.loopieapp.Model.PerfilDeUsuario

class PerfilRepositorio {
    private var perfilActual = PerfilDeUsuario(1, "Usuario", null)
    fun getProfile(): PerfilDeUsuario = perfilActual
    fun updateImage(uri: Uri?) {
        perfilActual = perfilActual.copy(imagenUri = uri)
    }
}

//• Repositorio simple en memoria para centralizar lectura/actualización del perfil.
//• copy() preserva el resto de campos, modificando solo la imagen
