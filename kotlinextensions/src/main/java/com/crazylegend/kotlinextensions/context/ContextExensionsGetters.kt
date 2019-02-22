package com.crazylegend.kotlinextensions.context

import android.app.AlarmManager
import android.app.KeyguardManager
import android.app.Notification
import android.app.NotificationManager
import android.app.admin.DevicePolicyManager
import android.app.job.JobScheduler
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.*
import android.net.ConnectivityManager
import android.telephony.TelephonyManager
import android.view.inputmethod.InputMethodManager
import androidx.core.app.NotificationCompat


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