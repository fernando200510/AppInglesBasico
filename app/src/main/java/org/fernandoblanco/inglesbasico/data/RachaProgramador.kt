package org.fernandoblanco.inglesbasico.data

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.Calendar
import java.util.concurrent.TimeUnit

object RachaProgramador {

    fun programar(context: Context) {
        val ahora = Calendar.getInstance()
        val objetivo = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 18)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            if (before(ahora)) add(Calendar.DAY_OF_YEAR, 1)
        }
        val demora = objetivo.timeInMillis - ahora.timeInMillis

        val solicitud = PeriodicWorkRequestBuilder<RachaNotificacionWorker>(
            1, TimeUnit.DAYS
        )
            .setInitialDelay(demora, TimeUnit.MILLISECONDS)
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
                    .build()
            )
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            RachaNotificacionWorker.TRABAJO_ID,
            ExistingPeriodicWorkPolicy.KEEP,
            solicitud
        )
    }

    fun cancelar(context: Context) {
        WorkManager.getInstance(context).cancelUniqueWork(RachaNotificacionWorker.TRABAJO_ID)
    }
}