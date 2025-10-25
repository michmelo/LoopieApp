package com.example.loopieapp.Data

import android.content.Context
import android.net.Uri

class PreferenciasUsuario(context: Context) {

    // archivo de preferencias
    private val prefs = context.getSharedPreferences("LoopiePrefs", Context.MODE_PRIVATE)

    // guardar la URI
    private val KEY_IMAGEN_PERFIL = "imagen_perfil_uri"

    fun guardarImagenPerfil(uri: Uri?) {
        // Guardamos la URI como un String. Si es nula, guardamos un string vacío.
        prefs.edit().putString(KEY_IMAGEN_PERFIL, uri?.toString() ?: "").apply()
    }

    fun obtenerImagenPerfil(): Uri? {
        // Leemos el String de la URI.
        val uriString = prefs.getString(KEY_IMAGEN_PERFIL, null)
        // Si no es nulo ni está vacío, lo convertimos de nuevo a Uri.
        return if (uriString.isNullOrEmpty()) {
            null
        } else {
            Uri.parse(uriString)
        }
    }
}
