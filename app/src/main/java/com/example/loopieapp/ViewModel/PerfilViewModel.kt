package com.example.loopieapp.ViewModel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loopieapp.Repository.PerfilRepositorio
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PerfilViewModel(
    private val repositorio: PerfilRepositorio = PerfilRepositorio()
) : ViewModel() {
    private val _imagenUri = MutableStateFlow<Uri?>(repositorio.getProfile().imagenUri)
    val imagenUri: StateFlow<Uri?> = _imagenUri
    fun setImage(uri: Uri?) {
        viewModelScope.launch {
            _imagenUri.value = uri
            repositorio.updateImage(uri)
        }
    }
}

//• imagenUri: flujo observado por la UI para renderizar la foto.
//• setImage(uri): actualiza el estado y sincroniza el repositorio (fuente de verdad)