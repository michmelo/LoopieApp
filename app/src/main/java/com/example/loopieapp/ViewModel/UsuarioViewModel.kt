package com.example.loopieapp.ViewModel

import UsuarioErrores
import UsuarioUIState
import android.app.Application
import android.content.ContentResolver
import android.net.Uri
import androidx.core.net.toUri
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
import com.example.loopieapp.Data.LoginRequest
import com.example.loopieapp.Data.PreferenciasUsuario
import com.example.loopieapp.Data.models.Country
import com.example.loopieapp.Model.AppDatabase
import com.example.loopieapp.Repository.CountryRepository
import com.example.loopieapp.Services.NotificationService
import kotlinx.coroutines.flow.StateFlow

class UsuarioViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: UsuarioRepository = UsuarioRepository()
    private val countryRepository: CountryRepository = CountryRepository()
    private val preferencias: PreferenciasUsuario = PreferenciasUsuario(application)
    private val notificationService = NotificationService(application)

    private val _estado = MutableStateFlow(UsuarioUIState())
    val estado = _estado.asStateFlow()

    private val _usuarioActivo = MutableStateFlow<Usuario?>(null)
    val usuarioActivo = _usuarioActivo.asStateFlow()

    private val _isLoading = MutableStateFlow(true) // Empieza como 'true' porque estamos cargando
    val isLoading = _isLoading.asStateFlow()

    //Logica para integrar la api country
    private val _allCountries = MutableStateFlow<List<Country>>(emptyList())

    private val _filteredCountries = MutableStateFlow<List<Country>>(emptyList())
    val filteredCountries: StateFlow<List<Country>> = _filteredCountries.asStateFlow()

    private val _countries = MutableStateFlow<List<Country>>(emptyList())
    val countries: StateFlow<List<Country>> = _countries.asStateFlow()

    private val _selectedCountry = MutableStateFlow<Country?>(null)
    val selectedCountry: StateFlow<Country?> = _selectedCountry.asStateFlow()

    private val _countrySearchText = MutableStateFlow("")
    val countrySearchText: StateFlow<String> = _countrySearchText.asStateFlow()

    //private val _countryInfo = MutableStateFlow<Country?>(null)
    //val countryInfo: StateFlow<Country?> = _countryInfo.asStateFlow()

    init {
        // Obtenemos el DAO y creamos el repositorio DENTRO del init.
        //val usuarioDao = AppDatabase.getDatabase(application).usuarioDao()
        //repository = UsuarioRepository(usuarioDao)
        //preferencias = PreferenciasUsuario(application)

        viewModelScope.launch {
            try {
                val correoGuardado = preferencias.obtenerCorreoUsuarioActivo()
                val sortedCountries = countryRepository.getAllCountries().sortedBy { it.name.common }

                _allCountries.value = sortedCountries
                _filteredCountries.value = sortedCountries // La UI ahora tiene la lista completa

                if (correoGuardado.isNullOrBlank()) {
                    _isLoading.value = false //No hay sesión
                } else{
                    val usuarioEncontrado = repository.obtenerUsuarioPorCorreo(correoGuardado)
                    _usuarioActivo.value = usuarioEncontrado
                    _isLoading.value = false //Termina la carga
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _isLoading.value = false //Termina la carga

            }
            cargarPaises()
        }
        viewModelScope.launch {
            countries.collect { allCountries ->
                _filteredCountries.value = allCountries
            }
        }
    }

    //Función para cargar la lista de paises
    private fun cargarPaises() {
        viewModelScope.launch {
            val sortedCountries = countryRepository.getAllCountries().sortedBy { it.name.common }
            _allCountries.value = sortedCountries
            _filteredCountries.value = sortedCountries
        }
    }

    fun onCountrySearchChange(text: String) {
        _countrySearchText.value = text
        _selectedCountry.value = null //Limpia la selección si el usuario empieza a escribir de nuevo.
        _estado.update { it.copy(errores = it.errores.copy(direccion = null)) } // Limpia el error de validación.

        if (text.isNotBlank()) {
            _filteredCountries.value = _allCountries.value.filter { country ->
                country.name.common.contains(text, ignoreCase = true)
            }
        } else {
            // Si el campo está vacío, muestra de nuevo la lista completa.
            _filteredCountries.value = _allCountries.value
        }
    }


    //Funcion para cuando el usuario selecciona un pais del menu
    fun onCountrySelected(country: Country) {
        _selectedCountry.value = country
        _countrySearchText.value = country.name.common
        _filteredCountries.value = emptyList()
    }

    //Funciones para el formualario
    fun onNombreChange(valor: String) { _estado.update { it.copy(nombre = valor, errores = it.errores.copy(nombre = null)) } }
    fun onApellidoChange(valor: String) { _estado.update { it.copy(apellido = valor, errores = it.errores.copy(apellido = null)) } }
    fun onCorreoChange(valor: String) { _estado.update { it.copy(correo = valor, errores = it.errores.copy(correo = null)) } }
    fun onClaveChange(valor: String) { _estado.update { it.copy(clave = valor, errores = it.errores.copy(clave = null)) } }
    fun onConfirmarClaveChange(valor: String) { _estado.update { it.copy(confirmarClave = valor, errores = it.errores.copy(confirmarClave = null)) } }
    fun onDireccionChange(valor: String) { _estado.update { it.copy(direccion = valor, errores = it.errores.copy(direccion = null)) } }
    fun onAceptaTerminosChange(valor: Boolean) { _estado.update { it.copy(aceptaTerminos = valor) } }

    // Validaciones formulario
    fun validarFormulario(): Boolean {
        val estadoActual = _estado.value
        _estado.update { it.copy(errores = UsuarioErrores()) }
        if (estadoActual.nombre.isBlank()) { _estado.update { it.copy(errores = it.errores.copy(nombre = "Debe ingresar un nombre")) }
            return false }
        if (estadoActual.apellido.isBlank()) { _estado.update { it.copy(errores = it.errores.copy(apellido = "Debe ingresar un apellido")) }
            return false }
        if (!estadoActual.correo.contains("@")) { _estado.update { it.copy(errores = it.errores.copy(correo = "Correo inválido")) }
            return false }
        if (estadoActual.clave.length < 8) { _estado.update { it.copy(errores = it.errores.copy(clave = "Contraseña debe tener al menos 8 carácteres")) }
            return false }
        if (estadoActual.clave != estadoActual.confirmarClave) { _estado.update { it.copy(errores = it.errores.copy(confirmarClave = "Las contraseñas no coinciden")) }
            return false }
        if (estadoActual.direccion.isBlank()) { _estado.update { it.copy(errores = it.errores.copy(direccion = "Debe ingresar una dirección")) }
            return false }
        if (selectedCountry.value == null) {
            // Si no se ha seleccionado ningún país de la lista, es un error.
            _estado.update { it.copy(errores = it.errores.copy(direccion = "Debes seleccionar un país")) }
            return false
        }
        if (!estadoActual.aceptaTerminos) { _estado.update { it.copy(errores = it.errores.copy(aceptaTerminos = "Debes aceptar los términos y condiciones")) }
            return false }
        _estado.update { it.copy(errores = UsuarioErrores()) }
        return true
    }

    //Validaciones inicio sesion
    fun validarInicioSesion(): Boolean {
        val estadoActual = _estado.value
        _estado.update { it.copy(errores = UsuarioErrores()) }
        if (!estadoActual.correo.contains("@") || !estadoActual.correo.contains(".")){ _estado.update { it.copy(errores = it.errores.copy(correo = "Correo inválido")) }
            return false }
        if (estadoActual.clave.isEmpty()) { _estado.update { it.copy(errores = it.errores.copy(clave = "Debe ingresar una contraseña")) }
            return false }
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
                //llama al metodo en el repositorio que usa la apo
                val usuarioRegistrado = repository.registrarUsuario(nuevoUsuario)

                if (usuarioRegistrado != null) {
                    // Éxito: Muestra notificación y podrías hacer login automático
                    notificationService.mostrarNotificacionRegistroExitoso()
                    iniciarSesion(estadoActual.correo, estadoActual.clave)
                } else {
                    // Error: Muestra un mensaje al usuario
                    _estado.update { it.copy(errores = it.errores.copy(nombre = "El registro falló. Inténtalo de nuevo.")) }
                }
            }
        }
    }

    fun iniciarSesion(correo: String? = null, clave: String? = null) {
        if (correo == null && !validarInicioSesion()) {
            return
        }

        viewModelScope.launch {
            val currentState = _estado.value
            val loginRequest = LoginRequest(correo = currentState.correo, clave = currentState.clave)
            val usuario = repository.iniciarSesion(loginRequest)

            if (usuario != null) {
                _usuarioActivo.value = usuario
                // Guarda la sesión en SharedPreferences
                preferencias.guardarCorreoUsuarioActivo(usuario.correo)
            } else {
                // Error: Muestra un mensaje al usuario
                if (correo == null) {
                    _estado.update { it.copy(errores = it.errores.copy(clave = "Correo o contraseña incorrectos.")) }
                }
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
        val uriPersistente = if (uri != null) guardarCopiaLocal(uri) else "".toUri()

        viewModelScope.launch {
            val usuarioActualizado = usuario.copy(fotoPerfilUri = uriPersistente.toString())
            val resultado = repository.actualizarUsuario(usuario.id,usuarioActualizado)
            if (resultado != null) {
                _usuarioActivo.value = resultado
            }
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