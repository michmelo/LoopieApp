package com.example.loopieapp.View

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.loopieapp.Components.CenterAlignedTopAppBarComponent
import com.example.loopieapp.Model.Producto
import com.example.loopieapp.ViewModel.ProductoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PanelVendedor(
    navController: NavController,
    correoUsuario: String,
    viewModel: ProductoViewModel = viewModel())
{

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
                title ="Panel de Vendedor",
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
                    items(productos) { producto ->
                        ProductoCard(
                            producto = producto,
                            onEditar = {
                                productoAEditar = producto
                                viewModel.limpiarFormulario()
                                viewModel.actualizarNombre(producto.nombre)
                                viewModel.actualizarDescripcion(producto.descripcion)
                                viewModel.actualizarPrecio(producto.precio.toString())
                                viewModel.actualizarCategoria(producto.categoria)
                                viewModel.actualizarStock(producto.stock.toString())
                                viewModel.actualizarImagen(producto.imagen)
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
                viewModel.agregarProducto()
                mostrarDialogoAgregar = false
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
                viewModel.editarProducto(productoAEditar!!.idProducto) // Assuming a method like this exists
                mostrarDialogoEditar = false
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
                        productoAEliminar?.let { viewModel.eliminarProducto(it.idProducto) }
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
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
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
                    Text(
                        text = producto.categoria,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
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
                Text(
                    text = "Stock: ${producto.stock}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (producto.stock > 0) Color.Green else Color.Red
                )
            }
        }
    }
}

@Composable
fun FormularioProductoDialog(
    titulo: String,
    onConfirmar: () -> Unit,
    onCancelar: () -> Unit,
    viewModel: ProductoViewModel
) {
    val productoUiState by viewModel.uiState.collectAsState()

    AlertDialog(
        onDismissRequest = onCancelar,
        title = { Text(titulo) },
        text = {
            Column {
                OutlinedTextField(
                    value = productoUiState.nombre,
                    onValueChange = viewModel::actualizarNombre,
                    label = { Text("Nombre") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = productoUiState.errores.nombre != null
                )
                if (productoUiState.errores.nombre != null) {
                    Text(
                        text = productoUiState.errores.nombre!!,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = productoUiState.descripcion,
                    onValueChange = viewModel::actualizarDescripcion,
                    label = { Text("Descripción") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = productoUiState.errores.descripcion != null
                )
                if (productoUiState.errores.descripcion != null) {
                    Text(
                        text = productoUiState.errores.descripcion!!,
                        color = Color.Red,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        OutlinedTextField(
                            value = productoUiState.precio,
                            onValueChange = viewModel::actualizarPrecio,
                            label = { Text("Precio") },
                            modifier = Modifier.weight(1f),
                            isError = productoUiState.errores.precio != null
                        )
                        if (productoUiState.errores.precio != null) {
                            Text(
                                text = productoUiState.errores.precio!!,
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        OutlinedTextField(
                            value = productoUiState.stock,
                            onValueChange = viewModel::actualizarStock,
                            label = { Text("Stock") },
                            modifier = Modifier.weight(1f),
                            isError = productoUiState.errores.stock != null
                        )
                        if (productoUiState.errores.stock != null) {
                            Text(
                                text = productoUiState.errores.stock!!,
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = productoUiState.categoria,
                    onValueChange = viewModel::actualizarCategoria,
                    label = { Text("Categoría") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = productoUiState.errores.categoria != null
                )
                if (productoUiState.errores.categoria != null) {
                    Text(
                        text = productoUiState.errores.categoria!!,
                        color = Color.Red,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = productoUiState.imagen,
                    onValueChange = viewModel::actualizarImagen,
                    label = { Text("URL de Imagen") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = productoUiState.errores.imagen != null,
                    singleLine = true
                )
                if (productoUiState.errores.imagen != null) {
                    Text(
                        text = productoUiState.errores.imagen!!,
                        color = Color.Red,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirmar,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xff847996))
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            Button(
                onClick = onCancelar
            ) {
                Text("Cancelar")
            }
        }
    )
}
