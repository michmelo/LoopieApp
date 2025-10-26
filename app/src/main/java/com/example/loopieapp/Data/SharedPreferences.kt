package com.example.loopieapp.Data

import android.content.Context
import android.net.Uri

class PreferenciasUsuario(context: Context) {

    // archivo de preferencias
    private val prefs = context.getSharedPreferences("LoopiePrefs", Context.MODE_PRIVATE)
    private val editor = prefs.edit()

    companion object{
        const val KEY_IMAGEN_PERFIL = "imagen_perfil_uri"
        const val KEY_USUARIO_ACTIVO_CORREO = "usuario_activo_correo"
    }

    fun guardarImagenPerfil(uri: Uri?) {
        prefs.edit().putString(KEY_IMAGEN_PERFIL, uri?.toString() ?: "").apply()
    }

    fun obtenerImagenPerfil(): Uri? {
        // Leemos el String de la URI.
        val uriString = prefs.getString(KEY_IMAGEN_PERFIL, null)
        // Si no es nulo ni está vacío, lo convertimos de nuevo a Uri.
        return if (uriString.isNullOrEmpty()) null else Uri.parse(uriString)
    }

    fun guardarCorreoUsuarioActivo(correo: String?) {
        prefs.edit().putString(KEY_USUARIO_ACTIVO_CORREO, correo).apply()
    }

    fun obtenerCorreoUsuarioActivo(): String? {
        // Devuelve el correo guardado, o null si no hay ninguno.
        return prefs.getString(KEY_USUARIO_ACTIVO_CORREO, null)
    }
}
