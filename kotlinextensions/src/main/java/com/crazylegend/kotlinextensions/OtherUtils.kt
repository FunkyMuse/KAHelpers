package com.crazylegend.kotlinextensions

import android.content.Context
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.crazylegend.kotlinextensions.basehelpers.InMemoryCache
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
inline val isMainThread: Boolean get() = Looper.myLooper() == Looper.getMainLooper()


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

/**
 * try the code in [runnable], If it runs then its perfect if its not, It won't crash your app.
 */
fun tryOrIgnore(runnable: () -> Unit) = try {
    runnable()
} catch (e: Exception) {
    e.printStackTrace()
}

/**
 * get CurrentTimeInMillis from System.currentTimeMillis
 */
inline val currentTimeMillis: Long get() = System.currentTimeMillis()


/**
 * get Saved Data from memory, null if it os not exists
 */
fun getFromMemory(key: String): Any? = InMemoryCache.get(key)


/**
 * put Something In Memory to use it later
 */
fun putInMemory(key: String, any: Any?) = InMemoryCache.put(key, any)


fun Context.dp2px(dpValue: Float): Int {
    return (dpValue * resources.displayMetrics.density + 0.5f).toInt()
}
fun Context.dp2px(dpValue: Int): Int {
    return (dpValue * resources.displayMetrics.density + 0.5f).toInt()
}
fun Context.px2dp(pxValue: Int): Float {
    return pxValue / resources.displayMetrics.density + 0.5f
}
fun Context.px2dp(pxValue: Float): Float {
    return pxValue / resources.displayMetrics.density + 0.5f
}
fun Fragment.dp2px(dpValue: Float): Int {
    return requireActivity().dp2px(dpValue)
}
fun Fragment.dp2px(dpValue: Int): Int {
    return requireActivity().dp2px(dpValue)
}
fun Fragment.px2dp(pxValue: Int): Float {
    return requireActivity().px2dp(pxValue)
}
fun View.px2dp(pxValue: Float): Float? {
    return context?.px2dp(pxValue)
}
fun View.dp2px(dpValue: Float): Int? {
    return context?.dp2px(dpValue)
}
fun View.dp2px(dpValue: Int): Int? {
    return context?.dp2px(dpValue)
}
fun View.px2dp(pxValue: Int): Float? {
    return context?.px2dp(pxValue)
}
fun RecyclerView.ViewHolder.px2dp(pxValue: Float): Float? {
    return itemView.px2dp(pxValue)
}
fun RecyclerView.ViewHolder.dp2px(dpValue: Float): Int? {
    return itemView.dp2px(dpValue)
}
fun RecyclerView.ViewHolder.dp2px(dpValue: Int): Int? {
    return itemView.dp2px(dpValue)
}
fun RecyclerView.ViewHolder.px2dp(pxValue: Int): Float? {
    return itemView.px2dp(pxValue)
}
