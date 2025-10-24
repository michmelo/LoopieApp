package com.example.loopieapp.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loopieapp.Model.Producto
import com.example.loopieapp.Model.ProductoUIState
import com.example.loopieapp.Model.ProductoErrores
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class ProductoViewModel : ViewModel() {
    
    // Estado privado
    private val _productos = MutableStateFlow<List<Producto>>(emptyList())
    val productos: StateFlow<List<Producto>> = _productos.asStateFlow()
    
    private val _uiState = MutableStateFlow(ProductoUIState())
    val uiState: StateFlow<ProductoUIState> = _uiState.asStateFlow()
    
    private val _productoSeleccionado = MutableStateFlow<Producto?>(null)
    val productoSeleccionado: StateFlow<Producto?> = _productoSeleccionado.asStateFlow()
    
    // CREATE - Crear nuevo producto
    fun agregarProducto() {
        val currentState = _uiState.value
        
        // Validaciones
        val errores = validarProducto(currentState)
        if (errores != ProductoErrores()) {
            _uiState.value = currentState.copy(errores = errores)
            return
        }
        
        _uiState.value = currentState.copy(isLoading = true)
        
        viewModelScope.launch {
            try {
                val nuevoProducto = Producto(
                    idProducto = UUID.randomUUID().toString(), //genera id automaticamente
                    nombre = currentState.nombre,
                    descripcion = currentState.descripcion,
                    precio = currentState.precio.toDoubleOrNull() ?: 0.0,
                    categoria = currentState.categoria,
                    stock = currentState.stock.toIntOrNull() ?: 0,
                    imagen = currentState.imagen
                )
                
                _productos.value = _productos.value + nuevoProducto
                limpiarFormulario()
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
    }
    
    // READ - Buscar productos
    fun obtenerProductos() { //Lista de todos los productos
        _uiState.value = _uiState.value.copy(isLoading = true)
        
        viewModelScope.launch {
            try {
                //Lista en memoria
                _uiState.value = _uiState.value.copy(isLoading = false)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errores = ProductoErrores(
                        nombre = "Error al obtener productos: ${e.message}"
                    )
                )
            }
        }
    }
    
    fun buscarProductos(termino: String) { //Buscar productos por nombre, descripcion o categoria
        viewModelScope.launch {
            try {
                val productosFiltrados = _productos.value.filter { producto ->
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
    fun editarProducto(idProducto: String) {
        val currentState = _uiState.value
        val productoActual = _productoSeleccionado.value
        
        if (productoActual == null) {
            _uiState.value = currentState.copy(
                errores = ProductoErrores(nombre = "No hay producto seleccionado para actualizar")
            )
            return
        }
        
        // Validaciones
        val errores = validarProducto(currentState)
        if (errores != ProductoErrores()) {
            _uiState.value = currentState.copy(errores = errores)
            return
        }
        
        _uiState.value = currentState.copy(isLoading = true)
        
        viewModelScope.launch {
            try {
                val productoActualizado = productoActual.copy(
                    nombre = currentState.nombre,
                    descripcion = currentState.descripcion,
                    precio = currentState.precio.toDoubleOrNull() ?: 0.0,
                    categoria = currentState.categoria,
                    stock = currentState.stock.toIntOrNull() ?: 0,
                    imagen = currentState.imagen
                )
                
                _productos.value = _productos.value.map { 
                    if (it.idProducto == productoActualizado.idProducto) productoActualizado else it
                }
                
                _productoSeleccionado.value = productoActualizado
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
    }
    
    // DELETE - Eliminar producto
    fun eliminarProducto(id: String) {
        _uiState.value = _uiState.value.copy(isLoading = true)
        
        viewModelScope.launch {
            try {
                _productos.value = _productos.value.filter { it.idProducto != id }
                
                // Si el producto eliminado era el seleccionado, limpiar selección
                if (_productoSeleccionado.value?.idProducto == id) {
                    _productoSeleccionado.value = null
                }
                
                _uiState.value = _uiState.value.copy(isLoading = false)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errores = ProductoErrores(
                        nombre = "Error al eliminar el producto: ${e.message}"
                    )
                )
            }
        }
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
    
    fun limpiarFormulario() {
        _uiState.value = ProductoUIState()
        _productoSeleccionado.value = null
    }
    
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
        
        return errores
    }
}
