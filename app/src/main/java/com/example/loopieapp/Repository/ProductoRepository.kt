package com.example.loopieapp.Repository

import com.example.loopieapp.DAO.ProductoDao
import com.example.loopieapp.Data.RetrofitClient
import com.example.loopieapp.Model.Producto
import com.example.loopieapp.Data.ApiService

class ProductoRepository (
    //private val productoDao: ProductoDao
    private val apiService: ApiService = RetrofitClient.instance
) {

    // Pide al DAO la lista de todos los productos
    //suspend fun obtenerProductos(): List<Producto> { return productoDao.obtenerProductos() }

    // Le dice al DAO que inserte un nuevo producto
    //suspend fun insertar(producto: Producto) { productoDao.insertar(producto) }

    // Le dice al DAO que actualice un producto
    //suspend fun actualizar(producto: Producto) { productoDao.actualizar(producto) }

    // Le dice al DAO que elimine un producto
    //suspend fun eliminar(producto: Producto) { productoDao.eliminar(producto) }

    // Llama al endpoint GET /productos
    suspend fun obtenerProductos(): List<Producto> {
        return try {
            val response = apiService.getAllProducts()
            if (response.isSuccessful) {
                // Si la llamada fue exitosa, devuelve la lista de productos.
                // Si el cuerpo es nulo, devuelve una lista vacía para evitar crashes.
                response.body() ?: emptyList()
            } else {
                // Si hubo un error en el servidor (ej. 404, 500), devuelve una lista vacía.
                emptyList()
            }
        } catch (e: Exception) {
            // Si hubo un error de red (ej. sin internet), imprime el error y devuelve una lista vacía.
            e.printStackTrace()
            emptyList()
        }
    }

    // Llama al endpoint POST /productos
    suspend fun insertar(producto: Producto): Producto? {
        return try {
            val response = apiService.createProduct(producto)
            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    // Llama al endpoint PUT /productos/{idProducto}
    suspend fun actualizar(idProducto: Int, producto: Producto): Producto? {
        return try {
            val response = apiService.updateProduct(idProducto, producto)
            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    // Llama al endpoint DELETE /productos/{idProducto}
    suspend fun eliminar(idProducto: Int): Boolean {
        return try {
            val response = apiService.deleteProduct(idProducto)
            // La eliminación fue exitosa si el código es 200-299 (ej. 204 No Content).
            response.isSuccessful
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }




}