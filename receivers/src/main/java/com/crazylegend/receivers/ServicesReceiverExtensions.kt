package com.crazylegend.receivers

import android.app.ActivityManager
import android.app.Service
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothProfile
import android.content.*
import android.media.AudioManager
import android.os.Build


/**
 * Created by hristijan on 8/5/19 to long live and prosper !
 */

inline fun Context.registerVolumeChange(crossinline block: (Int) -> Unit): BroadcastReceiver {
    return object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action ?: return
            if (action == "android.media.VOLUME_CHANGED_ACTION") {
                val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
                val currVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
                block(currVolume)
            }
        }
    }.apply {
        val intent = IntentFilter().apply { addAction("android.media.VOLUME_CHANGED_ACTION") }
        this@registerVolumeChange.registerReceiver(this, intent)
    }
}


inline fun Context.registerBluetoothChange(crossinline connectionStateChanged: (Intent, Int) -> Unit): BroadcastReceiver {
    return object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action ?: return
            if (action == BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED) {
                connectionStateChanged(intent, intent.getIntExtra(BluetoothProfile.EXTRA_STATE, -1))
            }
        }
    }.apply {
        val intent =
            IntentFilter().apply { addAction(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED) }
        this@registerBluetoothChange.registerReceiver(this, intent)
    }
}

inline fun Context.registerWifiStateChanged(crossinline callback: (Intent) -> Unit): BroadcastReceiver {
    val action = "android.net.wifi.WIFI_STATE_CHANGED"
    return object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == action) {
                callback(intent)
            }
        }
    }.apply {
        val intent = IntentFilter().apply { addAction(action) }
        this@registerWifiStateChanged.registerReceiver(this, intent)
    }
}

inline fun <reified T : Service> Context.startForegroundService(predicate: Intent.() -> Unit = {}) {
    val intent = Intent(this, T::class.java)
    predicate(intent)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        startForegroundService(intent)
    } else {
        startService(intent)
    }
}

@Suppress("DEPRECATION")
inline fun <reified T : Service> Context.isServiceRunning(): Boolean {
    (this.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager?)?.run {
        for (service in getRunningServices(Integer.MAX_VALUE)) {
            if (T::class.java.name == service.service.className) return true //service.foreground
        }
    }
    return false
}

inline fun <reified T : Service> Context.startServiceUnlessRunning(predicate: Intent.() -> Unit = {}) {
    if (!this.isServiceRunning<T>()) this.startForegroundService<T>(predicate)
}

inline fun <reified T : Service> Context.stopService(): Boolean {
    val intent = Intent(this, T::class.java)
    return stopService(intent)
}