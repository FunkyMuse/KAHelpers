package dev.funkymuse.kotlinextensions.power

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.PowerManager
import androidx.annotation.RequiresApi
import androidx.core.content.getSystemService




private val Context.powerManager get() = getSystemService<PowerManager>()

val Context.isInPowerSaveMode get() = powerManager?.isPowerSaveMode

val Context.thermalStatus
    @RequiresApi(Build.VERSION_CODES.Q)
    get() = powerManager?.currentThermalStatus

val Context.locationPowerSaveMode
    @RequiresApi(Build.VERSION_CODES.P)
    get() = powerManager?.locationPowerSaveMode

/**
 *
 * @receiver Context
 * @param level Int
 * @return Boolean?
 */
fun Context.isWakeLockLevelSupported(level: Int): Boolean? {
    return powerManager?.isWakeLockLevelSupported(level)
}

/**
 * Creates new wakelock
 * Creates a new wake lock with the specified level and flags.

The levelAndFlags parameter specifies a wake lock level and optional flags combined using the logical OR operator.

The wake lock levels are: PARTIAL_WAKE_LOCK, FULL_WAKE_LOCK, SCREEN_DIM_WAKE_LOCK and SCREEN_BRIGHT_WAKE_LOCK.
Exactly one wake lock level must be specified as part of the levelAndFlags parameter.

The wake lock flags are: ACQUIRE_CAUSES_WAKEUP and ON_AFTER_RELEASE. Multiple flags can be combined as part of the levelAndFlags parameters.

Call WakeLock#acquire() on the object to acquire the wake lock, and WakeLock#release when you are done.

If using this to keep the screen on, you should strongly consider using WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON instead
 * @receiver Context
 * @param levelFlags Int
 * @param tag String
 */
fun Context.createNewWakeLock(levelFlags: Int, tag: String): PowerManager.WakeLock? {
    return powerManager?.newWakeLock(levelFlags, tag)
}


val Context.isSustainedPerformanceModeSupported
    @RequiresApi(Build.VERSION_CODES.N)
    get() = powerManager?.isSustainedPerformanceModeSupported


val Context.isInInteractiveState get() = powerManager?.isInteractive

val Context.isIgnoringBatteryOptimization
    get() = powerManager?.isIgnoringBatteryOptimizations(packageName)


val Context.isDeviceIdle
    get() = powerManager?.isDeviceIdleMode

fun Context.permissionGranted(permission: String) =
    packageManager.checkPermission(permission, packageName) == PackageManager.PERMISSION_GRANTED


fun Context.getDozeState(): DozeState {
    val powerManager = powerManager ?: return DozeState.ERROR_GETTING_STATE
    return if (powerManager.isDeviceIdleMode) DozeState.DOZE_TURNED_ON_IDLE else if (powerManager.isInteractive) DozeState.NORMAL_INTERACTIVE else DozeState.NORMAL_NON_INTERACTIVE
}

enum class DozeState {
    NORMAL_INTERACTIVE, DOZE_TURNED_ON_IDLE, NORMAL_NON_INTERACTIVE, ERROR_GETTING_STATE, UNKNOWN_TOO_OLD_ANDROID_API_FOR_CHECKING
}


