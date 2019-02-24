package com.crazylegend.kotlinextensions.context

import android.app.*
import android.app.admin.DevicePolicyManager
import android.app.job.JobScheduler
import android.appwidget.AppWidgetManager
import android.bluetooth.BluetoothManager
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.*
import android.content.Intent
import android.content.res.Configuration
import android.graphics.drawable.Drawable
import android.hardware.camera2.CameraManager
import android.media.AudioManager
import android.net.ConnectivityManager
import android.net.Uri
import android.os.BatteryManager
import android.os.Build
import android.os.Vibrator
import android.telephony.TelephonyManager
import android.view.inputmethod.InputMethodManager
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat


/**
 * Created by hristijan on 2/22/19 to long live and prosper !
 */

/**
 * Get inputManager for Context.
 */
inline val Context.inputManager: InputMethodManager?
    get() = getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager

/**
 * Get notificationManager for Context.
 */
inline val Context.notificationManager: NotificationManager?
    get() = getSystemService(NOTIFICATION_SERVICE) as? NotificationManager

/**
 * Get keyguardManager for Context.
 */
inline val Context.keyguardManager: KeyguardManager?
    get() = getSystemService(KEYGUARD_SERVICE) as? KeyguardManager


/**
 * Get telephonyManager for Context.
 */
inline val Context.telephonyManager: TelephonyManager?
    get() = getSystemService(TELEPHONY_SERVICE) as? TelephonyManager


/**
 * Get devicePolicyManager for Context.
 */
inline val Context.devicePolicyManager: DevicePolicyManager?
    get() = getSystemService(DEVICE_POLICY_SERVICE) as? DevicePolicyManager


/**
 * Get connectivityManager for Context.
 */
inline val Context.connectivityManager: ConnectivityManager?
    get() = getSystemService(CONNECTIVITY_SERVICE) as? ConnectivityManager


/**
 *Get alarmManager for Context.
 */
inline val Context.alarmManager: AlarmManager?
    get() = getSystemService(ALARM_SERVICE) as? AlarmManager


/**
 * Get clipboardManager for Context.
 */
inline val Context.clipboardManager: ClipboardManager?
    get() = getSystemService(CLIPBOARD_SERVICE) as? ClipboardManager


/**
 * Get jobScheduler for Context.
 */
inline val Context.jobScheduler: JobScheduler?
    get() = getSystemService(JOB_SCHEDULER_SERVICE) as? JobScheduler


/**
 * Get notification for Context.
 */
inline fun Context.notification(body: NotificationCompat.Builder.() -> Unit, channelID:String): Notification {
    val builder = NotificationCompat.Builder(this, channelID)
    builder.body()
    return builder.build()
}


/**
 * Get color from resources
 */
fun Context.getCompatColor(@ColorRes colorInt: Int) : Int =
    ContextCompat.getColor(this, colorInt)

/**
 * Get drawable from resources
 */
fun Context.getCompatDrawable(@DrawableRes drawableRes: Int) : Drawable? =
    ContextCompat.getDrawable(this, drawableRes)



inline val Context.activityManager: ActivityManager
    get() = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager


inline val Context.appWidgetManager
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    get() = getSystemService(Context.APPWIDGET_SERVICE) as AppWidgetManager

inline val Context.inputMethodManager: InputMethodManager
    get() = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

inline val Context.bluetoothManager: BluetoothManager
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    get() = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager

inline val Context.audioManager
    get() = getSystemService(Context.AUDIO_SERVICE) as AudioManager

inline val Context.batteryManager
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    get() = getSystemService(Context.BATTERY_SERVICE) as BatteryManager

inline val Context.cameraManager
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    get() = getSystemService(Context.CAMERA_SERVICE) as CameraManager

inline val Context.vibrator
    get() = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

fun Context.dial(tel: String?) = startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:$tel")))

fun Context.isLandscape() = resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

fun Context.isPortrait() = resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT
