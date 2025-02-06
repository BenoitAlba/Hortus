package org.alba.hortus.utils

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import org.alba.hortus.di.androidContext

actual fun vibratePhone() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val vibratorManager =
            androidContext.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
        val vibrator = vibratorManager.defaultVibrator
        val effect = VibrationEffect.createPredefined(VibrationEffect.EFFECT_TICK)
        vibrator.vibrate(effect)
    } else {
        @Suppress("DEPRECATION")
        val vibratorManager = androidContext.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vibratorManager.vibrate(100)
    }
}