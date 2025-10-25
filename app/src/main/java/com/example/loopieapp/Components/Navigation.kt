package com.example.loopieapp.Components

import FormularioRegistro
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.loopieapp.View.HomeScreen
import com.example.loopieapp.View.InicioSesion
import com.example.loopieapp.View.PanelVendedor
import com.example.loopieapp.View.PantallaPrincipal
import com.example.loopieapp.View.Perfil

@Composable
fun MainScreen () {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination

            val showBottomBar = Destinos.entries.any { it.route == currentDestination?.route }

            if (showBottomBar) {
                NavigationBar {
                    // Usamos nuestra enum class 'Destinos' para crear los items
                    Destinos.entries.forEach { destino ->
                        val isSelected = currentDestination?.hierarchy?.any { it.route == destino.route } == true

                        NavigationBarItem(
                            selected = isSelected,
                            onClick = {
                                navController.navigate(destino.route) {
                                    // Limpia la pila de navegación para evitar acumular pantallas
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true // Evita múltiples copias de la misma pantalla
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
                InicioSesion(navController = navController)
            }
            composable("FormularioRegistro") {
                FormularioRegistro(navController = navController)
            }
            // Pantallas CON barra de navegación
            // Rutas principales
            composable(Destinos.PANTALLAPRINCIPAL.route) {
                PantallaPrincipal(navController = navController)
            }
            composable(Destinos.PERFIL.route) {
                Perfil(navController = navController)
            }
            composable(Destinos.PANEL_VENDEDOR.route) {
                PanelVendedor(navController = navController)
            }

            // Rutas secundarias
            composable("InicioSesion") {
                InicioSesion(navController = navController)
            }
            composable("FormularioRegistro") {
                FormularioRegistro(navController = navController)
            }
            // Desde aquí otras rutas necesarias a futuro, ej"Detalle de Producto", etc.
        }
    }
}