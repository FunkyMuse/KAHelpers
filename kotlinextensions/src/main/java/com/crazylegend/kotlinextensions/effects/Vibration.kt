@file:Suppress("DEPRECATION")

package com.crazylegend.kotlinextensions.effects

import android.Manifest.permission.VIBRATE
import android.content.Context
import android.content.Context.VIBRATOR_SERVICE
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.annotation.RequiresPermission


/**
 * Created by hristijan on 2/22/19 to long live and prosper !
 */

/**
 * Vibrate from context with milliseconds
 */
@RequiresPermission(allOf = [VIBRATE])
fun Context.vibrate(milliseconds: Long) {
    if (Build.VERSION.SDK_INT >= 26) {
        (getSystemService(VIBRATOR_SERVICE) as Vibrator).vibrate(
                VibrationEffect.createOneShot(milliseconds, VibrationEffect.DEFAULT_AMPLITUDE)
        )
    } else {
        (getSystemService(VIBRATOR_SERVICE) as Vibrator).vibrate(milliseconds)
    }
}

/**
 * Vibrate from context with milliseconds
 */
@RequiresPermission(allOf = [VIBRATE])
fun Context.vibrate(milliseconds: Long, effect: Int? = null) {
    if (Build.VERSION.SDK_INT >= 26) {
        (getSystemService(VIBRATOR_SERVICE) as Vibrator).vibrate(VibrationEffect.createOneShot(milliseconds,
                effect ?: VibrationEffect.DEFAULT_AMPLITUDE))
    } else {
        (getSystemService(VIBRATOR_SERVICE) as Vibrator).vibrate(milliseconds)
    }
}


/**
 * Check whether device has Vibrator
 */
val Context.hasVibrator: Boolean
    get() {
        val vibrator = this.getSystemService(VIBRATOR_SERVICE) as Vibrator
        return vibrator.hasVibrator()
    }


/**
 * Vibrate from context with pattern and repeat
 */
@RequiresPermission(allOf = [VIBRATE])
fun Context.vibrate(pattern: LongArray, repeat: Int) {
    if (Build.VERSION.SDK_INT >= 26) {
        (getSystemService(VIBRATOR_SERVICE) as Vibrator).vibrate(VibrationEffect.createWaveform(pattern, repeat))
    } else {
        (getSystemService(VIBRATOR_SERVICE) as Vibrator).vibrate(pattern, repeat)
    }
}

/**
 * Vibrate from context with pattern, repeat and amplitudes
 */
@RequiresPermission(allOf = [VIBRATE])
fun Context.vibrate(pattern: LongArray, repeat: Int, amplitudes: IntArray) {
    if (Build.VERSION.SDK_INT >= 26) {
        (getSystemService(VIBRATOR_SERVICE) as Vibrator).vibrate(VibrationEffect.createWaveform(pattern, amplitudes, repeat))
    } else {
        (getSystemService(VIBRATOR_SERVICE) as Vibrator).vibrate(pattern, repeat)
    }
}