package com.crazylegend.kotlinextensions.network

import android.content.Context
import com.crazylegend.kotlinextensions.context.wifiManager
import java.net.InetAddress
import java.net.UnknownHostException


/**
 * Created by hristijan on 6/21/19 to long live and prosper !
 */

@Throws(UnknownHostException::class)
fun Context.getIpAddress(): InetAddress? {
    val wifiMgr = wifiManager
    val wifiInfo = wifiMgr?.connectionInfo
    val ip = wifiInfo?.ipAddress

    return if (ip == 0) {
        null
    } else {
        val ipAddress = ip?.let { convertIpAddress(it) }
        InetAddress.getByAddress(ipAddress)
    }
}

private fun convertIpAddress(ip: Int): ByteArray {
    return byteArrayOf(
            (ip and 0xFF).toByte(),
            (ip shr 8 and 0xFF).toByte(),
            (ip shr 16 and 0xFF).toByte(),
            (ip shr 24 and 0xFF).toByte()
    )
}