package com.kaiju.app.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kaiju.app.model.RolUsuario

@Composable
fun InsigniaRol(rol: RolUsuario) {
    val (fondo, textoColor, etiqueta) = when (rol) {
        RolUsuario.ADMINISTRADOR -> Triple(Color(0xFFF3E5F5), Color(0xFF7B1FA2), "Admin")
        RolUsuario.VENDEDOR -> Triple(Color(0xFFE3F2FD), Color(0xFF1565C0), "Vendedor")
        RolUsuario.ENCARGADO_INVENTARIO -> Triple(Color(0xFFFFF3E0), Color(0xFFE65100), "Inventario")
    }

    Surface(
        color = fondo,
        shape = RoundedCornerShape(50)
    ) {
        Text(
            text = etiqueta,
            color = textoColor,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        )
    }
}