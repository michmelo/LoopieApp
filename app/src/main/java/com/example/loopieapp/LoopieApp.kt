package com.example.loopieapp

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.example.loopieapp.Model.AppDatabase
import com.example.loopieapp.Repository.UsuarioRepository

class LoopieApp : Application() {
    // Usamos 'lazy' para que la base de datos y el repositorio
    // solo se creen una vez, la primera vez que se necesiten.
    val database by lazy { AppDatabase.getDatabase(this) }
    val usuarioRepository by lazy { UsuarioRepository(database.usuarioDao()) }

    override fun onCreate() {
        super.onCreate()
        crearCanalNotificaciones()
    }

    private fun crearCanalNotificaciones() {
        // La creación del canal solo es necesaria en Android 8.0 (API 26) y superior
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val nombreCanal = "Notificaciones Generales"
            val descripcionCanal = "Canal para las notificaciones principales de LoopieApp"
            val importancia = NotificationManager.IMPORTANCE_HIGH

            val canal = NotificationChannel("LOOPY_CHANNEL_ID", nombreCanal, importancia).apply {
                description = descripcionCanal
                // Habilitamos la vibración en el canal
                enableVibration(true)
            }

            // Registramos el canal en el sistema
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(canal)
        }
    }
}
    