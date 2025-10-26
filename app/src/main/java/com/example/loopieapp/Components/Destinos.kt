package com.example.loopieapp.Components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.ui.graphics.vector.ImageVector

// Enum para definir los destinos de la barra de navegación
enum class Destinos(
    val route: String,
    val icon: ImageVector,
    val label: String
) {
    PANTALLAPRINCIPAL(
        route = "PantallaPrincipal",
        icon = Icons.Default.Home,
        label = "Inicio"
    ),
    PERFIL(
        route = "Perfil/{correo}",
        icon = Icons.Default.Person,
        label = "Perfil"
    ),
    PANEL_VENDEDOR(
        route = "PanelVendedor/{correo}",
        icon = Icons.Default.Storefront,
        label = "Mi Tienda"
    )
    // Puedes agregar más destinos aquí, como "Carrito", "Favoritos", etc.
}
