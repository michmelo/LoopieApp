package com.example.loopieapp.View

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.loopieapp.ViewModel.UsuarioViewModel // Asegúrate de importar tu ViewModel

@Composable
fun EditProfileScreen(
    usuarioViewModel: UsuarioViewModel = viewModel(),
    onProfileUpdated: () -> Unit // Lambda para navegar hacia atrás cuando se actualice
) {
    // 1. Observa el estado del formulario desde el ViewModel.
    //    Cualquier cambio en 'estado' provocará una recomposición.
    val uiState by usuarioViewModel.estado.collectAsState()

    // 2. Carga los datos del usuario en el formulario una sola vez cuando la pantalla aparece.
    LaunchedEffect(key1 = Unit) {
        usuarioViewModel.cargarDatosParaEdicion()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Editar Perfil", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        // --- Campo Nombre ---
        OutlinedTextField(
            value = uiState.nombre, // Lee el valor del estado
            onValueChange = { nuevoValor ->
                usuarioViewModel.onNombreChange(nuevoValor) // Notifica el cambio al ViewModel
            },
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        // --- Campo Apellido ---
        OutlinedTextField(
            value = uiState.apellido, // Lee el valor del estado
            onValueChange = { usuarioViewModel.onApellidoChange(it) }, // Notifica el cambio
            label = { Text("Apellido") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        // --- Campo Dirección ---
        // (Asumiendo que también quieres editar la dirección)
        OutlinedTextField(
            value = uiState.direccion, // Lee el valor del estado
            onValueChange = { usuarioViewModel.onDireccionChange(it) }, // Notifica el cambio
            label = { Text("Dirección") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        // --- Botón para guardar los cambios ---
        Button(
            onClick = {
                usuarioViewModel.actualizarPerfil() // Llama a la función que guarda en el backend
                onProfileUpdated() // Navega hacia atrás o muestra un mensaje de éxito
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar Cambios")
        }
    }
}
