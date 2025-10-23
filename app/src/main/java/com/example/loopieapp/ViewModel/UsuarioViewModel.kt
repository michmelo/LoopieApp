package com.example.loopieapp.ViewModel

import UsuarioErrores
import UsuarioUIState
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class UsuarioViewModel : ViewModel(){
    private val _estado = MutableStateFlow(UsuarioUIState())
    val estado : StateFlow<UsuarioUIState> = _estado

    fun onNombreChange (valor : String){
        _estado.update {it.copy(nombre = valor, errores = it.errores.copy(nombre = null))}
    }

    fun onApellidoChange (valor : String){
        _estado.update {it.copy(apellido = valor, errores = it.errores.copy(apellido = null))}
    }

    fun onCorreoChange (valor : String){
        _estado.update {it.copy(correo = valor, errores = it.errores.copy(correo = null))}
    }

    fun onClaveChange (valor : String){
        _estado.update {it.copy(clave = valor, errores = it.errores.copy(clave = null))}
    }

    fun onConfirmarClaveChange (valor : String){
        _estado.update {it.copy(confirmarClave = valor, errores = it.errores.copy(confirmarClave = null))}
    }

    fun onDireccionChange (valor : String){
        _estado.update {it.copy(direccion = valor, errores = it.errores.copy(direccion = null))}
    }

    fun onAceptaTerminosChange(valor : Boolean){
        _estado.update {it.copy(aceptaTerminos = valor)}
    }

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
}