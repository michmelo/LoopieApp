package com.example.loopieapp

import FormularioRegistro
import ResumenScreen
import com.example.loopieapp.ViewModel.UsuarioViewModel
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.loopieapp.View.HomeScreen
import com.example.loopieapp.View.InicioSesion
import com.example.loopieapp.View.PantallaPrincipal
import com.example.loopieapp.ui.theme.LoopieAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LoopieAppTheme {
                val navController = rememberNavController()
                val usuarioViewModel: UsuarioViewModel = viewModel()

                Scaffold { innerPadding ->

                    NavHost(
                        navController = navController,
                        startDestination = "HomeScreen",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("HomeScreen") {
                            HomeScreen(navController)
                        }
                        composable("InicioSesion") {
                            InicioSesion(navController = navController)
                        }
                        composable("FormularioRegistro") {
                            FormularioRegistro(navController, usuarioViewModel)
                        }
                        composable("resumen") {
                            ResumenScreen(usuarioViewModel)
                        }
                        composable("PantallaPrincipal") {
                            PantallaPrincipal()
                        }
                }

                }
            }
        }
    }
}


