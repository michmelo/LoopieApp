package com.example.loopieapp.View

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.loopieapp.Components.ProductoCard
import com.example.loopieapp.ViewModel.EstadoViewModel
import com.example.loopieapp.ViewModel.ProductoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaPrincipal(navController: NavController, productoViewModel: ProductoViewModel){
    val productos by productoViewModel.productos.collectAsState()
    val isLoading by productoViewModel.isLoading.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("¡Bienvenido a Loopie!") })
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (isLoading) {
                // Muestra un indicador de carga si los datos se están obteniendo
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                // 3. Usa LazyColumn para mostrar la lista eficientemente
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(productos) { producto ->
                        ProductoCard(
                            producto = producto,
                            onProductClick = {
                                navController.navigate("detalleProducto/${producto.idProducto}")
                            }
                        )
                    }
                }
            }
        }
    }
    /*
    // Botón para acceder al Perfil
    Button(
        onClick = { navController.navigate("Perfil") },
        colors = ButtonDefaults.buttonColors(
            Color(0xff4caf50),
            Color.White
        ),
        modifier = Modifier.fillMaxWidth()
    ) { 
        Text("Mi Perfil") 
    }
    */
}

