package com.crazylegend.kotlinextensions.context

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.Uri
import android.provider.Settings
import android.util.DisplayMetrics
import android.view.Gravity
import android.widget.Toast
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


inline fun <reified T : Any> Context.intent(body: Intent.() -> Unit): Intent {
    val intent = Intent(this, T::class.java)
    intent.body()
    return intent
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


fun Context.snackBar(text: String, actionText: String, length: Int, action: () -> Unit) {
    this as AppCompatActivity
    val snackbar =
        Snackbar.make(this.findViewById(android.R.id.content), text, length)
    snackbar.setAction(actionText) {
        action()
        snackbar.dismiss()
    }
    snackbar.show()

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


val Context.isOnline : Boolean get() {
    val connectivityManager = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkInfo = connectivityManager.activeNetworkInfo
    return networkInfo != null && networkInfo.isConnected
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