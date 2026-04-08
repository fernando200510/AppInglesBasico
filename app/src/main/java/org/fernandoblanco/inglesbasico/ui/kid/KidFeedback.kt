package org.fernandoblanco.inglesbasico.ui.kid

import android.content.Context
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.Handler
import android.os.Looper
import android.os.VibratorManager
import androidx.core.content.ContextCompat

object KidFeedback {

    private val mainHandler = Handler(Looper.getMainLooper())

    fun playCorrect(context: Context) {
        vibrate(context, short = true)
        try {
            val tg = ToneGenerator(AudioManager.STREAM_MUSIC, 80)
            tg.startTone(ToneGenerator.TONE_PROP_ACK, 120)
            mainHandler.postDelayed({ runCatching { tg.release() } }, 220)
        } catch (_: Exception) { }
    }

    fun playIncorrect(context: Context) {
        vibrate(context, short = false)
        try {
            val tg = ToneGenerator(AudioManager.STREAM_MUSIC, 70)
            tg.startTone(ToneGenerator.TONE_PROP_NACK, 180)
            mainHandler.postDelayed({ runCatching { tg.release() } }, 280)
        } catch (_: Exception) { }
    }

    private fun vibrate(context: Context, short: Boolean) {
        val v = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vm = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
            vm?.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            ContextCompat.getSystemService(context, Vibrator::class.java)
        } ?: return
        val duration = if (short) 35L else 80L
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            @Suppress("DEPRECATION")
            v.vibrate(duration)
        }
    }
}
