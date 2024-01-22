package dev.funkymuse.audio

import android.media.AudioManager
import android.media.AudioManager.ADJUST_MUTE
import android.media.AudioManager.ADJUST_UNMUTE
import android.media.AudioManager.STREAM_ALARM
import android.media.AudioManager.STREAM_MUSIC
import android.media.AudioManager.STREAM_NOTIFICATION
import android.media.AudioManager.STREAM_RING
import android.media.AudioManager.STREAM_SYSTEM
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.M
import android.view.KeyEvent
import kotlin.math.max
import kotlin.math.min


fun AudioManager.dispatchEvent(keycode: Int) {
    dispatchMediaKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN, keycode))
    dispatchMediaKeyEvent(KeyEvent(KeyEvent.ACTION_UP, keycode))
}

private var isMuted: Boolean = false

var AudioManager.isMuted: Boolean
    get() = dev.funkymuse.audio.isMuted
    private set(value) {
        dev.funkymuse.audio.isMuted = value
    }

@Suppress("DEPRECATION")
fun AudioManager.mute(mute: Boolean = true) {
    isMuted = mute
    if (SDK_INT >= M) {
        val flag = if (mute) ADJUST_MUTE else ADJUST_UNMUTE
        adjustStreamVolume(STREAM_NOTIFICATION, flag, 0)
        adjustStreamVolume(STREAM_ALARM, flag, 0)
        adjustStreamVolume(STREAM_MUSIC, flag, 0)
        adjustStreamVolume(STREAM_RING, flag, 0)
        adjustStreamVolume(STREAM_SYSTEM, flag, 0)
    } else {
        setStreamMute(STREAM_NOTIFICATION, mute)
        setStreamMute(STREAM_ALARM, mute)
        setStreamMute(STREAM_MUSIC, mute)
        setStreamMute(STREAM_RING, mute)
        setStreamMute(STREAM_SYSTEM, mute)
    }
}

@Suppress("DEPRECATION")
fun AudioManager.unmute() {
    mute(false)
}

fun AudioManager.decreaseVolume() {
    changeVolume(-10)
}

fun AudioManager.increaseVolume() {
    changeVolume(10)
}


fun AudioManager.decreaseVolume(volume: Int) {
    changeVolume(volume)
}

fun AudioManager.increaseVolume(volume: Int) {
    changeVolume(volume)
}

fun AudioManager.toggleMute() {
    mute(!isMuted)
}

fun AudioManager.changeVolume(dt: Int) {
    val streamMaxVolume = getStreamVolume(STREAM_MUSIC)
    val volume = clamp((streamMaxVolume + dt).toFloat(), 0F, streamMaxVolume.toFloat())
    setStreamVolume(STREAM_MUSIC, volume.toInt(), 0)
}

private fun clamp(value: Float, min: Float, max: Float): Float = max(min, min(value, max))
