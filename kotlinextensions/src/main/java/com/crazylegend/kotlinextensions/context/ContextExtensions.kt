package com.crazylegend.kotlinextensions.context

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.telephony.TelephonyManager
import android.util.DisplayMetrics
import android.view.Gravity
import android.widget.Toast
import androidx.annotation.IntRange
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar


/**
 * Created by Hristijan on 2/1/19 to long live and prosper !
 */

inline fun <reified T> Context.launch() {
    this.startActivity(Intent(this, T::class.java))
}


fun Context.shortToast(text: String) {
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
}

fun Context.longToast(text: String) {
    Toast.makeText(this, text, Toast.LENGTH_LONG).show()
}

fun Context.shortToast(resId: Int) = Toast.makeText(this, resId, Toast.LENGTH_SHORT).show()
fun Context.longToast(resId: Int) = Toast.makeText(this, resId, Toast.LENGTH_LONG).show()


inline fun <reified T> Context.intent(body: Intent.() -> Unit): Intent {
    val intent = Intent(this, T::class.java)
    intent.body()
    return intent
}

inline fun <reified T> Context.startActivity(body: Intent.() -> Unit) {
    val intent = Intent(this, T::class.java)
    intent.body()
    startActivity(intent)
}

fun AppCompatActivity.showBackButton() {
    this.supportActionBar?.setDisplayHomeAsUpEnabled(true)
}

fun AppCompatActivity.hideToolbar() {
    this.supportActionBar?.hide()
}

fun AppCompatActivity.showToolbar() {
    this.supportActionBar?.show()
}


fun Context.snackBar(text: String, actionText: String, length: Int, action: () -> Unit): Snackbar {
    this as AppCompatActivity
    val snackbar =
            Snackbar.make(this.findViewById(android.R.id.content), text, length)
    snackbar.setAction(actionText) {
        action()
        snackbar.dismiss()
    }
    snackbar.show()

    return snackbar
}


fun Context.rateUs() {
    try {
        startActivity(Intent("android.intent.action.VIEW", Uri.parse("market://details?id=$packageName")))
    } catch (e: ActivityNotFoundException) {
        startActivity(
                Intent(
                        "android.intent.action.VIEW",
                        Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
                )
        )
    }
}


fun Context.convertDpToPixel(dp: Float): Float {
    val resources = this.resources
    val metrics = resources.displayMetrics
    return dp * (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
}


val Context.isLocationEnabled: Boolean
    get() = (getSystemService(Context.LOCATION_SERVICE) as LocationManager?)?.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
            ?: false


fun Context.convertPixelsToDp(px: Float): Float {
    val resources = this.resources
    val metrics = resources.displayMetrics
    return px / (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
}


val Context.deviceID
    @SuppressLint("HardwareIds")
    get() = Settings.Secure.getString(this.contentResolver, Settings.Secure.ANDROID_ID)


val Context.isOnline: Boolean
    get() {
        val cm = connectivityManager

        if (cm != null) {
            if (Build.VERSION.SDK_INT < 23) {
                val ni = cm.activeNetworkInfo
                if (ni != null) {
                    return ni.isConnected && (ni.type == ConnectivityManager.TYPE_WIFI || ni.type == ConnectivityManager.TYPE_MOBILE)
                }
            } else {
                val n = cm.activeNetwork

                if (n != null) {
                    val nc = cm.getNetworkCapabilities(n)
                    return if (nc == null) {
                        false
                    } else {
                        nc.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || nc.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                    }
                }
            }
        }
        return false
    }


/**
 * 0 = no connection info available
 * 1 = mobile data
 * 2 = wifi
 * @receiver Context
 * @return Int
 */
@IntRange(from = 0, to = 2)
fun Context.getConnectionType(): Int {
    var result = 0 // Returns connection type. 0: none; 1: mobile data; 2: wifi
    val cm = connectivityManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        cm?.run {
            cm.getNetworkCapabilities(cm.activeNetwork)?.run {
                if (hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    result = 2
                } else if (hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    result = 1
                }
            }
        }
    } else {
        cm?.run {
            cm.activeNetworkInfo?.run {
                if (type == ConnectivityManager.TYPE_WIFI) {
                    result = 2
                } else if (type == ConnectivityManager.TYPE_MOBILE) {
                    result = 1
                }
            }
        }
    }
    return result
}

/**
 * 0 = no telephony manager available
 * 1 = unknown telephony
 * 2 = 2g internet
 * 3 = 3g internet
 * 4 = 4g internet
 * 5 = 5g internet
 * @receiver Context
 * @return Int
 */
@IntRange(from = 0, to = 5)
fun Context.deviceNetworkType(): Int {
    val NO_TELEPHONY = 0
    val UNKNOWN = 1
    val NET2G = 2
    val NET3G = 3
    val NET4G = 4
    val NET5G = 5
    val mTelephonyManager = telephonyManager ?: return NO_TELEPHONY
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
            && mTelephonyManager.networkType == TelephonyManager.NETWORK_TYPE_NR) { //New Radio
        return NET5G
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
        when (mTelephonyManager.networkType) {
            TelephonyManager.NETWORK_TYPE_IWLAN      //Industrial Wireless Local Area Network, transfer IP data between a mobile device and operator’s core network through a Wi-Fi access
            -> return NET4G
            TelephonyManager.NETWORK_TYPE_TD_SCDMA   //3G Time division synchronous code division multiple access, China-Mobile standard
            -> return NET3G
            TelephonyManager.NETWORK_TYPE_GSM        // Global System for Mobile Communications, standard for 2g
            -> return NET2G
        }
    }
    return when (mTelephonyManager.networkType) {
        TelephonyManager.NETWORK_TYPE_GPRS,     //2G(2.5) General Packet Radio Service 114 kbps
        TelephonyManager.NETWORK_TYPE_EDGE,     //2G(2.75G) Enhanced Data Rate for GSM Evolution 384 kbps
        TelephonyManager.NETWORK_TYPE_CDMA,     //2G Code Division Multiple Access  ~ 14-64 kbps
        TelephonyManager.NETWORK_TYPE_1xRTT,    //2G CDMA2000 1xRTT (RTT - Radio transmission technology) 144 kbps,
        TelephonyManager.NETWORK_TYPE_IDEN      //2G Integrated Dispatch Enhanced Networks (part of 2G from Wikipedia)  ~25 kbps
        -> NET2G

        TelephonyManager.NETWORK_TYPE_UMTS,     //3G WCDMA 3G Universal Mobile Telecommunication System  ~ 400-7000 kbps
        TelephonyManager.NETWORK_TYPE_EVDO_0,   //3G (EVDO CDMA2000 1xEV-DO) Evolution - Data Only (Data Optimized) 153.6kps - 2.4 mbps belong 3G
        TelephonyManager.NETWORK_TYPE_EVDO_A,   //3G 1.8mbps - 3.1mbps belong 3G ~ 3.5G
        TelephonyManager.NETWORK_TYPE_EVDO_B,   //3G EV-DO Rev.B 14.7Mbps down 3.5G
        TelephonyManager.NETWORK_TYPE_HSDPA,    //3.5G WCDMA High Speed Downlink Packet Access 14.4mbps
        TelephonyManager.NETWORK_TYPE_HSUPA,    //3.5G High Speed Uplink Packet Access 1.4 - 5.8 mbps
        TelephonyManager.NETWORK_TYPE_HSPA,     //3G (part HSDPA,HSUPA) High Speed Packet Access    ~ 700-1700 kbps
        TelephonyManager.NETWORK_TYPE_EHRPD,    //3G CDMA2000 to LTE 4G Evolved High Rate Packet Data   ~ 1-2 Mbps
        TelephonyManager.NETWORK_TYPE_HSPAP     //3G HSPAP is faster than HSDPA  ~ 10-20 Mbps
        -> NET3G

        TelephonyManager.NETWORK_TYPE_LTE       //4G Long Term Evolution FDD-LTE and TDD-LTE, 3G transition, upgraded LTE Advanced is 4G ~ 10+ Mbps
        -> NET4G
        else -> UNKNOWN
    }
}


fun Context.showShortToastTop(message: String) {
    val toast = Toast.makeText(this, message, Toast.LENGTH_SHORT)

    toast.setGravity(Gravity.TOP or Gravity.END, 100, 200)
    toast.show()
}

fun Context.showLongToastTop(message: String) {
    val toast = Toast.makeText(this, message, Toast.LENGTH_LONG)

    toast.setGravity(Gravity.TOP or Gravity.END, 100, 200)
    toast.show()
}