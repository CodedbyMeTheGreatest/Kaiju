package com.kaiju.app.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.kaiju.app.model.puedeGestionarProductos
import com.kaiju.app.model.puedeVerAlertas
import com.kaiju.app.model.tiposMovimientoPermitidos
import com.kaiju.app.ui.screen.PantallaAlertas
import com.kaiju.app.ui.screen.PantallaDetalleProducto
import com.kaiju.app.ui.screen.PantallaFormularioProductoEvento
import com.kaiju.app.ui.screen.PantallaInicioSesionEvento
import com.kaiju.app.ui.screen.PantallaListaProductos
import com.kaiju.app.ui.screen.PantallaMovimiento
import com.kaiju.app.viewmodel.AlertasViewModel
import com.kaiju.app.viewmodel.AlertasViewModelFactory
import com.kaiju.app.viewmodel.DetalleProductoViewModel
import com.kaiju.app.viewmodel.DetalleProductoViewModelFactory
import com.kaiju.app.viewmodel.FormularioMovimientoViewModel
import com.kaiju.app.viewmodel.FormularioMovimientoViewModelFactory
import com.kaiju.app.viewmodel.FormularioProductoViewModel
import com.kaiju.app.viewmodel.FormularioProductoViewModelFactory
import com.kaiju.app.viewmodel.InicioSesionViewModel
import com.kaiju.app.viewmodel.ListaProductosViewModel
import com.kaiju.app.viewmodel.ListaProductosViewModelFactory
import com.kaiju.app.viewmodel.SesionViewModel

@Composable
fun AppNavHost(
    viewModelInicioSesion: InicioSesionViewModel = viewModel(),
    sesion: SesionViewModel = viewModel(),
    viewModelFormularioProducto: FormularioProductoViewModel = viewModel(factory = FormularioProductoViewModelFactory(
                   LocalContext.current)),
    viewModelFormularioMovimiento: FormularioMovimientoViewModel =
        viewModel(factory = FormularioMovimientoViewModelFactory(LocalContext.current)),
    viewModelListaProductos: ListaProductosViewModel = viewModel(factory = ListaProductosViewModelFactory(
                   LocalContext.current)), viewModelAlertas: AlertasViewModel = viewModel(factory = AlertasViewModelFactory(
        LocalContext.current
    )
    )

    ) {
    val navController = rememberNavController()
    val context = LocalContext.current

    val backStackEntry by navController.currentBackStackEntryAsState()
    val rutaActual = backStackEntry?.destination?.route

    val usuario by sesion.usuarioActivo.collectAsState()
    val rol = usuario?.rol

    LaunchedEffect(usuario, rutaActual) {
        if (usuario == null && rutaActual != null && rutaActual != Rutas.InicioSesion.ruta) {
            navController.navigate(Rutas.InicioSesion.ruta) {
                popUpTo(navController.graph.id) { inclusive = true }
            }
        }
    }

    fun irAPestana(ruta: String) {
        navController.navigate(ruta) {
            popUpTo(Rutas.Catalogo.ruta) { saveState = true }
            launchSingleTop = true
            restoreState = true
        }
    }

    Scaffold(
        bottomBar = {
            if (rol != null) {
                NavigationBar {
                    NavigationBarItem(
                        selected = rutaActual == Rutas.Catalogo.ruta,
                        onClick = { irAPestana(Rutas.Catalogo.ruta) },
                        icon = {Icon(Icons.AutoMirrored.Filled.List, contentDescription = null)},
                        label = {Text("Catalogo")}
                    )
                    NavigationBarItem(
                        selected = rutaActual == Rutas.GestionMovimiento.ruta,
                        onClick = { irAPestana(Rutas.GestionMovimiento.ruta) },
                        icon = { Icon(Icons.Default.Add, contentDescription = null) },
                        label = { Text("Movimiento") }
                    )
                    if (rol.puedeVerAlertas()) {
                        NavigationBarItem(
                            selected = rutaActual == Rutas.Alertas.ruta,
                            onClick = { irAPestana(Rutas.Alertas.ruta) },
                            icon = { Icon(Icons.Default.Warning, contentDescription = null) },
                            label = { Text("Alertas") }
                        )
                    }
                    NavigationBarItem(
                        selected = false,
                        onClick = {
                            sesion.cerrarSesion()
                            viewModelFormularioMovimiento.reiniciarFormulario()
                        },
                        icon = { Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = null) },
                        label = { Text("Salir") }
                    )

                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Rutas.InicioSesion.ruta,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Rutas.InicioSesion.ruta) {
                PantallaInicioSesionEvento(
                    viewModel = viewModelInicioSesion,
                    onLoginExitoso = { u ->
                        sesion.iniciar(u)
                        navController.navigate(Rutas.Catalogo.ruta) {
                            popUpTo(Rutas.InicioSesion.ruta) { inclusive = true }
                        }
                    }
                )
            }
            composable(Rutas.Catalogo.ruta){
                val alAgregar: (() -> Unit)? =
                    if (rol?.puedeGestionarProductos()==true){
                        {navController.navigate(Rutas.GestionProducto.ruta)}
                    } else null
                PantallaListaProductos(
                    viewModel =  viewModelListaProductos,
                    onAgregarProducto = alAgregar,
                    onProductoClick = { codigoProducto -> navController.navigate(Rutas.DetalleProducto.crearRuta(codigoProducto)) }
                )
            }
            composable(Rutas.GestionProducto.ruta){
                if (rol?.puedeGestionarProductos()==true){
                    PantallaFormularioProductoEvento(viewModelFormularioProducto)
                }
            }
            composable(Rutas.GestionMovimiento.ruta){
                val u = usuario ?: return@composable
                PantallaMovimiento(
                viewModel = viewModelFormularioMovimiento,
                nombreUsuario = u.nombre,
                tiposPermitidos = u.rol.tiposMovimientoPermitidos(),
                notificarAlertas = u.rol.puedeVerAlertas()
                )
            }
            composable(
                route = Rutas.DetalleProducto.ruta,
                arguments = listOf(navArgument("codigoProducto") { type = NavType.StringType })
            ) {
                backStack ->
                val codigoProducto = backStack.arguments?.getString("codigoProducto")
                val detalleViewModel: DetalleProductoViewModel = viewModel(factory = DetalleProductoViewModelFactory(context, codigoProducto!!))
                PantallaDetalleProducto(
                    viewModel = detalleViewModel,
                    onVolver = { navController.popBackStack() })
            }
            composable(Rutas.Alertas.ruta) {
                if (rol?.puedeVerAlertas() == true){
                    PantallaAlertas(viewModel = viewModelAlertas)
                }
            }
        }

    }
}
