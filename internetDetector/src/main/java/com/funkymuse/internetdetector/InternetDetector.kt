package com.funkymuse.internetdetector

import android.Manifest.permission.ACCESS_NETWORK_STATE
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import androidx.annotation.RequiresPermission
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow



class InternetDetector(private val context: Context, private val serverUrl: String = "https://www.google.com/",
                       private val timeOut: Int = 10 * 1000) {

    private val connectivityManager get() = context. getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager

    @RequiresPermission(allOf = [ACCESS_NETWORK_STATE])
    val state = callbackFlow {
        val networkCallback = NetworkCallback(this, context, serverUrl, timeOut)

        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.N -> connectivityManager?.registerDefaultNetworkCallback(networkCallback)
            else -> {
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

    private class NetworkCallback(private val liveData: ProducerScope<Boolean>, private val context: Context,
                                  private val serverUrl: String, private val timeOut: Int) : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            liveData.trySend(context.isURLReachable(serverUrl, timeOut)) //checks for the real connection
        }

        override fun onLost(network: Network) {
            liveData.trySend(false)
        }
    }

}
