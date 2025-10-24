package com.example.loopieapp.View

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.loopieapp.Components.CenterAlignedTopAppBarComponent
import com.example.loopieapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController : NavController) { //llamamos al navcontrollerdentro de los parametros
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.loopie),
                "Logo App",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                contentScale = ContentScale.Fit
            )
            Text(
                text = "Inicia sesión para continuar",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth(), //Row ocupa todo el ancho
                horizontalArrangement = Arrangement.SpaceEvenly //Distribuye los botones uniformemente
            ) {
                Button(
                    onClick = { navController.navigate("InicioSesion") },
                    colors = ButtonDefaults.buttonColors(
                        Color(0xff847996),
                        Color.White
                    ),
                    modifier = Modifier.weight(1f)
                        .padding(horizontal = 4.dp) //Cada botón ocupa el mismo espacio
                ) { Text("Iniciar Sesión") }
                Button(
                    onClick = { navController.navigate("FormularioRegistro") },
                    colors = ButtonDefaults.buttonColors(
                        Color(0xff847996),
                        Color.White
                    ),
                    modifier = Modifier.weight(1f)
                        .padding(horizontal = 4.dp) //Cada botón ocupa el mismo espacio
                ) { Text("Registrarse") }
            }

            Spacer(modifier = Modifier.height(16.dp))

        }
    }
}