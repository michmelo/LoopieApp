package com.example.loopieapp.ViewModel.factory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.loopieapp.Model.AppDatabase
import com.example.loopieapp.Repository.ProductoRepository
import com.example.loopieapp.ViewModel.ProductoViewModel

class ProductoViewModelFactory (private val application: Application) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            val productoDao = AppDatabase.getDatabase(application).productoDao()
            val repository = ProductoRepository(productoDao)
            return ProductoViewModel(repository, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}