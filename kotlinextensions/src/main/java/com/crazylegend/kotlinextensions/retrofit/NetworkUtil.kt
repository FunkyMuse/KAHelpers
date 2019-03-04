package com.crazylegend.kotlinextensions.retrofit

import android.content.Context
import android.net.ConnectivityManager

/**
 * Created by Hristijan on 1/25/19 to long live and prosper !
 */
object NetworkUtil {

    fun isOnline(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

}