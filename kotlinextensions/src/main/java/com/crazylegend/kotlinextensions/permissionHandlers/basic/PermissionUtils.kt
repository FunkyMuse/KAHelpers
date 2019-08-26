package com.crazylegend.kotlinextensions.permissionHandlers.basic

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.edit


/**
 * Created by hristijan on 8/26/19 to long live and prosper !
 */


object PermissionUtils {

    @RequiresApi(api = Build.VERSION_CODES.M)
    fun neverAskAgainSelected(activity: Activity, permission: String): Boolean {
        val prevShouldShowStatus = getRatinaleDisplayStatus(activity, permission)
        val currShouldShowStatus = activity.shouldShowRequestPermissionRationale(permission)
        return prevShouldShowStatus != currShouldShowStatus
    }

    fun setShouldShowStatus(context: Context, permission: String) {
        val genPrefs = context.getSharedPreferences("GENERIC_PREFERENCES", MODE_PRIVATE)
        genPrefs.edit(true){
            putBoolean(permission, true)
        }
    }

    fun getRatinaleDisplayStatus(context: Context, permission: String): Boolean {
        val genPrefs = context.getSharedPreferences("GENERIC_PREFERENCES", MODE_PRIVATE)
        return genPrefs.getBoolean(permission, false)
    }
}


fun Activity.requestPermission(permissionName: String, permissionRequestCode: Int,
                               displayNeverAskAgainDialog: () -> Unit = {},
                               onShouldShowRationale: () -> Unit = {},
                               onGranted: () -> Unit = {}) {


    if (ContextCompat.checkSelfPermission(this, permissionName) != PackageManager.PERMISSION_GRANTED) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (PermissionUtils.neverAskAgainSelected(this, permissionName)) {
                displayNeverAskAgainDialog()
            } else {
                // Permission is not granted
                // Should we show an explanation?
                if (shouldShowRequestPermissionRationale(this, permissionName)) {
                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.
                    onShouldShowRationale()
                } else {
                    // No explanation needed, we can request the permission.
                    ActivityCompat.requestPermissions(this, arrayOf(permissionName), permissionRequestCode)
                }
            }
        } else {

            // Permission is not granted
            // Should we show an explanation?
            if (shouldShowRequestPermissionRationale(this, permissionName)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                onShouldShowRationale()
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this, arrayOf(permissionName), permissionRequestCode)
            }
        }


    } else {
        // Permission has already been granted
        onGranted()
    }
}



fun Context.checkIfPermissionWasGrantedOnResult(grantResults: IntArray, onDenied: () -> Unit, onGranted: () -> Unit) {
    return if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
        // permission was granted, yay! Do the
        // contacts-related task you need to do.
        onGranted()
    } else {
        // permission denied, boo! Disable the
        // functionality that depends on this permission.
        onDenied()
        //PermissionUtils.setShouldShowStatus(this, SEND_SMS)
    }
}

/*
override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {

    when (requestCode) {
        123 -> {
            checkIfPermissionWasGrantedOnResult(grantResults, {
                shortToast("PERMISSION DENIED")
            }, {
                shortToast("PERMISSION GRANTED")
            })
            return
        }

        // Add other 'when' lines to check for other
        // permissions this app might request.
        else -> {
            // Ignore all other requests.
        }
    }

    super.onRequestPermissionsResult(requestCode, permissions, grantResults)
}*/
