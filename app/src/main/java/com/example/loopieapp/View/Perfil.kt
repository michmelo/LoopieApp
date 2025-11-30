package com.example.loopieapp.View

import android.Manifest
import android.content.ClipData.newUri
import android.content.Context
import android.content.pm.PackageManager
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Environment
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.icons.twotone.Storefront
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.example.loopieapp.Components.CenterAlignedTopAppBarComponent
import com.example.loopieapp.Components.ImagenInteligente
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import com.example.loopieapp.Components.AppScreen
import com.example.loopieapp.Components.Destinos
import com.example.loopieapp.Model.AppDatabase
import com.example.loopieapp.Repository.UsuarioRepository
import com.example.loopieapp.ViewModel.UsuarioViewModel
import com.example.loopieapp.ViewModel.UsuarioViewModelFactory
import java.io.File
import java.util.Date
import java.util.Locale


@Composable
fun Perfil(
    navController: NavController,
    correoUsuario: String,
    viewModel: UsuarioViewModel
) {
    val usuario by viewModel.usuarioActivo.collectAsState()
    var mostrarDialogo by remember { mutableStateOf(false) } // Estado para controlar el diálogo


    // Carga los datos del usuario cuando la pantalla se muestra por primera vez
    /*LaunchedEffect(key1 = correoUsuario) {
        if (correoUsuario.isNotBlank()) {
            viewModel.cargarUsuarioActivo(correoUsuario)
        }
    }*/

    val context = LocalContext.current
    var cameraUri by remember { mutableStateOf<Uri?>(null) }

    fun createImageUri(context: Context): Uri {
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val file = File.createTempFile("JPEG_${timestamp}_", ".jpg", storageDir)
        return FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
    }

    val pickImageLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            viewModel.actualizarFotoPerfil(uri)
        }
    }

    val takePictureLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            cameraUri?.let { viewModel.actualizarFotoPerfil(it) }
        }
    }

    val requestCameraPermission = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            val newUri = createImageUri(context)
            cameraUri = newUri
            takePictureLauncher.launch(newUri)
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBarComponent(
                title ="Mi Perfil"
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Información del usuario
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier.clickable {pickImageLauncher.launch("image/*") }
                    ) {
                        val imagenUri = usuario?.fotoPerfilUri?.let { Uri.parse(it) }
                        ImagenInteligente(
                            imagenUri = imagenUri,
                            modifier = Modifier
                                .size(80.dp)
                                .clip(CircleShape)
                                .background(Color.LightGray)
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))

                    Column {
                        Text(
                            text = usuario?.let { "${it.nombre} ${it.apellido}" } ?: "Cargando...",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = usuario?.correo ?: "cargando...",
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
            }
            // Opciones para cambiar la imagen de perfil
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(onClick = { pickImageLauncher.launch("image/*") }, modifier = Modifier.weight(1f)) {
                        Text("Galería")
                    }
                    Button(onClick = {
                        when {
                            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED -> {
                                val uri = createImageUri(context)
                                cameraUri = uri
                                takePictureLauncher.launch(uri)
                            }
                            else -> requestCameraPermission.launch(Manifest.permission.CAMERA)
                        }
                    }, modifier = Modifier.weight(1f)) {
                        Text("Cámara")
                    }
                }
            }

            //Opciones de perfil
            item {
                Text(
                    text = "Opciones",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
            item {
                OpcionPerfil(
                    icono = Icons.Default.Edit,
                    titulo = "Editar perfil",
                    descripcion = "Gestiona tus datos personales",
                    onClick = {
                        navController.navigate(AppScreen.EditProfile.route)
                    }
                )
            }
            item {
                OpcionPerfil(
                    icono = Icons.TwoTone.Storefront,
                    titulo = "Panel de Vendedor",
                    descripcion = "Gestiona tus productos y ventas",
                    onClick = {
                        val userEmail = usuario?.correo
                        if (userEmail != null) {
                            val rutaCompleta = Destinos.PANEL_VENDEDOR.route.replace("{correo}", userEmail)
                            navController.navigate(rutaCompleta)
                        }
                    }
                )
            }
            item {
                OpcionPerfil(
                    icono = Icons.Default.ShoppingBag,
                    titulo = "Mis Compras",
                    descripcion = "Historial de compras realizadas",
                    onClick = { /* TODO: Implementar navegación a compras */ }
                )
            }
            item {
                OpcionPerfil(
                    icono = Icons.Default.Favorite,
                    titulo = "Favoritos",
                    descripcion = "Productos que te gustan",
                    onClick = { /* TODO: Implementar navegación a favoritos */ }
                )
            }
            item {
                OpcionPerfil(
                    icono = Icons.Default.Settings,
                    titulo = "Configuración",
                    descripcion = "Ajustes de la aplicación",
                    onClick = { /* TODO: Implementar navegación a configuración */ }
                )
            }
            //Cerrar sesion
            item {
                Text(
                    text = "Cuenta",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
            item {
                OpcionPerfil(
                    titulo = "Cerrar Sesión",
                    descripcion = "Finaliza tu sesión actual",
                    icono = Icons.AutoMirrored.Filled.ExitToApp,
                    isDestructive = true,
                    onClick = {
                        viewModel.cerrarSesion()

                        navController.navigate("HomeScreen") {
                            popUpTo(navController.graph.findStartDestination().id) {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun OpcionPerfil(
    icono: ImageVector,
    titulo: String,
    descripcion: String,
    onClick: () -> Unit,
    isDestructive: Boolean = false
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
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
                Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "Ir",
                tint = Color.Gray,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
