import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.loopieapp.Components.CenterAlignedTopAppBarComponent
import com.example.loopieapp.Model.AppDatabase
import com.example.loopieapp.Repository.UsuarioRepository
import com.example.loopieapp.ViewModel.UsuarioViewModel
import com.example.loopieapp.ViewModel.UsuarioViewModelFactory

@Composable
fun FormularioRegistro(
    navController : NavController,
    viewModel : UsuarioViewModel
) {
    val estado by viewModel.estado.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBarComponent(
                title ="Registro",
                onBackClick = { navController.popBackStack() }
            )
        }
    ) { innerPadding ->
        LazyColumn (
            modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()
            .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Rellene los datos para completar su registro",
                    fontWeight = FontWeight.Thin,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(bottom = 16.dp),
                    color = Color(0xff310a31),
                    style = MaterialTheme.typography.titleLarge,
                    lineHeight = 32.sp,
                    letterSpacing = 0.005.sp,
                    textAlign = TextAlign.Center
                )
            }
            item {
                OutlinedTextField(
                    value = estado.nombre,
                    onValueChange = viewModel::onNombreChange,
                    label = { Text("Nombre") },
                    isError = estado.errores.nombre != null,
                    supportingText = {
                        estado.errores.nombre?.let {
                            Text(it, color = MaterialTheme.colorScheme.error)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item {
                OutlinedTextField(
                    value = estado.apellido,
                    onValueChange = viewModel::onApellidoChange,
                    label = { Text("Apellido") },
                    isError = estado.errores.apellido != null,
                    supportingText = {
                        estado.errores.apellido?.let {
                            Text(it, color = MaterialTheme.colorScheme.error)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item {
                OutlinedTextField(
                    value = estado.correo,
                    onValueChange = viewModel::onCorreoChange,
                    label = { Text("Correo") },
                    isError = estado.errores.correo != null,
                    supportingText = {
                        estado.errores.correo?.let {
                            Text(it, color = MaterialTheme.colorScheme.error)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item {
                OutlinedTextField(
                    value = estado.clave,
                    onValueChange = viewModel::onClaveChange,
                    label = { Text("Contraseña") },
                    visualTransformation = PasswordVisualTransformation(),
                    isError = estado.errores.clave != null,
                    supportingText = {
                        estado.errores.clave?.let {
                            Text(it, color = MaterialTheme.colorScheme.error)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item {
                OutlinedTextField(
                    value = estado.confirmarClave,
                    onValueChange = viewModel::onConfirmarClaveChange,
                    label = { Text("Confirmar contraseña") },
                    visualTransformation = PasswordVisualTransformation(),
                    isError = estado.errores.confirmarClave != null,
                    supportingText = {
                        estado.errores.confirmarClave?.let {
                            Text(it, color = MaterialTheme.colorScheme.error)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item {
                OutlinedTextField(
                    value = estado.direccion,
                    onValueChange = viewModel::onDireccionChange,
                    label = { Text("Dirección") },
                    isError = estado.errores.direccion != null,
                    supportingText = {
                        estado.errores.direccion?.let {
                            Text(it, color = MaterialTheme.colorScheme.error)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }

           item {
               Row(verticalAlignment = Alignment.CenterVertically) {
                   Checkbox(
                       checked = estado.aceptaTerminos,
                       onCheckedChange = viewModel::onAceptaTerminosChange
                   )
                   Spacer(Modifier.width(8.dp))

                   val colorTextoTerminos = if (estado.errores.aceptaTerminos != null) {
                       MaterialTheme.colorScheme.error
                   } else {
                       MaterialTheme.colorScheme.onSurface
                   }
                   Text(
                       text = "Acepto los términos y condiciones",
                       color = colorTextoTerminos
                   )
               }
           }
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = {
                        if (viewModel.validarFormulario()) {
                            viewModel.guardarUsuario()
                            val correo = estado.correo
                            navController.navigate("Perfil/$correo") {
                                popUpTo("HomeScreen") { inclusive = true }
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xff847996),
                        contentColor = Color.White
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Registrarse")
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}