package com.kaiju.app.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Login
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.kaiju.app.ui.screen.PantallaInicioSesionEvento
import com.kaiju.app.viewmodel.InicioSesionViewModel

@Composable
fun AppNavHost(viewModel: InicioSesionViewModel = viewModel()) {
    val navController = rememberNavController()

    // Ruta de la pantalla que se está mostrando ahora (para marcar el menú).
    val backStackEntry by navController.currentBackStackEntryAsState()
    val rutaActual = backStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = rutaActual == Rutas.InicioSesion.ruta,
                    onClick = { navController.navigate(Rutas.InicioSesion.ruta) { launchSingleTop = true } },
                    icon = { Icon(Icons.AutoMirrored.Filled.Login, contentDescription = null) },
                    label = { Text("Inicio Sesión") }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Rutas.InicioSesion.ruta,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Rutas.InicioSesion.ruta) {
                PantallaInicioSesionEvento(viewModel = viewModel)
            }
        }
    }
}