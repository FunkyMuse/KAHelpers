package com.funkymuse.kotlinextensions.location

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Context
import android.location.LocationListener
import android.location.LocationManager
import androidx.annotation.RequiresPermission

/**
 * Very basic location provider to enable location updates.
 * Please note that this approach is very minimal and try to implement a more
 * advanced location provider for your app. (see https://developer.android.com/training/location/index.html)
 */
class LocationProvider(context: Context,
                       /** location listener called on each location update  */
                       private val locationListener: LocationListener,
                       /** callback called when no providers are enabled  */
                       private val callback: ErrorCallback) {
    /** system's locationManager allowing access to GPS / Network position  */

    private val locationManager: LocationManager?

    /** is gpsProvider and networkProvider enabled in system settings  */
    private var gpsProviderEnabled = false
    private var networkProviderEnabled = false

    @RequiresPermission(allOf = [ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION])
    fun onPause() {
        if (locationManager != null && (gpsProviderEnabled || networkProviderEnabled)) {
            locationManager.removeUpdates(locationListener)
        }
    }

    @RequiresPermission(allOf = [ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION])
    fun onResume() {
        if (locationManager != null) {

            // check which providers are available are available
            gpsProviderEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            networkProviderEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

            // is GPS provider enabled?
            if (gpsProviderEnabled) {
                val lastKnownGPSLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                if (lastKnownGPSLocation != null && lastKnownGPSLocation.time > System.currentTimeMillis() - LOCATION_OUTDATED_WHEN_OLDER_MS) {
                    locationListener.onLocationChanged(lastKnownGPSLocation)
                }
                if (locationManager.getProvider(LocationManager.GPS_PROVIDER) != null) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_UPDATE_MIN_TIME_GPS.toLong(), LOCATION_UPDATE_DISTANCE_GPS.toFloat(), locationListener)
                }
            }

            //is Network / WiFi positioning provider available?
            if (networkProviderEnabled) {
                val lastKnownNWLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                if (lastKnownNWLocation != null && lastKnownNWLocation.time > System.currentTimeMillis() - LOCATION_OUTDATED_WHEN_OLDER_MS) {
                    locationListener.onLocationChanged(lastKnownNWLocation)
                }
                if (locationManager.getProvider(LocationManager.NETWORK_PROVIDER) != null) {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, LOCATION_UPDATE_MIN_TIME_NW.toLong(), LOCATION_UPDATE_DISTANCE_NW.toFloat(), locationListener)
                }
            }

            // user didn't check a single positioning in the location settings, recommended: handle this event properly in your app, e.g. forward user directly to location-settings, new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS )
            if (!gpsProviderEnabled && !networkProviderEnabled) {
                callback.noProvidersEnabled()
            }
        }
    }

    interface ErrorCallback {
        fun noProvidersEnabled()
    }

    companion object {
        /** location updates should fire approximately every second  */
        private const val LOCATION_UPDATE_MIN_TIME_GPS = 1000

        /** location updates should fire, even if last signal is same than current one (0m distance to last location is OK)  */
        private const val LOCATION_UPDATE_DISTANCE_GPS = 0

        /** location updates should fire approximately every second  */
        private const val LOCATION_UPDATE_MIN_TIME_NW = 1000

        /** location updates should fire, even if last signal is same than current one (0m distance to last location is OK)  */
        private const val LOCATION_UPDATE_DISTANCE_NW = 0

        /** to faster access location, even use 10 minute old locations on start-up  */
        private const val LOCATION_OUTDATED_WHEN_OLDER_MS = 1000 * 60 * 10
    }

    init {
        locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        gpsProviderEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        networkProviderEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }
}