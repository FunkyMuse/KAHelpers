package com.crazylegend.kotlinextensions.network

import android.content.Context
import android.net.wifi.WifiManager
import com.crazylegend.kotlinextensions.tryOrElse
import com.crazylegend.kotlinextensions.tryOrNull
import java.net.InetAddress
import java.net.UnknownHostException


/**
 * Created by hristijan on 6/21/19 to long live and prosper !
 */

fun Context.getIPAddress(defaultAddress: String = "127.0.0.1"): String {
    return tryOrNull {
        getInetAddress()?.hostAddress
    } ?: defaultAddress
}

@Throws(UnknownHostException::class)
fun Context.getInetAddress(): InetAddress? {
    val wifiMgr = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
    val wifiInfo = wifiMgr.connectionInfo
    val ip = wifiInfo.ipAddress

    return if (ip == 0) {
        null
    } else {
        val ipAddress = convertIpAddress(ip)
        InetAddress.getByAddress(ipAddress)
    }
}

private fun convertIpAddress(ip: Int): ByteArray {
    return byteArrayOf((ip and 0xFF).toByte(), (ip shr 8 and 0xFF).toByte(), (ip shr 16 and 0xFF).toByte(), (ip shr 24 and 0xFF).toByte())
}