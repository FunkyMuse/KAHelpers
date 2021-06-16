@file:Suppress("DEPRECATION")

package com.crazylegend.kotlinextensions.pip

import android.app.Activity
import android.app.PictureInPictureParams
import android.os.Build
import androidx.annotation.RequiresApi
import com.crazylegend.activity.hasPipPermission
import com.crazylegend.activity.supportsPictureInPicture
import com.crazylegend.intent.applicationDetailsIntent


/**
 * Created by crazy on 8/4/20 to long live and prosper !
 */


@RequiresApi(Build.VERSION_CODES.N)
inline fun Activity.enterPIPMode(builderActions: PictureInPictureParams.Builder.() -> Unit = {}) {
    if (supportsPictureInPicture) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
           val builder = PictureInPictureParams.Builder()
            builder.builderActions()
            enterPictureInPictureMode(builder.build())
        } else {
            enterPictureInPictureMode()
        }
    }
}

@RequiresApi(Build.VERSION_CODES.N)
inline fun Activity.checkPIPPermissionAndEnter(
        onCantHandleAction: () -> Unit = {},
        noinline builderActions: PictureInPictureParams.Builder.() -> Unit = {}) {
    if (!isInPictureInPictureMode) {
        if (supportsPictureInPicture) {
            if (hasPipPermission())
                enterPIPMode(builderActions)
            else {
                applicationDetailsIntent(onCantHandleAction)
            }
        }
    }
}



@RequiresApi(Build.VERSION_CODES.N)
inline fun Activity.checkPIPPermissions(onPermissionDenied: () -> Unit = {}, onPermissionGranted: () -> Unit) {
    if (!isInPictureInPictureMode) {
        if (supportsPictureInPicture) {
            if (hasPipPermission())
                onPermissionGranted()
            else {
                onPermissionDenied()
            }
        }
    }
}