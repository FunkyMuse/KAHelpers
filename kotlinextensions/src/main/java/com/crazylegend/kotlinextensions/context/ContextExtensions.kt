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


inline fun <reified T> Context.intent(body: Intent.() -> Unit ): Intent {
    val intent = Intent(this, T::class.java)
    intent.body()
    return intent
}

inline fun <reified T> Context.startActivity(body: Intent.() -> Unit ) {
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


val Context.isOnline : Boolean get()  {
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

                return nc!!.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || nc.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
            }
        }
    }

    return false
}


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