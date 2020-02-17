package com.crazylegend.kotlinextensions.version

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi


/**
 * Created by hristijan on 3/4/19 to long live and prosper !
 */

inline fun toApi(toVersion: Int, inclusive: Boolean = false, action: () -> Unit) {
    if (Build.VERSION.SDK_INT < toVersion || (inclusive && Build.VERSION.SDK_INT == toVersion)) action()
}

inline fun fromApi(fromVersion: Int, inclusive: Boolean = true, action: () -> Unit) {
    if (Build.VERSION.SDK_INT > fromVersion || (inclusive && Build.VERSION.SDK_INT == fromVersion)) action()
}

/**
 * Execute [f] only if the current Android SDK version is [version] or newer. Optionally, execute [else] if the
 * current Android SDK version is lower than the provided one.
 */
fun doStartingFromSdk(version: Int, f: () -> Unit, `else`: () -> Unit = {}) {
    if (Build.VERSION.SDK_INT >= version) {
        f()
    } else {
        `else`()
    }
}

/**
 * Execute [f] only if the current Android SDK version is [version] or newer.
 */
fun doStartingFromSdk(version: Int, f: () -> Unit) {
    if (Build.VERSION.SDK_INT >= version) {
        f()
    }
}

/**
 * Execute [f] only if the current Android SDK version is [version] Optionally, execute [else] if the
 * current Android SDK version doesn't match the provided one.
 */
fun doIfSdk(version: Int, f: () -> Unit, `else`: () -> Unit = {}) {
    if (Build.VERSION.SDK_INT == version) {
        f()
    } else {
        `else`()
    }
}

/**
 * Execute [f] only if the current Android SDK version is [version]
 */
fun doIfSdk(version: Int, f: () -> Unit) {
    if (Build.VERSION.SDK_INT == version) {
        f()
    }
}

val Context.getVersionCode: Long
    @RequiresApi(Build.VERSION_CODES.P)
    get() = packageManager.getPackageInfo(packageName, 0).longVersionCode

fun Context.getVersionCode(): Int = packageManager.getPackageInfo(packageName, 0).versionCode

fun Context.getVersionName(): String = packageManager.getPackageInfo(packageName, 0).versionName

inline fun doWithApi(api: Api, block: () -> Unit) {
    doWithApi(api.sdkCode, block)
}

inline fun doWithApi(sdkCode: Int, block: () -> Unit) {
    if (Build.VERSION.SDK_INT == sdkCode) {
        block()
    }
}

inline fun doWithAtLeastApi(api: Api, block: () -> Unit) {
    doWithAtLeastApi(api.sdkCode, block)
}

inline fun doWithAtLeastApi(sdkCode: Int, block: () -> Unit) {
    if (Build.VERSION.SDK_INT >= sdkCode) {
        block()
    }
}

inline fun doWithAtMostApi(api: Api, block: () -> Unit) {
    doWithAtMostApi(api.sdkCode, block)
}

inline fun doWithAtMostApi(sdkCode: Int, block: () -> Unit) {
    if (Build.VERSION.SDK_INT <= sdkCode) {
        block()
    }
}

inline fun doWithHigherApi(api: Api, block: () -> Unit) {
    doWithHigherApi(api.sdkCode, block)
}

inline fun doWithHigherApi(sdkCode: Int, block: () -> Unit) {
    if (Build.VERSION.SDK_INT > sdkCode) {
        block()
    }
}

inline fun doWithLowerApi(api: Api, block: () -> Unit) {
    doWithLowerApi(api.sdkCode, block)
}

inline fun doWithLowerApi(sdkCode: Int, block: () -> Unit) {
    if (Build.VERSION.SDK_INT < sdkCode) {
        block()
    }
}

fun isLollipop(): Boolean =
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP


enum class Api(val sdkCode: Int) {


    BASE(1),
    BASE_1_1(2),
    CUPCAKE(3),
    CUR_DEVELOPMENT(10000),
    DONUT(4),
    ECLAIR(5),
    ECLAIR_0_1(6),
    ECLAIR_MR1(7),
    FROYO(8),
    GINGERBREAD(9),
    GINGERBREAD_MR1(10),
    HONEYCOMB(11),
    HONEYCOMB_MR1(12),
    HONEYCOMB_MR2(13),
    ICE_CREAM_SANDWICH(14),
    ICE_CREAM_SANDWICH_MR1(15),
    JELLY_BEAN(16),
    JELLY_BEAN_MR1(17),
    JELLY_BEAN_MR2(18),
    KITKAT(19),
    KITKAT_WATCH(20),
    LOLLIPOP(21),
    LOLLIPOP_MR1(22),
    M(23),
    N(24),
    N_MR1(25),
    O(26),
    P(27),
    Q(29)


}