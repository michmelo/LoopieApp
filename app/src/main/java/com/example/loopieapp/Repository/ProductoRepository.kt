package com.example.loopieapp.Repository

import com.example.loopieapp.DAO.ProductoDao
import com.example.loopieapp.Model.Producto

class ProductoRepository (private val productoDao: ProductoDao) {

    // Pide al DAO la lista de todos los productos
    suspend fun obtenerProductos(): List<Producto> {
        return productoDao.obtenerProductos()
    }

    // Le dice al DAO que inserte un nuevo producto
    suspend fun insertar(producto: Producto) {
        productoDao.insertar(producto)
    }

    // Le dice al DAO que actualice un producto
    suspend fun actualizar(producto: Producto) {
        productoDao.actualizar(producto)
    }

    // Le dice al DAO que elimine un producto
    suspend fun eliminar(producto: Producto) {
        productoDao.eliminar(producto)
    }
}