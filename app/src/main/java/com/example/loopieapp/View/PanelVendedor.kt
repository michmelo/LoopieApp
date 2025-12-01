package com.example.loopieapp.View

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Label
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.loopieapp.Components.CenterAlignedTopAppBarComponent
import com.example.loopieapp.Model.Producto
import com.example.loopieapp.ViewModel.ProductoViewModel
import com.example.loopieapp.Components.RatingBar
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PanelVendedor(
    navController: NavController,
    correoUsuario: String,
    viewModel: ProductoViewModel) {

    // val uiState by viewModel.uiState.collectAsState()
    val productos by viewModel.productos.collectAsState()
    // val productoSeleccionado by viewModel.productoSeleccionado.collectAsState()

    var mostrarDialogoAgregar by remember { mutableStateOf(false) }
    var mostrarDialogoEditar by remember { mutableStateOf(false) }
    var mostrarDialogoEliminar by remember { mutableStateOf(false) }
    var productoAEliminar by remember { mutableStateOf<Producto?>(null) }
    var productoAEditar by remember { mutableStateOf<Producto?>(null) }

    // Cargar productos al iniciar
    LaunchedEffect(Unit) {
        viewModel.obtenerProductos()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBarComponent(
                title = "Panel de Vendedor",
                onBackClick = { navController.popBackStack() }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { mostrarDialogoAgregar = true },
                containerColor = Color(0xff847996)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar Producto")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Estadísticas rápidas
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xfff5f5f5))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = productos.size.toString(),
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xff847996)
                        )
                        Text("Productos")
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = productos.sumOf { it.stock }.toString(),
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xff847996)
                        )
                        Text("Stock Total")
                    }
                }
            }

            // Lista de productos
            Text(
                text = "Mis Productos",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            if (productos.isEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xfff9f9f9))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "No tienes productos registrados",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.Gray
                        )
                        Text(
                            text = "Toca el botón + para agregar tu primer producto",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(productos, key = { producto -> producto.idProducto }) { producto ->
                        ProductoCard(
                            producto = producto,
                            onEditar = {
                                /*
                                productoAEditar = producto
                                viewModel.limpiarFormulario()
                                viewModel.actualizarNombre(producto.nombre)
                                viewModel.actualizarDescripcion(producto.descripcion)
                                viewModel.actualizarPrecio(producto.precio.toString())
                                viewModel.actualizarCategoria(producto.categoria)
                                viewModel.actualizarStock(producto.stock.toString())
                                viewModel.actualizarImagen(producto.imagen)
                                mostrarDialogoEditar = true
                                 */
                                viewModel.seleccionarProducto(producto)
                                mostrarDialogoEditar = true
                            },
                            onEliminar = {
                                productoAEliminar = producto
                                mostrarDialogoEliminar = true
                            }
                        )
                    }
                }
            }
        }
    }
    // Diálogo para agregar producto
    if (mostrarDialogoAgregar) {
        FormularioProductoDialog(
            titulo = "Agregar Producto",
            onConfirmar = {
                viewModel.agregarProducto(
                    onSuccess = {
                        // Esta es la acción que se ejecuta si se guarda bien:
                        // simplemente cierra el diálogo.
                        mostrarDialogoAgregar = false
                    }
                )
            },
            onCancelar = {
                mostrarDialogoAgregar = false
                viewModel.limpiarFormulario()
            },
            viewModel = viewModel
        )
    }

    // Diálogo para editar producto
    if (mostrarDialogoEditar && productoAEditar != null) {
        FormularioProductoDialog(
            titulo = "Editar Producto",
            onConfirmar = {
                if (viewModel.editarProducto()) {
                    mostrarDialogoEditar = false
                }
            },
            onCancelar = {
                mostrarDialogoEditar = false
                viewModel.limpiarFormulario()
            },
            viewModel = viewModel
        )
    }

    // Diálogo de confirmación para eliminar
    if (mostrarDialogoEliminar && productoAEliminar != null) {
        AlertDialog(
            onDismissRequest = { mostrarDialogoEliminar = false },
            title = { Text("Eliminar Producto") },
            text = { Text("¿Estás seguro de que quieres eliminar '${productoAEliminar?.nombre}'?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        productoAEliminar?.let { viewModel.eliminarProducto(it) }
                        mostrarDialogoEliminar = false
                        productoAEliminar = null
                    }
                ) {
                    Text("Eliminar", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDialogoEliminar = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}


@Composable
fun ProductoCard(
    producto: Producto,
    onEditar: () -> Unit,
    onEliminar: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
        //Imagen del producto
            AsyncImage(
                model = producto.imagen, // Carga la imagen desde la URI guardada en la BD
                contentDescription = "Imagen de ${producto.nombre}",
                modifier = Modifier
                    .size(80.dp) // Un tamaño adecuado para la miniatura
                    .clip(RoundedCornerShape(8.dp)) // Bordes redondeados
                    .background(Color.LightGray), // Un fondo mientras carga
                contentScale = ContentScale.Crop // Asegura que la imagen llene el espacio
            )
            Spacer(modifier = Modifier.width(16.dp))

            //Info del producto
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = producto.nombre,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        AssistChip(
                            onClick = { /* Podrías hacer algo aquí, como filtrar */ },
                            label = { Text(producto.categoria) },
                            leadingIcon = {
                                Icon(
                                    Icons.AutoMirrored.Filled.Label, // O un ícono más específico
                                    contentDescription = "Categoría"
                                )
                            }
                        )
                    }

                    Row {
                        IconButton(onClick = onEditar) {
                            Icon(
                                Icons.Default.Edit,
                                contentDescription = "Editar",
                                tint = Color(0xff847996)
                            )
                        }
                        IconButton(onClick = onEliminar) {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = "Eliminar",
                                tint = Color.Red
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = producto.descripcion,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "$${String.format("%.2f", producto.precio)}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xff847996)
                    )

                    RatingBar(
                        rating = producto.rating,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )

                    Text(
                        text = "Stock: ${producto.stock}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (producto.stock > 0) Color.Green else Color.Red
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioProductoDialog(
    titulo: String,
    onConfirmar: () -> Unit,
    onCancelar: () -> Unit,
    viewModel: ProductoViewModel
) {
    val context = LocalContext.current
    val productoUiState by viewModel.uiState.collectAsState()

    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            try {
                val takeFlags: Int = Intent.FLAG_GRANT_READ_URI_PERMISSION
                context.contentResolver.takePersistableUriPermission(it, takeFlags)

                // Ahora sí, actualiza el ViewModel con una URI que tiene permiso duradero
                viewModel.actualizarImagen(it.toString())
            } catch (e: SecurityException) {
                e.printStackTrace()
                // Manejar el caso en que no se pudo obtener el permiso
            }
        }
    }

    Dialog(onDismissRequest = onCancelar) {
        Surface(
            shape = MaterialTheme.shapes.large,
            modifier = Modifier
                .fillMaxWidth()
                // Limita la altura al 90% de la pantalla para que no sea demasiado grande
                .fillMaxHeight(0.9f)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // Título del Diálogo
                Text(
                    titulo,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                )
                {
                    item {
                        // NOMBRE
                        OutlinedTextField(
                            value = productoUiState.nombre,
                            onValueChange = viewModel::actualizarNombre,
                            label = { Text("Nombre") },
                            modifier = Modifier.fillMaxWidth(),
                            isError = productoUiState.errores.nombre != null,
                            singleLine = true
                        )
                        AnimatedVisibility(visible = productoUiState.errores.nombre != null) {
                            Text(
                                productoUiState.errores.nombre ?: "",
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }

                    item {
                        // DESCRIPCION
                        OutlinedTextField(
                            value = productoUiState.descripcion,
                            onValueChange = viewModel::actualizarDescripcion,
                            label = { Text("Descripción") },
                            modifier = Modifier.fillMaxWidth(),
                            isError = productoUiState.errores.descripcion != null
                        )
                        AnimatedVisibility(visible = productoUiState.errores.descripcion != null) {
                            Text(
                                productoUiState.errores.descripcion ?: "",
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }

                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                // PRECIO
                                OutlinedTextField(
                                    value = productoUiState.precio,
                                    onValueChange = viewModel::actualizarPrecio,
                                    label = { Text("Precio") },
                                    modifier = Modifier.fillMaxWidth(),
                                    isError = productoUiState.errores.precio != null
                                )
                                AnimatedVisibility(visible = productoUiState.errores.precio != null) {
                                    Text(
                                        productoUiState.errores.precio ?: "",
                                        color = MaterialTheme.colorScheme.error
                                    )
                                }
                            }

                            Column(modifier = Modifier.weight(1f)) {
                                //STOCK
                                OutlinedTextField(
                                    value = productoUiState.stock,
                                    onValueChange = viewModel::actualizarStock,
                                    label = { Text("Stock") },
                                    modifier = Modifier.fillMaxWidth(),
                                    isError = productoUiState.errores.stock != null
                                )
                                AnimatedVisibility(visible = productoUiState.errores.stock != null) {
                                    Text(
                                        productoUiState.errores.stock ?: "",
                                        color = MaterialTheme.colorScheme.error
                                    )
                                }
                            }
                        }
                    }

                    item {
                        OutlinedTextField(
                            //CATEGORIA
                            value = productoUiState.categoria,
                            onValueChange = viewModel::actualizarCategoria,
                            label = { Text("Categoría") },
                            modifier = Modifier.fillMaxWidth(),
                            isError = productoUiState.errores.categoria != null
                        )
                        AnimatedVisibility(visible = productoUiState.errores.categoria != null) {
                            Text(
                                productoUiState.errores.categoria ?: "",
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }

                    /*item {
                    //RATING
                        OutlinedTextField(
                                value = productoUiState.rating,
                        onValueChange = viewModel::actualizarRating,
                        label = { Text("Rating (0.0 - 5.0)") },
                        modifier = Modifier.fillMaxWidth(),
                        isError = productoUiState.errores.rating != null
                        )

                        AnimatedVisibility(visible = productoUiState.errores.rating != null) {
                            Text(productoUiState.errores.rating ?: "",
                                color = MaterialTheme.colorScheme.error)
                        }
                    }*/

                    item {
                        //IMAGEN
                        OutlinedTextField(
                            value = productoUiState.imagen,
                            onValueChange = viewModel::actualizarImagen,
                            label = { Text("URL de Imagen") },
                            modifier = Modifier.fillMaxWidth(),
                            isError = productoUiState.errores.imagen != null,
                            trailingIcon = {
                                IconButton(onClick = {
                                    pickImageLauncher.launch("image/*")
                                }) {
                                    Icon(
                                        Icons.Default.PhotoLibrary,
                                        contentDescription = "Seleccionar Imagen"
                                    )
                                }
                            }
                        )
                        AnimatedVisibility(visible = productoUiState.errores.imagen != null) {
                            Text(
                                productoUiState.errores.imagen ?: "",
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = onCancelar,
                        colors = ButtonDefaults.textButtonColors()
                    ) {
                        Text("Cancelar")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = onConfirmar,
                        enabled = !productoUiState.isLoading
                    ) {
                        if (productoUiState.isLoading) {
                            CircularProgressIndicator(modifier = Modifier.size(24.dp))
                        } else {
                            Text(if (titulo.contains("Agregar")) "Agregar" else "Guardar Cambios")
                        }
                    }
                }
            }
        }
    }
}