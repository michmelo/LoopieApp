package com.example.loopieapp.Components

import FormularioRegistro
import android.app.Application
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.loopieapp.LoopieApp
import com.example.loopieapp.Model.AppDatabase
import com.example.loopieapp.Repository.UsuarioRepository
import com.example.loopieapp.View.HomeScreen
import com.example.loopieapp.View.InicioSesion
import com.example.loopieapp.View.PanelVendedor
import com.example.loopieapp.View.PantallaPrincipal
import com.example.loopieapp.View.Perfil
import com.example.loopieapp.ViewModel.UsuarioViewModel
import com.example.loopieapp.ViewModel.UsuarioViewModelFactory

@Composable
fun MainScreen () {
    val navController = rememberNavController()
    val context = LocalContext.current
    val application = LocalContext.current.applicationContext as LoopieApp
    val factory = UsuarioViewModelFactory(application)

    val viewModel : UsuarioViewModel = viewModel (factory = factory)
    val usuarioActivo by viewModel.usuarioActivo.collectAsState()

    Scaffold(
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination

            val showBottomBar = Destinos.entries.any { destino ->
                currentDestination?.route?.startsWith(destino.route.substringBefore('/')) == true
            }

            if (showBottomBar) {
                NavigationBar {
                    Destinos.entries.forEach { destino ->
                        val isSelected = currentDestination?.hierarchy?.any { it.route?.startsWith(destino.route.substringBefore('/')) == true
                        } == true

                        NavigationBarItem(
                            selected = isSelected,
                            onClick = {
                                val userEmail = usuarioActivo?.correo ?: "error@loopie.cl"
                                val route = destino.route.replace("{correo}", userEmail)

                                navController.navigate(route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = destino.icon,
                                    contentDescription = destino.label
                                )
                            },
                            label = { Text(destino.label) }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "HomeScreen", // La app empieza en HomeScreen
            modifier = Modifier.padding(innerPadding)
        ) {
            // Pantallas SIN barra de navegación
            composable("HomeScreen") {
                HomeScreen(navController = navController)
            }
            composable("InicioSesion") {
                InicioSesion(navController = navController, viewModel = viewModel)
            }
            composable("FormularioRegistro") {
                FormularioRegistro(navController = navController, viewModel = viewModel)
            }
            // Pantallas CON barra de navegación
            // Rutas principales
            composable(Destinos.PANTALLAPRINCIPAL.route) {
                PantallaPrincipal(navController = navController)
            }
            composable(route = Destinos.PERFIL.route, // Define la ruta con un placeholder
                arguments = listOf(navArgument("correo") { type = NavType.StringType })
            ) { backStackEntry ->
                val correo = backStackEntry.arguments?.getString("correo") ?: ""
                Perfil(navController, correo, viewModel)
            }
            composable( route = Destinos.PANEL_VENDEDOR.route,
                arguments = listOf(navArgument("correo") { type = NavType.StringType })
            ) { backStackEntry ->
                val correo = backStackEntry.arguments?.getString("correo") ?: ""
                PanelVendedor(navController, correo)            }

            // Desde aquí otras rutas necesarias a futuro, ej"Detalle de Producto", etc.
        }
    }
}