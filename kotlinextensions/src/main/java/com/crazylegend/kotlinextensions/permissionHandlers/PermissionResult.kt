package com.crazylegend.kotlinextensions.permissionHandlers


/**
 * Created by hristijan on 6/17/19 to long live and prosper !
 */

sealed class PermissionResult {
    class PermissionGranted(val requestId: Int) : PermissionResult()
    class PermissionDenied(
            val requestId: Int,
            val deniedPermissions: List<String>
    ) : PermissionResult()

    class ShowRationale(val requestId: Int) : PermissionResult()
    class PermissionDeniedPermanently(
            val requestId: Int,
            val permanentlyDeniedPermissions: List<String>
    ) : PermissionResult()
}