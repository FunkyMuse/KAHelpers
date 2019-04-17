package com.crazylegend.kotlinextensions.internetdetector

import android.Manifest.permission.ACCESS_NETWORK_STATE
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.*
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import androidx.lifecycle.LiveData
import com.crazylegend.kotlinextensions.context.isOnline


/**
 * Created by Hristijan on 2/1/19 to long live and prosper !
 */
class InternetDetector(private val context: Context) : LiveData<Boolean>() {


    private val CONNECTIVITY_CHANGE = "android.net.conn.CONNECTIVITY_CHANGE"

    private  var intentFilter = IntentFilter(CONNECTIVITY_CHANGE)
    private var connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    private lateinit var networkCallback: NetworkCallback

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            networkCallback = NetworkCallback(this)
        }
    }

    @RequiresPermission(allOf = [ACCESS_NETWORK_STATE])
    override fun onActive() {
        super.onActive()
        updateConnection()
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.N -> connectivityManager.registerDefaultNetworkCallback(networkCallback)
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP -> {
                val builder = NetworkRequest.Builder().addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR).addTransportType(
                    NetworkCapabilities.TRANSPORT_WIFI
                )
                connectivityManager.registerNetworkCallback(builder.build(), networkCallback)
            }
            else -> {
                context.registerReceiver(networkReceiver, intentFilter)
            }
        }
    }

    override fun onInactive() {
        super.onInactive()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            connectivityManager.unregisterNetworkCallback(networkCallback)
        } else {
            context.unregisterReceiver(networkReceiver)
        }
    }


    private val networkReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            updateConnection()
        }
    }

    private fun updateConnection() {

       /* val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        postValue(activeNetwork?.isConnected == true)*/
        postValue(  context.isOnline)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)

    class NetworkCallback(private val liveData: InternetDetector) : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network?) {
            liveData.postValue(true)
        }

        override fun onLost(network: Network?) {
            liveData.postValue(false)
        }
    }


}
