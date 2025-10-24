package com.example.loopieapp.Components

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter

@Composable
fun ImagenInteligente(imagenUri: Uri?) {
    if (imagenUri != null) {
        Image(
            painter = rememberAsyncImagePainter(model = imagenUri),
            contentDescription = "Imagen de perfil",
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(150.dp).clip(CircleShape)
        )
    } else {
        Icon(
            imageVector = Icons.Default.AccountCircle,
            contentDescription = "Imagen por defecto",
            tint = Color.Gray,
            modifier = Modifier.size(150.dp).clip(CircleShape)
        )
    }
}

//• Carga condicional: si hay URI, usa Coil para pintar la imagen; si no, muestra un ícono.
//• CircleShape + ContentScale.Crop: recorte circular estilo avatar.
