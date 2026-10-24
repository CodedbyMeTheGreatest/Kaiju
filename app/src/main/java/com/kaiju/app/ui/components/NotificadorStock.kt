package com.kaiju.app.ui.components

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.kaiju.app.model.Producto

object NotificadorStock {

    private const val CANAL_ID = "alertas_stock"

    fun crearCanal(context: Context) {
        val canal = NotificationChannel(
            CANAL_ID,
            "Alertas de stock",
            NotificationManager.IMPORTANCE_HIGH
        ).apply { description = "Avisa cuando un producto llega a su stock mínimo" }
        context.getSystemService(NotificationManager::class.java).createNotificationChannel(canal)
    }
    fun tienePermiso(context: Context): Boolean =
        Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
                ContextCompat.checkSelfPermission(
                    context, Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED

    @SuppressLint("MissingPermission")
    fun notificarStockBajo(context: Context, producto: Producto) {
        if (!tienePermiso(context)) return

        val intentApp = context.packageManager.getLaunchIntentForPackage(context.packageName)
        val abrirApp = intentApp?.let {
            PendingIntent.getActivity(
                context, 0, it,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
        }

        val titulo = if (producto.stockActual == 0) "Producto agotado" else "Stock bajo"
        val notificacion = NotificationCompat.Builder(context, CANAL_ID)
            .setSmallIcon(android.R.drawable.stat_sys_warning)
            .setContentTitle("$titulo: ${producto.nombre}")
            .setContentText("Quedan ${producto.stockActual} (mínimo ${producto.stockMinimo})")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(abrirApp)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(context).notify(producto.codigo.hashCode(), notificacion)
    }
}