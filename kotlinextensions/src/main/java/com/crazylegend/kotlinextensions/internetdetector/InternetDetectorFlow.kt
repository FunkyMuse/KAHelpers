package com.crazylegend.kotlinextensions.internetdetector

import android.Manifest.permission.ACCESS_NETWORK_STATE
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import androidx.annotation.RequiresPermission
import com.crazylegend.kotlinextensions.context.connectivityManager
import com.crazylegend.kotlinextensions.context.isOnline
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow


/**
 * Created by Hristijan on 2/1/19 to long live and prosper !
 */
class InternetDetectorFlow(private val context: Context) {

    private val connectivityManager get() = context.connectivityManager

    @RequiresPermission(allOf = [ACCESS_NETWORK_STATE])
    val state = callbackFlow {
        val networkCallback = NetworkCallback(this)

        if (!isClosedForSend)
            this.offer(context.isOnline)

        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.N -> connectivityManager?.registerDefaultNetworkCallback(networkCallback)
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP -> {
                val builder = NetworkRequest.Builder()
                        .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                        .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                        .addTransportType(NetworkCapabilities.TRANSPORT_VPN)
                        .addTransportType(NetworkCapabilities.TRANSPORT_ETHERNET)
                connectivityManager?.registerNetworkCallback(builder.build(), networkCallback)
            }
        }

        awaitClose {
            connectivityManager?.unregisterNetworkCallback(networkCallback)
        }
    }

    class NetworkCallback(private val liveData: ProducerScope<Boolean>) : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            if (!liveData.isClosedForSend)
                liveData.offer(true)
        }

        override fun onLost(network: Network) {
            if (!liveData.isClosedForSend)
                liveData.offer(false)
        }
    }

}
