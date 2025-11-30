package com.example.loopieapp.Components

import com.example.loopieapp.View.FormularioRegistro
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.loopieapp.View.HomeScreen
import com.example.loopieapp.View.InicioSesion
import com.example.loopieapp.View.PanelVendedor
import com.example.loopieapp.View.PantallaPrincipal
import com.example.loopieapp.View.Perfil
import com.example.loopieapp.View.SplashScreen
import com.example.loopieapp.ViewModel.UsuarioViewModel
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.LaunchedEffect
import com.example.loopieapp.View.Carrito
import com.example.loopieapp.View.EditProfileScreen
import com.example.loopieapp.ViewModel.ProductoViewModel

@Composable
fun MainScreen () {
    val navController = rememberNavController()
    //val context = LocalContext.current
    //val application = LocalContext.current.applicationContext as Application
    //val factory = remember (application) {
    //    UsuarioViewModelFactory(application)
    //}

    val usuarioViewModel: UsuarioViewModel = viewModel()
    val productoViewModel: ProductoViewModel = viewModel()

    val usuarioActivo by usuarioViewModel.usuarioActivo.collectAsState()
    val isLoading by usuarioViewModel.isLoading.collectAsState()

    /*val productoFactory = remember(application) {
        ProductoViewModelFactory(application)
    }*/

    LaunchedEffect(key1 =usuarioActivo, key2 = isLoading) {
        if (isLoading) {
            return@LaunchedEffect
        }
        if (usuarioActivo != null) {
            navController.navigate(Destinos.PANTALLAPRINCIPAL.route) {
                popUpTo(navController.graph.findStartDestination().id) {
                    inclusive = true
                }
                launchSingleTop = true
            }
        }
        else {
            navController.navigate("HomeScreen") {
                popUpTo(navController.graph.findStartDestination().id) {
                    inclusive = true
                }
                launchSingleTop = true
            }
        }
    }
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
                        val isSelected = currentDestination?.hierarchy?.any {
                            it.route?.startsWith(destino.route.substringBefore('/')) == true
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
            startDestination = "SplashScreen", // La app empieza en HomeScreen
            modifier = Modifier.padding(innerPadding),

            // --- Parámetros de Animación ---
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(400)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(400)
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(400)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(400)
                )
            }
        ) {
            composable("SplashScreen") {
                SplashScreen()
            }
            // Pantallas SIN barra de navegación
            composable("HomeScreen") {
                HomeScreen(navController)
            }
            composable("InicioSesion") {
                InicioSesion(navController = navController, viewModel = usuarioViewModel)
            }
            composable("FormularioRegistro") {
                FormularioRegistro(navController = navController, viewModel = usuarioViewModel)
            }
            composable(route = AppScreen.EditProfile.route) {
                EditProfileScreen(
                    usuarioViewModel = usuarioViewModel,
                    onProfileUpdated = {
                        // Acción para volver a la pantalla anterior (Perfil)
                        navController.popBackStack()
                    }
                )
            }

            // Pantallas CON barra de navegación
            // Rutas principales
            composable(Destinos.PANTALLAPRINCIPAL.route) {
                PantallaPrincipal(navController = navController, productoViewModel = productoViewModel)
            }
            composable(
                route = Destinos.PERFIL.route, // Define la ruta con un placeholder
                arguments = listOf(navArgument("correo") { type = NavType.StringType })
            ) { backStackEntry ->
                val correo = backStackEntry.arguments?.getString("correo") ?: ""
                Perfil(navController, correo, usuarioViewModel)
            }
            composable(
                route = Destinos.PANEL_VENDEDOR.route,
                arguments = listOf(navArgument("correo") { type = NavType.StringType })
            ) { backStackEntry ->
                val correo = backStackEntry.arguments?.getString("correo") ?: ""
                PanelVendedor(
                    navController = navController,
                    correoUsuario = correo,
                    viewModel = productoViewModel)
            }
            // Desde aquí otras rutas necesarias a futuro, ej"Detalle de Producto", etc.
            composable(
                route = Destinos.CARRITO.route) {
                Carrito(
                    navController = navController
                ) // Llama a tu nuevo Composable
            }
        }
    }
}