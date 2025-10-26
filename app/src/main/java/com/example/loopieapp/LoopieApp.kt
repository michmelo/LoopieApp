package com.example.loopieapp

import android.app.Application
import com.example.loopieapp.Model.AppDatabase
import com.example.loopieapp.Repository.UsuarioRepository    class LoopieApp : Application() {
    // Usamos 'lazy' para que la base de datos y el repositorio
    // solo se creen una vez, la primera vez que se necesiten.
    val database by lazy { AppDatabase.getDatabase(this) }
    val usuarioRepository by lazy { UsuarioRepository(database.usuarioDao()) }
}
    