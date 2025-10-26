package com.example.loopieapp.ViewModel

import UsuarioErrores
import UsuarioUIState
import android.app.Application
import android.content.ContentResolver
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.loopieapp.Model.Usuario
import com.example.loopieapp.Repository.UsuarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import androidx.lifecycle.viewModelScope
import java.io.File
import java.io.FileOutputStream
import androidx.lifecycle.ViewModel
import com.example.loopieapp.Data.PreferenciasUsuario
import com.example.loopieapp.Model.AppDatabase

class UsuarioViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: UsuarioRepository
    private val preferencias: PreferenciasUsuario

    private val _estado = MutableStateFlow(UsuarioUIState())
    val estado = _estado.asStateFlow()

    private val _usuarioActivo = MutableStateFlow<Usuario?>(null)
    val usuarioActivo = _usuarioActivo.asStateFlow()

    private val _isLoading = MutableStateFlow(true) // Empieza como 'true' porque estamos cargando
    val isLoading = _isLoading.asStateFlow()

    init {
        // Obtenemos el DAO y creamos el repositorio DENTRO del init.
        val usuarioDao = AppDatabase.getDatabase(application).usuarioDao()
        repository = UsuarioRepository(usuarioDao)
        preferencias = PreferenciasUsuario(application)

        viewModelScope.launch {
            val correoGuardado = preferencias.obtenerCorreoUsuarioActivo()
            if (correoGuardado.isNullOrBlank()) {
                _isLoading.value = false
            } else{
                val usuarioEncontrado = repository.obtenerUsuarios().find { it.correo == correoGuardado }
                _usuarioActivo.value = usuarioEncontrado
                _isLoading.value = false
            }
        }
    }

    //Funciones para el formualario
    fun onNombreChange(valor: String) {
        _estado.update { it.copy(nombre = valor, errores = it.errores.copy(nombre = null)) }
    }

    fun onApellidoChange(valor: String) {
        _estado.update { it.copy(apellido = valor, errores = it.errores.copy(apellido = null)) }
    }

    fun onCorreoChange(valor: String) {
        _estado.update { it.copy(correo = valor, errores = it.errores.copy(correo = null)) }
    }

    fun onClaveChange(valor: String) {
        _estado.update { it.copy(clave = valor, errores = it.errores.copy(clave = null)) }
    }

    fun onConfirmarClaveChange(valor: String) {
        _estado.update {
            it.copy(
                confirmarClave = valor,
                errores = it.errores.copy(confirmarClave = null)
            )
        }
    }

    fun onDireccionChange(valor: String) {
        _estado.update { it.copy(direccion = valor, errores = it.errores.copy(direccion = null)) }
    }

    fun onAceptaTerminosChange(valor: Boolean) {
        _estado.update { it.copy(aceptaTerminos = valor) }
    }

    // Validaciones formulario
    fun validarFormulario(): Boolean {
        val estadoActual = _estado.value

        _estado.update { it.copy(errores = UsuarioErrores()) }

        if (estadoActual.nombre.isBlank()) {
            _estado.update { it.copy(errores = it.errores.copy(nombre = "Debe ingresar un nombre")) }
            return false
        }
        if (estadoActual.apellido.isBlank()) {
            _estado.update { it.copy(errores = it.errores.copy(apellido = "Debe ingresar un apellido")) }
            return false
        }
        if (!estadoActual.correo.contains("@")) {
            _estado.update { it.copy(errores = it.errores.copy(correo = "Correo inválido")) }
            return false
        }
        if (estadoActual.clave.length < 8) {
            _estado.update { it.copy(errores = it.errores.copy(clave = "Contraseña debe tener al menos 8 carácteres")) }
            return false
        }
        if (estadoActual.clave != estadoActual.confirmarClave) {
            _estado.update {
                it.copy(errores = it.errores.copy(confirmarClave = "Las contraseñas no coinciden"))
            }
            return false
        }
        if (estadoActual.direccion.isBlank()) {
            _estado.update { it.copy(errores = it.errores.copy(direccion = "Debe ingresar una dirección")) }
            return false
        }
        if (!estadoActual.aceptaTerminos) {
            _estado.update { it.copy(errores = it.errores.copy(aceptaTerminos = "Debes aceptar los términos y condiciones")) }
            return false
        }
        _estado.update { it.copy(errores = UsuarioErrores()) }
        return true
    }

    //Validaciones inicio sesion
    fun validarInicioSesion(): Boolean {
        val estadoActual = _estado.value
        _estado.update { it.copy(errores = UsuarioErrores()) }

        if (!estadoActual.correo.contains("@") || !estadoActual.correo.contains(".")){
            _estado.update { it.copy(errores = it.errores.copy(correo = "Correo inválido")) }
            return false
        }
        if (estadoActual.clave.isEmpty()) {
            _estado.update { it.copy(errores = it.errores.copy(clave = "Debe ingresar una contraseña")) }
            return false
        }
        return true
    }
    fun guardarUsuario() {
            if (validarFormulario()) {
                viewModelScope.launch {
                    val estadoActual = _estado.value
                    val nuevoUsuario = Usuario(
                        nombre = estadoActual.nombre,
                        apellido = estadoActual.apellido,
                        correo = estadoActual.correo,
                        clave = estadoActual.clave,
                        direccion = estadoActual.direccion
                )
                repository.insertar(nuevoUsuario)
            }
        }
    }

    fun cargarUsuarioActivo(correo: String) {
        viewModelScope.launch {
            val usuarioEncontrado = repository.obtenerUsuarios().find { it.correo == correo }
            _usuarioActivo.value = usuarioEncontrado
            if (usuarioEncontrado != null) {
                preferencias.guardarCorreoUsuarioActivo(correo)
            }
        }
    }
    fun cerrarSesion() {
        _usuarioActivo.value = null
        _estado.value = UsuarioUIState() // Limpia el estado del formulario
        preferencias.guardarCorreoUsuarioActivo(null)
    }

    fun actualizarFotoPerfil(uri: Uri?) {
        val usuario = _usuarioActivo.value ?: return
        val uriPersistente = if (uri != null) guardarCopiaLocal(uri) else null
        viewModelScope.launch {
            repository.actualizarFotoPerfil(usuario.id, uriPersistente?.toString())
            cargarUsuarioActivo(usuario.correo)
        }
    }

    fun guardarCopiaLocal(uri: Uri): Uri {
        val application = getApplication<Application>()
        val contentResolver: ContentResolver = application.contentResolver

        // Crea un nombre de archivo único para la copia
        val nombreArchivo = "perfil_${System.currentTimeMillis()}.jpg"
        val archivoDestino = File(application.filesDir, nombreArchivo)

        try {
            // Abre un "stream" para leer la imagen original
            contentResolver.openInputStream(uri)?.use { inputStream ->
                // Abre un "stream" para escribir en el archivo de destino
                FileOutputStream(archivoDestino).use { outputStream ->
                    // Copia los bytes de la imagen original a nuestra copia local
                    inputStream.copyTo(outputStream)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            // Si algo falla, devuelve la URI original como último recurso.
            return uri
        }

        // Devuelve la URI del nuevo archivo guardado, que es 100% persistente.
        return Uri.fromFile(archivoDestino)
    }
}