package com.funkymuse.kotlinextensions.wakelock

import android.Manifest.permission.WAKE_LOCK
import android.content.Context
import android.os.Build
import android.os.PowerManager
import androidx.annotation.RequiresPermission
import java.util.Locale

@RequiresPermission(WAKE_LOCK)
fun PowerManager.WakeLock.safeRelease() {
    if (isHeld) {
        release()
    }
}

@RequiresPermission(WAKE_LOCK)
fun Context.huaweiWakeLockTag(customTag: String?): String {
    var tag = customTag ?: "$packageName:LOCK"
    if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M && Build.MANUFACTURER.lowercase(Locale.ENGLISH) == "huawei") {
        tag = "LocationManagerService"
    }
    return tag
}