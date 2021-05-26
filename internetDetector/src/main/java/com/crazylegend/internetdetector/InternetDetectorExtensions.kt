package com.crazylegend.internetdetector

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import androidx.annotation.RequiresApi
import com.crazylegend.common.isOnline
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import java.net.HttpURLConnection
import java.net.URL

/**
 * Created by funkymuse on 5/26/21 to long live and prosper !
 */


/**
 * Sends internet statuses as a simple flow of booleans
 * true if user has an active internet connection
 * false if user hasn't
 * @receiver Context
 * @param serverUrl String the url to ping for successfull internet connection
 * @param timeOut Int timeout for the ping
 * @return Flow<Boolean>
 */
fun Context.internetDetection(
    serverUrl: String = "https://www.google.com/",
    timeOut: Int = 10 * 1000
) = InternetDetector(this, serverUrl, timeOut).state


/**
 * Must not be called on the main thread
 * @receiver Context
 * @param serverUrl String
 * @param timeOut Int default is 10 seconds, timeout is in ms
 * @return Boolean
 */
fun Context.isURLReachable(serverUrl: String, timeOut: Int = 10 * 1000): Boolean {
    if (isOnline) {
        return try {
            with(URL(serverUrl).openConnection() as HttpURLConnection) {
                connectTimeout = timeOut
                connect()
                responseCode == 200
            }
        } catch (e: Throwable) {
            false
        }
    }
    return false
}

@RequiresApi(Build.VERSION_CODES.M)
fun Context.internetCapabilitiesCallback() = callbackFlow<Boolean> {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
    val callback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            trySend(true)
        }

        override fun onLost(network: Network) {
            trySend(false)
        }
    }
    val networkRequest = NetworkRequest.Builder()
        .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        .build()
    connectivityManager?.registerNetworkCallback(networkRequest, callback)

    awaitClose {
        connectivityManager?.unregisterNetworkCallback(callback)
    }
}