package com.crazylegend.kotlinextensions.permissionHandlers.basic

import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment


/**
 * Created by hristijan on 8/26/19 to long live and prosper !
 */


object PermissionUtils {

    const val PERMISSION_REQUEST_CODE = 222


    fun requestPermission(activity: Activity, successAction: () -> Unit, requestedPermissions: Array<String>) {
        if (needPermissions(activity, requestedPermissions)) {
            getPermissions(activity, requestedPermissions)
        } else {
            successAction.invoke()
        }
    }

    fun requestPermission(fragment: Fragment, action: () -> Unit, requestedPermissions: Array<String>) {
        fragment.activity.let {
            if (needPermissions(it!!, requestedPermissions)) {
                getPermissions(fragment, requestedPermissions)
            } else {
                action.invoke()
            }
        }
    }

    fun permissionsResult(
            activity: Activity, requestedPermissions: Array<String>, requestCode: Int, grantResults: IntArray,
            successAction: () -> Unit, unSuccessAction: () -> Unit, errorMessageAction: (() -> Unit)? = null
    ) {
        if (requestCode == PERMISSION_REQUEST_CODE && grantResults.size == requestedPermissions.size) {
            var allGranted = true
            for (gr in grantResults) {
                allGranted = allGranted && gr == PackageManager.PERMISSION_GRANTED
            }
            when {
                allGranted -> successAction()
                shouldShowRationaleAllPermissions(activity, requestedPermissions) -> unSuccessAction.invoke()
                else -> errorMessageAction?.invoke()
            }
        }
    }

    private fun needPermissions(activity: Activity, requestedPermissions: Array<String>): Boolean {
        for (permission in requestedPermissions) {
            if (ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_DENIED) {
                return true
            }
        }
        return false
    }

    private fun shouldShowRationaleAllPermissions(activity: Activity, requestedPermissions: Array<String>): Boolean {
        for (permission in requestedPermissions) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                return true
            }
        }
        return false
    }

    private fun getPermissions(activity: Activity, requestedPermissions: Array<String>) =
            ActivityCompat.requestPermissions(activity, requestedPermissions, PERMISSION_REQUEST_CODE)

    private fun getPermissions(fragment: Fragment, requestedPermissions: Array<String>) =
            fragment.requestPermissions(requestedPermissions, PERMISSION_REQUEST_CODE)

}