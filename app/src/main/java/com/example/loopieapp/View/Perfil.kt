package com.example.loopieapp.View

import androidx.compose.material.icons.twotone.Storefront
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.loopieapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Perfil(
    navController: NavController
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Perfil") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xff847996),
                    titleContentColor = Color.White
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Información del usuario
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Avatar del usuario
                    Image(
                        painter = painterResource(id = R.drawable.loopie),
                        contentDescription = "Avatar",
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        text = "Usuario Demo",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Text(
                        text = "usuario@loopie.com",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Badge de vendedor
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xffe8f5e8))
                    ) {
                        Text(
                            text = "Vendedor Verificado",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xff2e7d32),
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            // Opciones del perfil
            Text(
                text = "Opciones",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )

            // Lista de opciones
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OpcionPerfil(
                    icono = Icons.TwoTone.Storefront,
                    titulo = "Panel de Vendedor",
                    descripcion = "Gestiona tus productos y ventas",
                    onClick = { navController.navigate("PanelVendedor") }
                )
                
                OpcionPerfil(
                    icono = Icons.Default.ShoppingCart,
                    titulo = "Mis Compras",
                    descripcion = "Historial de compras realizadas",
                    onClick = { /* TODO: Implementar navegación a compras */ }
                )
                
                OpcionPerfil(
                    icono = Icons.Default.Favorite,
                    titulo = "Favoritos",
                    descripcion = "Productos que te gustan",
                    onClick = { /* TODO: Implementar navegación a favoritos */ }
                )
                
                OpcionPerfil(
                    icono = Icons.Default.Settings,
                    titulo = "Configuración",
                    descripcion = "Ajustes de la aplicación",
                    onClick = { /* TODO: Implementar navegación a configuración */ }
                )

            }

            Spacer(modifier = Modifier.weight(1f))

            // Botón de cerrar sesión
            Button(
                onClick = { 
                    // TODO: Implementar lógica de logout
                    navController.navigate("HomeScreen") {
                        popUpTo("HomeScreen") { inclusive = true }
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xffd32f2f),
                    contentColor = Color.White
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ExitToApp,
                    contentDescription = "Cerrar Sesión",
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text("Cerrar Sesión")
            }
        }
    }
}

@Composable
fun OpcionPerfil(
    icono: ImageVector,
    titulo: String,
    descripcion: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icono,
                contentDescription = titulo,
                tint = Color(0xff847996),
                modifier = Modifier.size(24.dp)
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = titulo,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = descripcion,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }

            Icon(
                Icons.Default.ChevronRight,
                contentDescription = "Ir",
                tint = Color.Gray,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PerfilPreview() {
    Perfil(navController = NavController(LocalContext.current))
}