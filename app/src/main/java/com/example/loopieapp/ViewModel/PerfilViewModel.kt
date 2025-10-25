package com.example.loopieapp.ViewModel

import android.app.Application
import android.content.ContentResolver
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loopieapp.Data.PreferenciasUsuario
import com.example.loopieapp.Repository.PerfilRepositorio
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

class PerfilViewModel (application: Application) : AndroidViewModel(application) {
    private val preferencias = PreferenciasUsuario(application)
    private val _imagenUri = MutableStateFlow<Uri?>(preferencias.obtenerImagenPerfil())
    val imagenUri = _imagenUri.asStateFlow()

    fun setImage(uri: Uri?) {
        if (uri == null) {
            _imagenUri.value = null
            preferencias.guardarImagenPerfil(null)
            return
        }
        val uriPersistente = guardarCopiaLocal(uri)

        _imagenUri.value = uriPersistente
        preferencias.guardarImagenPerfil(uriPersistente)
    }

    private fun guardarCopiaLocal(uri: Uri): Uri {
        val application = getApplication<Application>()
        val contentResolver: ContentResolver = application.contentResolver

        // Crea un nombre de archivo único
        val nombreArchivo = "perfil_${System.currentTimeMillis()}.jpg"
        val archivoDestino = File(application.filesDir, nombreArchivo)

        try {
            // Abre el stream de la URI original (de galería o cámara)
            contentResolver.openInputStream(uri)?.use { inputStream ->
                // Abre el stream del archivo de destino en el almacenamiento interno
                FileOutputStream(archivoDestino).use { outputStream ->
                    // Copia los datos
                    inputStream.copyTo(outputStream)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            // Si falla, devuelve la URI original
            return uri
        }

        // Devuelve la URI del nuevo archivo guardado, que es persistente
        return Uri.fromFile(archivoDestino)
    }
}

