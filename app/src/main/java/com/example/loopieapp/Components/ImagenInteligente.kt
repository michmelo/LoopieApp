package com.example.loopieapp.Components

import android.net.Uri
import com.example.loopieapp.R
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter

@Composable
fun ImagenInteligente(
    imagenUri: Uri?,
    modifier: Modifier = Modifier
) {
    AsyncImage(
        model = imagenUri,
        contentDescription = "Imagen de perfil",
        modifier = modifier,
        contentScale = ContentScale.Crop,
        // Imagen de placeholder mientras carga o si la URI es nula
        placeholder = painterResource(id = R.drawable.loopie),
        // Imagen a mostrar si hay un error
        error = painterResource(id = R.drawable.loopie)
    )
}

//• Carga condicional: si hay URI, usa Coil para pintar la imagen; si no, muestra un ícono.
//• CircleShape + ContentScale.Crop: recorte circular estilo avatar.
