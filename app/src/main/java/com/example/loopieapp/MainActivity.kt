package com.example.loopieapp


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import kotlinx.coroutines.launch
import androidx.lifecycle.lifecycleScope
import com.example.loopieapp.Components.MainScreen
import com.example.loopieapp.Repository.CountryRepository
import com.example.loopieapp.ui.theme.LoopieAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            CountryRepository().getAllCountries()
        }
        enableEdgeToEdge()
        setContent {
            LoopieAppTheme {
                MainScreen()
                /* Al usar MainScreen para navegar ya no necesito el NavHost
                val navController = rememberNavController()

                //val context = LocalContext.current
                //val db = remember { AppDatabase.getDatabase(context) }
                //val repository = remember { UsuarioRepository(db.usuarioDao()) }
                //val usuarioViewModel = remember { UsuarioViewModel(repository) }

                //val vm: PerfilViewModel = viewModel()
                //PerfilScreen(vm)

                NavHost(
                    navController = navController, 
                    startDestination = "HomeScreen"
                ) {
                    composable("HomeScreen") {
                        HomeScreen(navController)
                    }
                    composable("Perfil") {
                        Perfil(navController)
                    }
                    composable("PanelVendedor") {
                        PanelVendedor(navController)
                    }
                    composable("InicioSesion") {
                        InicioSesion(navController)
                    }
                    composable("FormularioRegistro") {
                        FormularioRegistro(navController)
                    }
                    composable("PantallaPrincipal") {
                        PantallaPrincipal(navController)
                    }
                }*/
            }
        }
    }
}
