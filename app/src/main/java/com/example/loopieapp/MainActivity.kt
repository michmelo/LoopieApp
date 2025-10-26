package com.example.loopieapp

import FormularioRegistro
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.loopieapp.Components.MainScreen
import com.example.loopieapp.Model.AppDatabase
import com.example.loopieapp.Repository.UsuarioRepository
import com.example.loopieapp.View.HomeScreen
import com.example.loopieapp.View.InicioSesion
import com.example.loopieapp.View.PanelVendedor
import com.example.loopieapp.View.PantallaPrincipal
import com.example.loopieapp.View.Perfil
import com.example.loopieapp.ViewModel.UsuarioViewModel
import com.example.loopieapp.ui.theme.LoopieAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
