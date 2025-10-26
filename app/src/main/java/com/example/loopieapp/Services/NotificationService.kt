package com.example.loopieapp.Services

import android.Manifest
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.loopieapp.R

class NotificationService(private val context: Context) {

    private val notificationManager = NotificationManagerCompat.from(context)

    fun mostrarNotificacionRegistroExitoso() {
        // Verifica si tienes el permiso para mostrar notificaciones
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Si no tienes el permiso, no puedes mostrar la notificación.
            // En una app real, aquí es donde pedirías el permiso al usuario.
            return
        }

        // Construimos la notificación
        val builder = NotificationCompat.Builder(context, "LOOPY_CHANNEL_ID")
            .setSmallIcon(R.drawable.loopie) // ¡IMPORTANTE! Usa un ícono tuyo
            .setContentTitle("¡Registro Exitoso!")
            .setContentText("¡Bienvenido a Loopie! Tu cuenta ha sido creada.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true) // La notificación se cierra al tocarla

        // Muestra la notificación con un ID único
        notificationManager.notify(1, builder.build())
    }
}