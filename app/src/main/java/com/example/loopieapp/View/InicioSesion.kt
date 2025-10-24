package com.example.loopieapp.View

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.ViewModel
import androidx.navigation.compose.rememberNavController
import com.example.loopieapp.Components.CenterAlignedTopAppBarComponent
import com.example.loopieapp.Model.AppDatabase
import com.example.loopieapp.Repository.UsuarioRepository
import com.example.loopieapp.ViewModel.UsuarioViewModel
import com.example.loopieapp.ViewModel.UsuarioViewModelFactory

@Composable
fun InicioSesion(
    navController : NavController,
    //viewModel : UsuarioViewModel = viewModel()
) {
    val context = LocalContext.current.applicationContext
    val factory = remember {
        UsuarioViewModelFactory(
            UsuarioRepository(
                AppDatabase.getDatabase(context).usuarioDao()
            )
        )
    }
    val viewModel: UsuarioViewModel = viewModel(factory = factory)
    val estado by viewModel.estado.collectAsState()

    Scaffold (
        topBar = {
            CenterAlignedTopAppBarComponent(
                title = "Inicio de Sesión",
                onBackClick = { navController.popBackStack() })
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
            text = "Inicio de Sesión",
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            color = Color(0xff310a31),
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center
            )

            //Campo para el correo
            OutlinedTextField(
                value = estado.correo,
                onValueChange = viewModel::onCorreoChange,
                label = { Text("Ingrese correo") },
                isError = estado.errores.correo != null,
                supportingText = {
                    estado.errores.correo?.let {
                        Text(it, color = MaterialTheme.colorScheme.error)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            //Campo para la contraseña
            OutlinedTextField(
                value = estado.clave,
                onValueChange = viewModel::onClaveChange,
                label = { Text("Ingrese contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                isError = estado.errores.clave != null,
                supportingText = {
                    estado.errores.clave?.let {
                        Text(it, color = MaterialTheme.colorScheme.error)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (viewModel.validarInicioSesion()) {
                        navController.navigate("Perfil")
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xff847996),
                    contentColor = Color.White
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Iniciar Sesión")
            }
        }
    }
}