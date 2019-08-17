package com.crazylegend.kotlinextensions.permissionHandlers

import androidx.lifecycle.LiveData


/**
 * Created by hristijan on 6/17/19 to long live and prosper !
 */

/**
 * Interface definition for a callback to get [LiveData] of [PermissionResult]
 *
 * Implement this interface to get [LiveData] for observing permission request result.
 */
interface PermissionObserver {
    fun setupObserver(permissionResultLiveData: LiveData<PermissionResult>)
}