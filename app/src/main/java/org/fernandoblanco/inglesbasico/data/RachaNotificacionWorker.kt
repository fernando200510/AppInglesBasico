package org.fernandoblanco.inglesbasico.data

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import org.fernandoblanco.inglesbasico.MainActivity
import org.fernandoblanco.inglesbasico.R
import org.fernandoblanco.inglesbasico.db.InglesDatabase

class RachaNotificacionWorker(
    private val context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    companion object {
        const val CANAL_ID = "racha_recordatorio"
        const val NOMBRE_CANAL = "Recordatorio de racha"
        const val TRABAJO_ID = "racha_diaria"
    }

    override suspend fun doWork(): Result {
        val db = InglesDatabase.obtener(context)
        val sesion = SesionUsuario(context)
        val padreId = sesion.padreIdActivo ?: return Result.success()
        val ninos = db.ninoDao().obtenerPorPadre(padreId)
        if (ninos.isEmpty()) return Result.success()

        val ninoActivo = ninos.firstOrNull { it.rachaActual > 0 } ?: ninos.first()
        val mensaje = when {
            ninoActivo.rachaActual >= 7 -> "🔥 ¡${ninoActivo.rachaActual} días seguidos! No pierdas tu racha hoy"
            ninoActivo.rachaActual >= 3 -> "⭐ ¡Llevas ${ninoActivo.rachaActual} días! Sigue jugando para mantener tu racha"
            ninoActivo.rachaActual > 0 -> "📚 ¡Hola ${ninoActivo.nombreMostrar}! No olvides practicar inglés hoy"
            else -> "📚 ¡Hola ${ninoActivo.nombreMostrar}! ¿Jugamos inglés hoy?"
        }

        mostrarNotificacion(ninoActivo.nombreMostrar, mensaje)
        return Result.success()
    }

    private fun mostrarNotificacion(nombre: String, mensaje: String) {
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val canal = NotificationChannel(
                CANAL_ID,
                NOMBRE_CANAL,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Recordatorio diario para mantener la racha de aprendizaje"
            }
            manager.createNotificationChannel(canal)
        }

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notificacion = NotificationCompat.Builder(context, CANAL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("¡Inglés Divertido te espera!")
            .setContentText(mensaje)
            .setStyle(NotificationCompat.BigTextStyle().bigText(mensaje))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        manager.notify(1001, notificacion)
    }
}