package com.kaiju.app.ui.components

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.provider.Settings
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.lifecycle.compose.LifecycleResumeEffect

@SuppressLint("InlinedApi")
@Composable
fun AvisoNotificaciones(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val activity = LocalActivity.current
    var tienePermiso by remember { mutableStateOf(NotificadorStock.tienePermiso(context)) }
    var mostrarExplicacion by remember { mutableStateOf(false) }
    var rechazado by remember { mutableStateOf(false) }

    LifecycleResumeEffect(Unit) {
        tienePermiso = NotificadorStock.tienePermiso(context)
        onPauseOrDispose { }
    }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { concedido ->
        tienePermiso = concedido
        rechazado = !concedido
    }

    if (!tienePermiso) {
        Card(modifier = modifier.fillMaxWidth().padding(16.dp)) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                if (rechazado) {
                    Text("Sin avisos activados. Las alertas de igual manera permaneceran visibles en su pantalla.")
                    TextButton(onClick = {
                        context.startActivity(
                            Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                                .putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                        )
                    }) { Text("Abrir Ajustes") }
                } else {
                    Text("Activa los avisos para enterarte al instante cuando un producto llegue a su stock mínimo.")
                    Button(onClick = {
                        if (activity != null && ActivityCompat.shouldShowRequestPermissionRationale(
                                activity, Manifest.permission.POST_NOTIFICATIONS
                            )
                        ) mostrarExplicacion = true
                        else launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    }) { Text("Activar avisos de stock bajo") }
                }
            }
        }
    }

    if (mostrarExplicacion) {
        AlertDialog(
            onDismissRequest = { mostrarExplicacion = false },
            title = { Text("Avisos de stock bajo") },
            text = { Text("Solo te enviaremos una notificación cuando un producto llegue a su stock mínimo.") },
            confirmButton = {
                TextButton(onClick = {
                    mostrarExplicacion = false
                    launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }) { Text("Continuar") }
            },
            dismissButton = {
                TextButton(onClick = {
                    mostrarExplicacion = false
                    rechazado = true
                }) { Text("Ahora no") }
            }
        )
    }
}