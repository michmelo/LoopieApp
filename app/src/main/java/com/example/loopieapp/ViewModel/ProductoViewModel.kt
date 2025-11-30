package com.example.loopieapp.ViewModel

import android.app.Application
import android.content.ContentResolver
import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.loopieapp.Model.Producto
import com.example.loopieapp.Repository.ProductoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

class ProductoViewModel (application: Application) : AndroidViewModel(application) {

    private val repository: ProductoRepository = ProductoRepository()
    
    //Estado privado
    private val _productos = MutableStateFlow<List<Producto>>(emptyList())
    val productos: StateFlow<List<Producto>> = _productos.asStateFlow()

    //Estado del formulario y de la UI
    private val _uiState = MutableStateFlow(ProductoUIState())
    val uiState: StateFlow<ProductoUIState> = _uiState.asStateFlow()

    //Estado para el producto seleccionado
    private val _productoSeleccionado = MutableStateFlow<Producto?>(null)
    val productoSeleccionado: StateFlow<Producto?> = _productoSeleccionado.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    init {
        //Cargar los productos desde la base de datos al iniciar el ViewModel
        cargarProductos()
    }

    fun cargarProductos() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // --- LÓGICA PARA OBTENER PRODUCTOS ---
                //_productos.value = productoRepository.obtenerTodosLosProductos()

                // Ejemplo con datos de prueba
                _productos.value = listOf(
                    Producto(
                        idProducto = 1,
                        nombre = "Polera Estampada",
                        descripcion = "Polera de algodón con estampado frontal.",
                        precio = 19990.0,
                        categoria = "Poleras",
                        stock = 50,
                        rating = 4.5f,
                        imagen = "https://falabella.scene7.com/is/image/Falabella/gsc_113886561_941074_1"
                    ),
                    Producto(
                        idProducto = 2,
                        nombre = "Jeans Slim Fit",
                        descripcion = "Pantalón de mezclilla azul, corte slim.",
                        precio = 29990.0,
                        categoria = "Pantalones",
                        rating = 4.7f,
                        imagen = "https://falabella.scene7.com/is/image/Falabella/882069792_1"
                    ),
                    Producto(
                        idProducto = 3,
                        nombre = "Chaqueta de Cuero",
                        descripcion = "Chaqueta de cuero sintético, color negro.",
                        precio = 49990.0,
                        categoria = "Chaquetas",
                        stock = 15,
                        rating = 4.9f,
                        imagen = "https://falabella.scene7.com/is/image/Falabella/882897262_1"
                    )
                )

            } catch (e: Exception) {
                _productos.value = emptyList() // Limpia la lista en caso de error
            } finally {
                _isLoading.value = false
            }
        }
    }

    // CREATE - Crear nuevo producto
    fun agregarProducto() : Boolean {
        val currentState = _uiState.value
        
        // Validaciones
        val errores = validarProducto(currentState)
        if (errores.nombre != null || errores.descripcion != null || errores.precio != null || errores.categoria != null || errores.stock != null || errores.rating != null || errores.imagen != null) {
            _uiState.update { it.copy(errores = errores) }
            return false // Falla la validación, detenemos
        }
        
        viewModelScope.launch {
            try {
                val imagenUriSegura = if(currentState.imagen.isNotBlank()){
                    guardarCopiaLocal(currentState.imagen.toUri())
                } else {
                    Uri.EMPTY
                }

                val nuevoProducto = Producto(
                    nombre = currentState.nombre,
                    descripcion = currentState.descripcion,
                    precio = currentState.precio.toDoubleOrNull() ?: 0.0,
                    categoria = currentState.categoria,
                    stock = currentState.stock.toIntOrNull() ?: 0,
                    rating = currentState.rating.toFloatOrNull() ?: 0.0f,
                    imagen = imagenUriSegura.toString()
                )
                val productoCreado = repository.insertar(nuevoProducto)
                if (productoCreado == null) {
                    obtenerProductos()
                    limpiarFormulario()
                }
            } catch (e: Exception) {
                // Manejar error
                _uiState.value = currentState.copy(
                    isLoading = false,
                    errores = ProductoErrores(
                        nombre = "Error al crear el producto: ${e.message}"
                    )
                )
            }
        }
        return true // Se ha creado el producto
    }

    // READ - Buscar productos
    fun obtenerProductos() {
        viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true) }
            try {

                _productos.value = repository.obtenerProductos()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errores = ProductoErrores(
                        nombre = "Error al obtener productos: ${e.message}"
                    )
                )
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }
    
    fun buscarProductos(termino: String){
        viewModelScope.launch {
            try {
                val productosFiltrados = repository.obtenerProductos()
                _productos.value = productosFiltrados.filter { producto ->
                    producto.nombre.contains(termino, ignoreCase = true) ||
                    producto.descripcion.contains(termino, ignoreCase = true) ||
                    producto.categoria.contains(termino, ignoreCase = true)
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errores = ProductoErrores(nombre = "Error en la búsqueda: ${e.message}")
                )
            }
        }
    }
    
    // UPDATE - Actualizar producto
    fun editarProducto() : Boolean {
        val currentState = _uiState.value
        val productoActual = _productoSeleccionado.value ?: return false
        
        // Validaciones
        val errores = validarProducto(currentState)
        if (errores.nombre != null || errores.descripcion != null || errores.precio != null || errores.categoria != null || errores.stock != null || errores.rating != null || errores.imagen != null) {
            _uiState.update { it.copy(errores = errores) }
            return false
        }

        viewModelScope.launch {
            try {
                val imagenUriSegura = if (currentState.imagen.isNotBlank()){
                    if (currentState.imagen != productoActual.imagen) {
                        guardarCopiaLocal(currentState.imagen.toUri())
                    } else {
                        // Si no cambió, usamos la URI que ya teníamos
                        productoActual.imagen.toUri()
                    }
                } else {
                    Uri.EMPTY
                }
                val productoActualizado = productoActual.copy(
                    nombre = currentState.nombre,
                    descripcion = currentState.descripcion,
                    precio = currentState.precio.toDoubleOrNull() ?: 0.0,
                    categoria = currentState.categoria,
                    stock = currentState.stock.toIntOrNull() ?: 0,
                    rating = currentState.rating.toFloatOrNull() ?: 0.0f,
                    imagen = imagenUriSegura.toString()
                )

                //Actualizar la base de datos llamando al repository
                repository.actualizar(productoActualizado.idProducto, productoActualizado)

                //Actualizar la lista en la base de datos
                obtenerProductos()
                limpiarFormulario()
            } catch (e: Exception) {
                _uiState.value = currentState.copy(
                    isLoading = false,
                    errores = ProductoErrores(
                        nombre = "Error al actualizar el producto: ${e.message}"
                    )
                )
            }
        }
        return true // Se ha actualizado el producto
    }
    
    // DELETE - Eliminar producto
    fun eliminarProducto(producto: Producto) {
        viewModelScope.launch {
            val exito = repository.eliminar(producto.idProducto)
            if (exito){
                obtenerProductos()
            } else {
                _uiState.value = _uiState.value.copy(
                    errores = ProductoErrores(
                        nombre = "Error al eliminar el producto: ${producto.nombre}"
                    )
                )
            }
        }
    }

    fun seleccionarProducto(producto: Producto) {
        _productoSeleccionado.value = producto
        _uiState.value = ProductoUIState(
            nombre = producto.nombre,
            descripcion = producto.descripcion,
            precio = producto.precio.toString(),
            categoria = producto.categoria,
            stock = producto.stock.toString(),
            rating = producto.rating.toString(),
            imagen = producto.imagen
        )
    }

    fun limpiarFormulario() {
        _uiState.value = ProductoUIState()
        _productoSeleccionado.value = null
    }

    // Métodos auxiliares para manejar el formulario
    fun actualizarNombre(nombre: String) {
        _uiState.value = _uiState.value.copy(
            nombre = nombre,
            errores = _uiState.value.errores.copy(nombre = null)
        )
    }
    
    fun actualizarDescripcion(descripcion: String) {
        _uiState.value = _uiState.value.copy(
            descripcion = descripcion,
            errores = _uiState.value.errores.copy(descripcion = null)
        )
    }
    
    fun actualizarPrecio(precio: String) {
        _uiState.value = _uiState.value.copy(
            precio = precio,
            errores = _uiState.value.errores.copy(precio = null)
        )
    }
    
    fun actualizarCategoria(categoria: String) {
        _uiState.value = _uiState.value.copy(
            categoria = categoria,
            errores = _uiState.value.errores.copy(categoria = null)
        )
    }
    
    fun actualizarStock(stock: String) {
        _uiState.value = _uiState.value.copy(
            stock = stock,
            errores = _uiState.value.errores.copy(stock = null)
        )
    }
    
    fun actualizarImagen(imagen: String) {
        _uiState.value = _uiState.value.copy(
            imagen = imagen,
            errores = _uiState.value.errores.copy(imagen = null)
        )
    }

    fun actualizarRating(rating: String) { _uiState.value = _uiState.value.copy(rating = rating, errores = _uiState.value.errores.copy(rating = null)) }
    
    fun cancelarEdicion() {
        limpiarFormulario()
    }
    
    // Validaciones
    private fun validarProducto(estado: ProductoUIState): ProductoErrores {
        var errores = ProductoErrores()
        
        if (estado.nombre.isBlank()) {
            errores = errores.copy(nombre = "El nombre es obligatorio")
        }
        if (estado.descripcion.isBlank()) {
            errores = errores.copy(descripcion = "La descripción es obligatoria")
        }
        if (estado.precio.isBlank() || estado.precio.toDoubleOrNull() == null || estado.precio.toDouble() <= 0) {
            errores = errores.copy(precio = "El precio debe ser un número válido mayor a 0")
        }
        if (estado.categoria.isBlank()) {
            errores = errores.copy(categoria = "La categoría es obligatoria")
        }
        if (estado.stock.isBlank() || estado.stock.toIntOrNull() == null || estado.stock.toInt() < 0) {
            errores = errores.copy(stock = "El stock debe ser un número válido mayor o igual a 0")
        }
        if (estado.rating.isBlank() || estado.rating.toFloatOrNull() == null || estado.rating.toFloat() < 0 || estado.rating.toFloat() > 5) {
            errores = errores.copy(rating = "El rating debe ser un número válido entre 0 y 5")
        }
        if (estado.imagen.isBlank()) {
            errores = errores.copy(imagen = "La imagen es obligatoria")
        }
        return errores
    }

    private fun guardarCopiaLocal(uri: Uri): Uri {
        val application = getApplication<Application>()
        val contentResolver: ContentResolver = application.contentResolver
        val nombreArchivo = "prod_img_${System.currentTimeMillis()}.jpg"
        val archivoDestino = File(application.filesDir, nombreArchivo)

        return try {
            contentResolver.openInputStream(uri)?.use { inputStream ->
                FileOutputStream(archivoDestino).use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
            //Exito: Devuelve la URI del nuevo archivo
            Uri.fromFile(archivoDestino)
        } catch (e: Exception) {
            e.printStackTrace()
            //Fallo: Devuelve la URI original como último recurso
            uri
        }
    }
}
