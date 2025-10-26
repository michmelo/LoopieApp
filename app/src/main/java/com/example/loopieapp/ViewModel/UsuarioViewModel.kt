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
import java.io.File
import java.io.FileOutputStream
import androidx.lifecycle.ViewModel
import com.example.loopieapp.Model.AppDatabase

class UsuarioViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: UsuarioRepository

    init {
        // Obtenemos el DAO y creamos el repositorio DENTRO del init.
        val usuarioDao = AppDatabase.getDatabase(application).usuarioDao()
        repository = UsuarioRepository(usuarioDao)
    }

    private val _estado = MutableStateFlow(UsuarioUIState())
    val estado = _estado.asStateFlow()

    private val _usuarioActivo = MutableStateFlow<Usuario?>(null)
    val usuarioActivo = _usuarioActivo.asStateFlow()

    //Funciones para el formualario
    fun onNombreChange (valor : String){ _estado.update {it.copy(nombre = valor, errores = it.errores.copy(nombre = null))} }
    fun onApellidoChange (valor : String){ _estado.update {it.copy(apellido = valor, errores = it.errores.copy(apellido = null))} }
    fun onCorreoChange (valor : String){ _estado.update {it.copy(correo = valor, errores = it.errores.copy(correo = null))} }
    fun onClaveChange (valor : String){ _estado.update {it.copy(clave = valor, errores = it.errores.copy(clave = null))} }
    fun onConfirmarClaveChange (valor : String){ _estado.update {it.copy(confirmarClave = valor, errores = it.errores.copy(confirmarClave = null))} }
    fun onDireccionChange (valor : String){ _estado.update {it.copy(direccion = valor, errores = it.errores.copy(direccion = null))} }
    fun onAceptaTerminosChange(valor : Boolean){ _estado.update {it.copy(aceptaTerminos = valor)} }

    // Validaciones formulario
    fun validarFormulario() : Boolean {
        val estadoActual = _estado.value
        val errores = UsuarioErrores(
            nombre = if(estadoActual.nombre.isBlank()) "Debe ingresar un nombre" else null,
            apellido = if(estadoActual.apellido.isBlank()) "Debe ingresar un apellido" else null,
            correo = if(!estadoActual.correo.contains("@")) "Correo inválido" else null,
            clave = if(estadoActual.clave.length < 8) "Contraseña debe tener al menos 8 carácteres" else null,
            confirmarClave = if(estadoActual.clave != estadoActual.confirmarClave) "Las contraseñas no coinciden" else null,
            direccion = if(estadoActual.direccion.isBlank()) "Debe ingresar una dirección" else null
        )

        val hayErrores = listOfNotNull(
            errores.nombre,
            errores.apellido,
            errores.correo,
            errores.clave,
            errores.confirmarClave,
            errores.direccion
        ).isNotEmpty()

        _estado.update {it.copy(errores = errores)}

        return !hayErrores
    }

    //Validaciones inicio sesion
    fun validarInicioSesion(): Boolean {
        val estadoActual = _estado.value
        val errores = UsuarioErrores(
            // Solo validamos correo y clave para el login
            correo = if (!estadoActual.correo.contains("@")) "Correo inválido" else null,
            clave = if (estadoActual.clave.length < 8) "Contraseña debe tener al menos 8 caracteres" else null
        )

        // Comprueba si hay algún error en los campos relevantes
        val hayErrores = listOfNotNull(
            errores.correo,
            errores.clave
        ).isNotEmpty()

        _estado.update { it.copy(errores = errores) }

        // Devuelve 'true' si NO hay errores
        return !hayErrores
    }

    fun guardarUsuario() {
        viewModelScope.launch {
            if (validarFormulario()) {
                val estadoActual = _estado.value
                val nuevoUsuario = Usuario(
                    nombre = estadoActual.nombre,
                    apellido = estadoActual.apellido,
                    correo = estadoActual.correo,
                    clave = estadoActual.clave,
                    direccion = estadoActual.direccion,
                    //aceptaTerminos = estadoActual.aceptaTerminos
                )
                repository.insertar(nuevoUsuario)
            }
        }
    }
    fun cargarUsuarioActivo(correo: String) {
        viewModelScope.launch {
            val usuarioEncontrado = repository.obtenerUsuarios().find { it.correo == correo }
            _usuarioActivo.value = usuarioEncontrado
        }
    }

    fun cerrarSesion() {
        _usuarioActivo.value = null
        _estado.value = UsuarioUIState() // Limpia el estado del formulario
    }

    fun actualizarFotoPerfil(uri: Uri?) {
        val usuario = _usuarioActivo.value ?: return
        val uriPersistente = if (uri != null) guardarCopiaLocal(uri) else null

        viewModelScope.launch {
            repository.actualizarFotoPerfil(usuario.id, uriPersistente?.toString())
            cargarUsuarioActivo(usuario.correo) // Recarga el usuario para refrescar la UI
        }
    }

    private fun guardarCopiaLocal(uri: Uri): Uri {
        return uri
    }
}