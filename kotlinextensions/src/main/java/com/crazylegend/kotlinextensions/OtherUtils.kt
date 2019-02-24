package com.crazylegend.kotlinextensions

import android.os.Build
import android.os.Handler
import android.os.Looper
import java.util.concurrent.TimeUnit


/**
 * Created by hristijan on 2/22/19 to long live and prosper borrowed from http://kotlinextensions.com
 */


/**
 * Method to check is aboveApi.
 */
inline fun aboveApi(api: Int, included: Boolean = false, block: () -> Unit) {
    if (Build.VERSION.SDK_INT > if (included) api - 1 else api) {
        block()
    }
}

/**
 * Method to check is belowApi.
 */
inline fun belowApi(api: Int, included: Boolean = false, block: () -> Unit) {
    if (Build.VERSION.SDK_INT < if (included) api + 1 else api) {
        block()
    }
}


/**
 * Check if is Main Thread.
 */
fun isMainThread(): Boolean = Looper.myLooper() == Looper.getMainLooper()


/**
 * Extension method to run block of code after specific Delay.
 */
fun runDelayed(delay: Long, timeUnit: TimeUnit = TimeUnit.MILLISECONDS, action: () -> Unit) {
    Handler().postDelayed(action, timeUnit.toMillis(delay))
}

/**
 * Extension method to run block of code on UI Thread after specific Delay.
 */
fun runDelayedOnUiThread(delay: Long, timeUnit: TimeUnit = TimeUnit.MILLISECONDS, action: () -> Unit) {
    ContextHandler.handler.postDelayed(action, timeUnit.toMillis(delay))
}

/**
 * Extension method to get the TAG name for all object
 */
fun <T : Any> T.TAG() = this::class.simpleName


val <T> T.exhaustive: T
    get() = this


fun <T> T.asNullable(): T? = this


/**
 * Provides handler and mainThread.
 */
private object ContextHandler {
    val handler = Handler(Looper.getMainLooper())
    val mainThread = Looper.getMainLooper().thread
}