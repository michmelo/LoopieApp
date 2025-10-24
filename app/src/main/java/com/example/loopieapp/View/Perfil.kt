package com.example.loopieapp.View

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Environment
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.loopieapp.Components.CenterAlignedTopAppBarComponent
import com.example.loopieapp.Components.ImagenInteligente
import com.example.loopieapp.R
import com.example.loopieapp.ViewModel.PerfilViewModel
import java.io.File
import java.util.Date
import java.util.Locale


@Composable
fun Perfil(
    navController: NavController
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBarComponent(
                title ="Mi Perfil"
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

@Composable
fun PerfilScreen(viewModel: PerfilViewModel = viewModel()) {
    val context = LocalContext.current
    val imagenUri by viewModel.imagenUri.collectAsState()
    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        viewModel.setImage(uri)
    }
    var cameraUri by remember { mutableStateOf<Uri?>(null) }
    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) viewModel.setImage(cameraUri)
    }
    fun createImageUri(context: Context): Uri {
        val timestamp = SimpleDateFormat(
            "yyyyMMdd_HHmmss",
            Locale.getDefault()
        ).format(Date())
        val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val file = File.createTempFile("JPEG_${timestamp}_", ".jpg", storageDir)
        return FileProvider.getUriForFile(context, "${context.packageName}.fileprovider",
            file)
    }
    val requestCameraPermission = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            val uri = createImageUri(context)
            cameraUri = uri
            takePictureLauncher.launch(uri)
        }
    }
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        ImagenInteligente(imagenUri)
        Spacer(Modifier.height(24.dp))
        Button(onClick = { pickImageLauncher.launch("image/*") }) {
            Text("Seleccionar desde galería")
        }
        Spacer(Modifier.height(12.dp))
        Button(onClick = {
            when (PackageManager.PERMISSION_GRANTED) {
                ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) ->
                {
                    val uri = createImageUri(context)
                    cameraUri = uri
                    takePictureLauncher.launch(uri)
                }
                else -> requestCameraPermission.launch(Manifest.permission.CAMERA)
            }
        }) {
            Text("Tomar foto con cámara")
        }
    }
}
