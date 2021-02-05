package com.crazylegend.kotlinextensions.context

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkCapabilities.NET_CAPABILITY_INTERNET
import android.net.NetworkRequest
import android.os.Build
import androidx.annotation.RequiresApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

/**
 * Created by crazy on 9/7/20 to long live and prosper !
 */

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

val Context.hasInternetCapabilities: Boolean
    @RequiresApi(Build.VERSION_CODES.M)
    get() = connectivityManager?.activeNetwork.hasInternetCapabilities(this)

@RequiresApi(Build.VERSION_CODES.M)
fun Network?.hasInternetCapabilities(context: Context): Boolean {
    return context.connectivityManager?.getNetworkCapabilities(this)?.hasInternetCapabilities
            ?: return false
}

@RequiresApi(Build.VERSION_CODES.M)
fun Context.internetCapabilitiesCallback() = callbackFlow<Boolean> {
    val callback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            if (!isClosedForSend) {
                offer(true)
            }
        }

        override fun onLost(network: Network) {
            if (!isClosedForSend) {
                offer(false)
            }
        }
    }
    val networkRequest = NetworkRequest.Builder()
            .addCapability(NET_CAPABILITY_INTERNET)
            .build()
    connectivityManager?.registerNetworkCallback(networkRequest, callback)

    awaitClose {
        connectivityManager?.unregisterNetworkCallback(callback)
    }
}

val NetworkCapabilities.hasInternetCapabilities
    @RequiresApi(Build.VERSION_CODES.M)
    get() = hasCapability(NET_CAPABILITY_INTERNET) || hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)