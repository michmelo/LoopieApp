package com.example.loopieapp.View

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.loopieapp.ViewModel.UsuarioViewModel

@Composable
fun ModificarPerfilDialog(
    viewModel: UsuarioViewModel,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    val estado by viewModel.estado.collectAsState()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Editar Perfil") },
        text = {
            LazyColumn {
                item {
                    //Campos de Datos Personales
                    Text("Datos Personales", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(8.dp))

                    // Campo para el nombre
                    OutlinedTextField(
                        value = estado.nombre,
                        onValueChange = viewModel::onNombreChange,
                        label = { Text("Nombre") }
                    )
                    Spacer(Modifier.height(8.dp))
                    // Campo para el apellido
                    OutlinedTextField(
                        value = estado.apellido,
                        onValueChange = viewModel::onApellidoChange,
                        label = { Text("Apellido") }
                    )
                    Spacer(Modifier.height(8.dp))
                    // Campo para la dirección
                    OutlinedTextField(
                        value = estado.direccion,
                        onValueChange = viewModel::onDireccionChange,
                        label = { Text("Dirección") }
                    )
                    Spacer(Modifier.height(8.dp))
                    Divider()
                    Spacer(Modifier.height(16.dp))

                    //Sección para Cambiar Contraseña
                    Text("Modificar Contraseña", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(8.dp))

                    //Campo para clave actual
                    OutlinedTextField(
                        value = estado.claveActual,
                        onValueChange = viewModel::onClaveActualChange,
                        label = { Text("Contraseña Actual") },
                        visualTransformation = PasswordVisualTransformation(),
                        isError = estado.errores.claveActual != null,
                        supportingText = { estado.errores.claveActual?.let { Text(it) } }
                    )

                    //Campo para nueva clave
                    OutlinedTextField(
                        value = estado.nuevaClave,
                        onValueChange = viewModel::onNuevaClaveChange,
                        label = { Text("Nueva Contraseña") },
                        visualTransformation = PasswordVisualTransformation(),
                        isError = estado.errores.nuevaClave != null,
                        supportingText = { estado.errores.nuevaClave?.let { Text(it) } }
                    )

                    //Campo para confirmar nueva clave
                    OutlinedTextField(
                        value = estado.confirmarNuevaClave,
                        onValueChange = viewModel::onConfirmarNuevaClaveChange,
                        label = { Text("Confirmar Nueva Contraseña") },
                        visualTransformation = PasswordVisualTransformation()
                    )
                }
            }
        },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text("Guardar Cambios")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}