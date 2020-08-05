package com.crazylegend.kotlinextensions.permissions

import android.Manifest.permission.*
import android.os.Build
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity


/**
 * Created by crazy on 6/15/20 to long live and prosper !
 */

//region Multiple permissions
inline fun Fragment.askForMultiplePermissions(crossinline onDenied: () -> Unit = {}, crossinline onPermissionsGranted: () -> Unit = {}) =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { result ->
            val granted = result.map { it.value }.filter { it == false }
            if (granted.isNullOrEmpty()) {
                onPermissionsGranted()
            } else {
                onDenied()
            }
        }

inline fun FragmentActivity.askForMultiplePermissions(crossinline onDenied: () -> Unit = {}, crossinline onPermissionsGranted: () -> Unit = {}) =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { result ->
            val granted = result.map { it.value }.filter { it == false }
            if (granted.isNullOrEmpty()) {
                onPermissionsGranted()
            } else {
                onDenied()
            }
        }
//endregion

//region Single permission
inline fun Fragment.askForSinglePermission(crossinline onDenied: () -> Unit = {}, crossinline onPermissionsGranted: () -> Unit = {}) =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                onPermissionsGranted()
            } else {
                onDenied()
            }
        }

inline fun FragmentActivity.askForSinglePermission(crossinline onDenied: () -> Unit = {}, crossinline onPermissionsGranted: () -> Unit = {}) =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                onPermissionsGranted()
            } else {
                onDenied()
            }
        }
//endregion

//region GPS
var enableLocationRetryCount = 1
inline fun Fragment.enableGPS(crossinline onDenied: () -> Unit = {}, crossinline onLocationGranted: () -> Unit = {}) = registerForActivityResult(LocationSettingsContract()) {
    if (enableLocationRetryCount <= 2) {
        onLocationGranted()
        enableLocationRetryCount++
    } else {
        onDenied()
        enableLocationRetryCount = 1
    }
}

inline fun FragmentActivity.enableGPS(crossinline onDenied: () -> Unit = {}, crossinline onLocationGranted: () -> Unit = {}) = registerForActivityResult(LocationSettingsContract()) {
    if (enableLocationRetryCount <= 2) {
        onLocationGranted()
        enableLocationRetryCount++
    } else {
        onDenied()
        enableLocationRetryCount = 1
    }
}
//endregion


//region Foreground location
inline fun Fragment.getForegroundLocationPermission(crossinline onDenied: () -> Unit = {}, crossinline onLocationGranted: () -> Unit = {}) =
        askForMultiplePermissions(onDenied, onLocationGranted).launch(arrayOf(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION))

inline fun FragmentActivity.getForegroundLocationPermission(crossinline onDenied: () -> Unit = {}, crossinline onLocationGranted: () -> Unit = {}) =
        askForMultiplePermissions(onDenied, onLocationGranted).launch(arrayOf(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION))
//endregion


//region Background location
inline fun Fragment.getBackgroundLocationPermission(crossinline onDenied: () -> Unit = {}, crossinline onLocationGranted: () -> Unit = {}) =
        when {
            Build.VERSION.SDK_INT == Build.VERSION_CODES.Q -> {
                askForSinglePermission(onDenied, onLocationGranted).launch(ACCESS_BACKGROUND_LOCATION)
            }
            Build.VERSION.SDK_INT < Build.VERSION_CODES.Q -> {
                getForegroundLocationPermission(onDenied, onLocationGranted)
            }
            Build.VERSION.SDK_INT == Build.VERSION_CODES.R -> {
                getForegroundLocationPermission(onDenied) {
                    askForSinglePermission(onDenied, onLocationGranted).launch(ACCESS_BACKGROUND_LOCATION)
                }
            }
            else -> {

            }
        }


inline fun FragmentActivity.getBackgroundLocationPermission(crossinline onDenied: () -> Unit = {}, crossinline onLocationGranted: () -> Unit = {}) =
        when {
            Build.VERSION.SDK_INT == Build.VERSION_CODES.Q -> {
                askForSinglePermission(onDenied, onLocationGranted).launch(ACCESS_BACKGROUND_LOCATION)
            }
            Build.VERSION.SDK_INT < Build.VERSION_CODES.Q -> {
                getForegroundLocationPermission(onDenied, onLocationGranted)
            }
            Build.VERSION.SDK_INT == Build.VERSION_CODES.R -> {
                getForegroundLocationPermission(onDenied) {
                    askForSinglePermission(onDenied, onLocationGranted).launch(ACCESS_BACKGROUND_LOCATION)
                }
            }
            else -> {

            }
        }
//endregion

//region camera permission
inline fun Fragment.getCameraPermission(crossinline onDenied: () -> Unit = {}, crossinline onGranted: () -> Unit = {}) =
        askForSinglePermission(onDenied, onGranted).launch(CAMERA)


inline fun FragmentActivity.getCameraPermission(crossinline onDenied: () -> Unit = {}, crossinline onGranted: () -> Unit = {}) =
        askForSinglePermission(onDenied, onGranted).launch(CAMERA)
//endregion
