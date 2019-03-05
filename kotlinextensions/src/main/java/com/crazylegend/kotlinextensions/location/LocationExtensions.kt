package com.crazylegend.kotlinextensions.location

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.location.Criteria
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import androidx.annotation.RequiresPermission


/**
 * Created by hristijan on 3/5/19 to long live and prosper !
 */

/**
 * Gets a single location update, returning the current location when found through a callback.
 *
 * This function does assume we have permission already.
 */
@RequiresPermission(allOf = [ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION])
inline fun LocationManager.requestSingleUpdate(
        criteria: Criteria = Criteria(),
        crossinline onLocationHad: (location: Location) -> Unit = {_ ->}
) {
    requestSingleUpdate(criteria, object : LocationListener {
        override fun onLocationChanged(location: Location) {
            onLocationHad(location)
        }

        override fun onProviderDisabled(p0: String?) {
        }

        override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {
        }

        override fun onProviderEnabled(p0: String?) {
        }
    }, Looper.getMainLooper())
}