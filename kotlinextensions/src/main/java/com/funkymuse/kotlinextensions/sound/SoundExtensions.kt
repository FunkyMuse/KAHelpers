package com.funkymuse.kotlinextensions.sound

import android.Manifest.permission.ACCESS_NOTIFICATION_POLICY
import android.Manifest.permission.MODIFY_AUDIO_SETTINGS
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission

@RequiresPermission(allOf = [MODIFY_AUDIO_SETTINGS])
fun Context.increaseVolume() {
    val audioManager = this.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    audioManager.adjustVolume(AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND)
}

@RequiresPermission(allOf = [MODIFY_AUDIO_SETTINGS])
fun Context.decreaseVolume() {
    val audioManager = this.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    audioManager.adjustVolume(AudioManager.ADJUST_LOWER, AudioManager.FLAG_PLAY_SOUND)
}

@RequiresPermission(allOf = [MODIFY_AUDIO_SETTINGS])
fun Context.putOnSilent() {
    val audioManager = this.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    audioManager.ringerMode = AudioManager.RINGER_MODE_SILENT
}

@RequiresPermission(allOf = [MODIFY_AUDIO_SETTINGS])
fun Context.putOnVibration() {
    val audioManager = this.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    audioManager.ringerMode = AudioManager.RINGER_MODE_VIBRATE
}

@RequiresPermission(allOf = [MODIFY_AUDIO_SETTINGS])
fun Context.putOnNormal() {
    val audioManager = this.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    audioManager.ringerMode = AudioManager.RINGER_MODE_NORMAL
}

@Suppress("DEPRECATION")
@RequiresPermission(allOf = [MODIFY_AUDIO_SETTINGS])
fun Context.muteAudio() {
    val audioManager = this.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        audioManager.adjustStreamVolume(AudioManager.STREAM_NOTIFICATION, AudioManager.ADJUST_MUTE, 0)
        audioManager.adjustStreamVolume(AudioManager.STREAM_ALARM, AudioManager.ADJUST_MUTE, 0)
        audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_MUTE, 0)
        audioManager.adjustStreamVolume(AudioManager.STREAM_RING, AudioManager.ADJUST_MUTE, 0)
        audioManager.adjustStreamVolume(AudioManager.STREAM_SYSTEM, AudioManager.ADJUST_MUTE, 0)
    } else {
        audioManager.setStreamMute(AudioManager.STREAM_NOTIFICATION, true)
        audioManager.setStreamMute(AudioManager.STREAM_ALARM, true)
        audioManager.setStreamMute(AudioManager.STREAM_MUSIC, true)
        audioManager.setStreamMute(AudioManager.STREAM_RING, true)
        audioManager.setStreamMute(AudioManager.STREAM_SYSTEM, true)
    }
}

@Suppress("DEPRECATION")
@RequiresPermission(allOf = [MODIFY_AUDIO_SETTINGS])
fun Context.unMuteAudio() {
    val audioManager = this.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        audioManager.adjustStreamVolume(AudioManager.STREAM_NOTIFICATION, AudioManager.ADJUST_UNMUTE, 0)
        audioManager.adjustStreamVolume(AudioManager.STREAM_ALARM, AudioManager.ADJUST_UNMUTE, 0)
        audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_UNMUTE, 0)
        audioManager.adjustStreamVolume(AudioManager.STREAM_RING, AudioManager.ADJUST_UNMUTE, 0)
        audioManager.adjustStreamVolume(AudioManager.STREAM_SYSTEM, AudioManager.ADJUST_UNMUTE, 0)
    } else {
        audioManager.setStreamMute(AudioManager.STREAM_NOTIFICATION, false)
        audioManager.setStreamMute(AudioManager.STREAM_ALARM, false)
        audioManager.setStreamMute(AudioManager.STREAM_MUSIC, false)
        audioManager.setStreamMute(AudioManager.STREAM_RING, false)
        audioManager.setStreamMute(AudioManager.STREAM_SYSTEM, false)
    }
}

@RequiresApi(api = Build.VERSION_CODES.M)
@RequiresPermission(allOf = [ACCESS_NOTIFICATION_POLICY])
fun Context.setToSilent() {
    val notificationManager = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !notificationManager.isNotificationPolicyAccessGranted) {

        val intent = Intent(
                android.provider.Settings
                        .ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS
        )

        startActivity(intent)
    }
}
