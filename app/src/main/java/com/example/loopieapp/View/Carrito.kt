package com.example.loopieapp.View

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController


@Composable
fun Carrito(navController: NavController) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("¡Bienvenido a tu carrito de compras!", fontSize = 24.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Text("Proximamente más novedades...", fontSize = 16.sp)
        // Aquí es donde en el futuro se mostrará el carrito de compras, etc.
    }
}
