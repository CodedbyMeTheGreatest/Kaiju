package com.kaiju.app.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.kaiju.app.model.EstadoBoton

@Composable
fun BotonGuardarAnimado(estado: EstadoBoton, onClick: () -> Unit, txtEjemplar: String) {

    val containerColor = when (estado) {
        EstadoBoton.INACTIVO -> MaterialTheme.colorScheme.primary
        EstadoBoton.CARGANDO -> MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
        EstadoBoton.EXITOSO -> Color(0xFF2E7D32)
    }
    val contentColor = when (estado) {
        EstadoBoton.INACTIVO -> MaterialTheme.colorScheme.onPrimary
        EstadoBoton.CARGANDO -> MaterialTheme.colorScheme.onPrimary
        EstadoBoton.EXITOSO -> Color.White
    }
    Button(
        onClick = onClick,
        enabled = estado === EstadoBoton.INACTIVO,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor,
            disabledContainerColor = containerColor,
            disabledContentColor = contentColor
        ),
        modifier = Modifier.fillMaxWidth()
    ){
        AnimatedContent(
            targetState = estado,
            transitionSpec = {
                (fadeIn() + scaleIn(initialScale = 0.8f)) togetherWith fadeOut()
            },
            label = "BotonGuardarAnimado"
        ) { targetEstado ->
            when(targetEstado){
                EstadoBoton.INACTIVO -> Text("Registrar $txtEjemplar")
                EstadoBoton.CARGANDO -> CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = contentColor,
                    strokeWidth = 2.dp
                )
                EstadoBoton.EXITOSO -> {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ){
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Éxito",
                            tint = contentColor
                        )
                        Text("¡Guardado!", color = contentColor)
                    }
                }
            }
        }
    }
}