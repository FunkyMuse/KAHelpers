package com.crazylegend.context

import android.content.Context
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkCapabilities.NET_CAPABILITY_INTERNET
import android.os.BatteryManager
import android.os.Build
import android.telephony.TelephonyManager
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.crazylegend.contextgetters.batteryManager
import com.crazylegend.contextgetters.connectivityManager
import com.crazylegend.contextgetters.telephonyManager
import com.crazylegend.contextgetters.uiModeManager
import java.io.InputStream

/**
 * Created by crazy on 9/7/20 to long live and prosper !
 */

fun Context.dp2px(dpValue: Float): Int {
    return (dpValue * resources.displayMetrics.density + 0.5f).toInt()
}

fun Context.dp2px(dpValue: Int): Int {
    return (dpValue * resources.displayMetrics.density + 0.5f).toInt()
}

fun Context.px2dp(pxValue: Int): Float {
    return pxValue / resources.displayMetrics.density + 0.5f
}

fun Context.px2dp(pxValue: Float): Float {
    return pxValue / resources.displayMetrics.density + 0.5f
}

val Context.hasInternetCapabilities: Boolean
    @RequiresApi(Build.VERSION_CODES.M)
    get() = connectivityManager?.activeNetwork.hasInternetCapabilities(this)

@RequiresApi(Build.VERSION_CODES.M)
fun Network?.hasInternetCapabilities(context: Context): Boolean {
    return context.connectivityManager?.getNetworkCapabilities(this)?.hasInternetCapabilities
            ?: return false
}



val NetworkCapabilities.hasInternetCapabilities
    @RequiresApi(Build.VERSION_CODES.M)
    get() = hasCapability(NET_CAPABILITY_INTERNET) || hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)


/**
 * Method to get Region based on SIM card
 * i.e., it will return country region
 *
 * @return  two letter country code
 */
fun Context.getRegionFromSimCard(): String? = telephonyManager?.simCountryIso

/**
 * Method which provides boolean result for simcard
 *
 * @return **true** if sim card is present in device,
 * **false** if sim card is not present in device
 */
fun Context.isSimPresentInDevice(): Boolean {
    return telephonyManager?.simState != TelephonyManager.SIM_STATE_ABSENT
}

val Context.getBatteryPercentage get() = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)


/**
 * Opens raw input stream for reading
 * @receiver Context
 * @param rawId Int
 * @param inputStream [@kotlin.ExtensionFunctionType] Function1<InputStream, Unit>
 */
fun Context.getRaw(rawId: Int, inputStream: InputStream.() -> Unit) = resources.openRawResource(rawId).use { it.inputStream() }


fun Context.getTypedArray(typedArrayID: Int) = resources.obtainTypedArray(typedArrayID)


fun Context.getBitmapFromResource(drawableRes: Int): Bitmap? {
    var bitmap: Bitmap? = null
    val drawable = ContextCompat.getDrawable(this, drawableRes)
    val canvas = Canvas()
    drawable?.apply {
        bitmap = Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)
        canvas.setBitmap(bitmap)
        setBounds(0, 0, intrinsicWidth, intrinsicHeight)
        draw(canvas)
    }
    return bitmap
}

val Context.isTV get() = uiModeManager?.currentModeType == Configuration.UI_MODE_TYPE_TELEVISION
